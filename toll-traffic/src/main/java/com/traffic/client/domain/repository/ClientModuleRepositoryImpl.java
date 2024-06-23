package com.traffic.client.domain.repository;

import com.traffic.client.domain.Account.Account;
import com.traffic.client.domain.Account.CreditCard;
import com.traffic.client.domain.Account.POSTPay;
import com.traffic.client.domain.Account.PREPay;
import com.traffic.client.domain.User.ForeignUser;
import com.traffic.client.domain.User.NationalUser;
import com.traffic.client.domain.User.TollCustomer;
import com.traffic.client.domain.User.User;
import com.traffic.client.domain.Vehicle.*;
import com.traffic.dtos.PaymentTypeData;
import com.traffic.dtos.account.CreditCardDTO;
import com.traffic.dtos.account.PostPayDTO;
import com.traffic.dtos.account.PrePayDTO;
import com.traffic.dtos.user.ForeignUserDTO;
import com.traffic.dtos.user.NationalUserDTO;
import com.traffic.dtos.user.TollCustomerDTO;
import com.traffic.dtos.user.UserDTO;
import com.traffic.dtos.vehicle.*;
import com.traffic.events.CustomEvent;
import com.traffic.events.PREPayTollPassEvent;
import com.traffic.exceptions.ExternalApiException;
import com.traffic.payment.Interface.PaymentController;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@ApplicationScoped
public class ClientModuleRepositoryImpl implements ClientModuleRepository{

    @Inject
    private PaymentController paymentController;

    @Inject
    private Event<CustomEvent> event;

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<User> findByTag(Long tagId) {

        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

        Tag tag = em.find(Tag.class, tagId);

        CriteriaQuery<Vehicle> vehicleCB = criteriaBuilder.createQuery(Vehicle.class);
        Root<Vehicle> vehicleRoot = vehicleCB.from(Vehicle.class);

        CriteriaQuery<Link> linkCB = criteriaBuilder.createQuery(Link.class);
        Root<Link> linkRoot = linkCB.from(Link.class);

        vehicleCB.select(vehicleRoot)
                .where(criteriaBuilder.equal(vehicleRoot.get("tag"), tag));

        Vehicle vehicleDB = em.createQuery(vehicleCB).getSingleResult();

        em.clear();

        linkCB.select(linkRoot)
                .where(criteriaBuilder.equal(linkRoot.get("vehicle"), vehicleDB));

        Link linkDB = null;

        try {
            linkDB = em.createQuery(linkCB).getSingleResult();

        } catch (Exception e){
            System.err.println(e.getMessage());
        }

        User user = null;
        if(linkDB != null){
           user = linkDB.getUser();
        }


        return user != null ? Optional.of(user) : Optional.empty();
    }

    @Override
    public Optional<List<User>> listUsers() {

        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<User> userCriteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> userRoot = userCriteriaQuery.from(User.class);

        CriteriaQuery<User> selectAll = userCriteriaQuery.select(userRoot);
        TypedQuery<User> allUsers = em.createQuery(selectAll);
        List<User> users = allUsers.getResultList();

        return users.isEmpty() ? Optional.empty() : Optional.of(users);
    }

    @Transactional
    public void createUser(User user){
        try{
            em.persist(user);
            em.flush();
        }catch (Exception e){
            System.err.println(e.getMessage());
            throw e;
        }
    }

    @Override
    public Optional<User> getUserById(Long id) {

        if (id == null) {
            throw new IllegalArgumentException("El ID del usuario no puede ser nulo.");
        }
       User user = em.find(User.class, id);

        if (user != null) {
            return Optional.of(user);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Vehicle> getVehicleByTag(Long tagId) {

        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        Tag tag = em.find(Tag.class, tagId);

        CriteriaQuery<Vehicle> vehicleCB = criteriaBuilder.createQuery(Vehicle.class);
        Root<Vehicle> vehicleRoot = vehicleCB.from(Vehicle.class);

        vehicleCB.select(vehicleRoot)
                .where(criteriaBuilder.equal(vehicleRoot.get("tag"), tag));
        try{
            Vehicle vehicleDB = em.createQuery(vehicleCB).getSingleResult();

            return Optional.ofNullable(vehicleDB);
        }catch (Exception e) {
            return Optional.empty();
        }

    }

    //Vehiculos.

    @Transactional
    public void linkVehicle (Long userId, Vehicle vehicle){
        try{
            Optional<User> usrOPT = getUserById(userId);
            if(usrOPT.isPresent()){
                User user = usrOPT.get();

                Tag tag = vehicle.getTag();
                if(tag.getId() == null){
                    em.persist(tag);
                }else{
                    em.merge(tag);
                }

                if(vehicle.getId() == null){
                    vehicle.setTag(tag);
                    em.persist(vehicle);
                }

                Link link = new Link(null, true, vehicle, LocalDate.now());
                link.setUser(user);
                em.persist(link);

                em.merge(user);
                em.flush();
            }

        }catch(Exception e){
            System.err.println(e.getMessage());
        }
    }

    @Transactional
    public void unLinkVehicle (Long userId, Long vehicleId){

        try{
            Optional<User> usrOPT = getUserById(userId);
            if(usrOPT.isPresent()){

                User user = usrOPT.get();

                Link linkToRemove = null;
                for(Link link : user.getLinkedCars()){
                    if(link.getVehicle().getId().equals(vehicleId)){
                        linkToRemove = link;
                        break;
                    }
                }

                if(linkToRemove != null){
                    user.getLinkedCars().remove(linkToRemove);
                    em.remove(linkToRemove);

                    Vehicle vehicle = linkToRemove.getVehicle();
                    em.remove(vehicle);

                    Tag tag = vehicle.getTag();
                    em.remove(tag);

                    em.merge(user);
                    em.flush();
                }else {
                    throw new IllegalArgumentException("Vehículo no encontrado para el usuario dado");
                }
            }else {
                throw new IllegalArgumentException("Usuario no encontrado para el id dado");
            }
        }catch (Exception e){
            System.err.println(e.getMessage());
            throw e;
        }
    }


    //Cuentas.
    @Transactional
    public void linkCreditCard (Long id , CreditCard creditCard){
        Optional<User> usrOPT = getUserById(id);

        if(usrOPT.isPresent()){

            User usr = usrOPT.get();

            TollCustomer tollCustomer = usr.getTollCustomer();
            if (tollCustomer == null) {
                throw new IllegalArgumentException("TollCustomer no encontrado para el usuario dado");
            }

            POSTPay postPay = tollCustomer.getPostPay();
            LocalDate creationDate = LocalDate.now();

            if (postPay == null){ //si no tiene cuenta postPaga le creo una y le agrego la tarjeta.
                em.persist(creditCard);
                Integer accountNumber = POSTPay.generateRandomAccountNumber();
                postPay = new POSTPay(null,accountNumber, creationDate, creditCard);
                em.persist(postPay);
                tollCustomer.setPostPay(postPay);
                em.merge(tollCustomer);

            }else{ //si ya tiene cuenta postpaga, cambio la tarjeta, por ahora maneja una tarjeta sola.
                em.merge(creditCard);
                postPay.setCreditCard(creditCard);
                em.merge(postPay);
                tollCustomer.setPostPay(postPay);
                em.merge(tollCustomer);
            }

            em.flush();
        }else {
            throw new IllegalArgumentException("Usuario no encontrado para el id dado");
        }

    }

    public Optional<List<Account>> getAccountsByTag(Long id) {

        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

        Tag tag = em.find(Tag.class, id);

        CriteriaQuery<Vehicle> vehicleCB = criteriaBuilder.createQuery(Vehicle.class);
        Root<Vehicle> vehicleRoot = vehicleCB.from(Vehicle.class);

        vehicleCB.select(vehicleRoot)
                .where(criteriaBuilder.equal(vehicleRoot.get("tag"), tag));

        Vehicle vehicle = em.createQuery(vehicleCB).getSingleResult();

        CriteriaQuery<Link> linkCB = criteriaBuilder.createQuery(Link.class);
        Root<Link> linkRoot = linkCB.from(Link.class);

        linkCB.select(linkRoot)
                .where(criteriaBuilder.equal(linkRoot.get("vehicle"), vehicle));

        Link link = em.createQuery(linkCB).getSingleResult();

        List<Account> accounts = new ArrayList<Account>();

        if(link.getUser().getTollCustomer().getPrePay() != null) {
            accounts.add(link.getUser().getTollCustomer().getPrePay());
        }

        if(link.getUser().getTollCustomer().getPostPay() != null){
            accounts.add(link.getUser().getTollCustomer().getPostPay());
        }

        return Optional.of(accounts);

    }

    //sin testear
    @Transactional
    public void loadBalance(Long tagId, Double balance) throws Exception {

            Optional<User> user = findByTag(tagId);

            if(user.isEmpty()){
                throw new IllegalArgumentException("Usuario no encontrado para el tag dado");
            }

            TollCustomer customer = user.get().getTollCustomer();

            if(customer == null){
                throw new IllegalArgumentException("TollCustomer no encontrado para el tag dado");
            }

            PREPay prepay = customer.getPrePay();

            if(prepay == null){

                Integer accountNumber = PREPay.generateRandomAccountNumber();
                prepay = (new PREPay(null, accountNumber, LocalDate.now(), balance));
                em.persist(prepay);
            }else{
                prepay.loadBalance(balance);
                em.merge(prepay);
            }

            em.merge(customer);
            em.flush();


    }


    private void firePREPayTollPassEvent(TollPassDTO pass){
        event.fire(new PREPayTollPassEvent(pass));
    }

    @Transactional
    public void prePay(Long tagId, Double balance) throws Exception {
        try{

            CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

            Tag tag = em.find(Tag.class, tagId);

            CriteriaQuery<Vehicle> vehicleCB = criteriaBuilder.createQuery(Vehicle.class);
            Root<Vehicle> vehicleRoot = vehicleCB.from(Vehicle.class);

            CriteriaQuery<Link> linkCB = criteriaBuilder.createQuery(Link.class);

            Root<Link> linkRoot =  linkCB.from(Link.class);

            vehicleCB.select(vehicleRoot)
                    .where(criteriaBuilder.equal(vehicleRoot.get("tag"), tag));

            Vehicle vehicleDB = em.createQuery(vehicleCB).getSingleResult();

            em.clear();

            linkCB.select(linkRoot)
                    .where(criteriaBuilder.equal(linkRoot.get("vehicle"), vehicleDB));

            Link linkDB = null;

            try {
                linkDB = em.createQuery(linkCB).getSingleResult();

            } catch (Exception e){
                System.err.println(e.getMessage());
            }

            User user = linkDB.getUser();

            TollCustomer customer = user.getTollCustomer();

            if(customer == null){
                throw new IllegalArgumentException("Cliente telepeaje no encontrado para el tag dado");
            }

            PREPay prePay = customer.getPrePay();

            if(prePay == null){
                throw new IllegalArgumentException("Cuenta prepaga no encontrada para el tag dado.");
            }

            //se procede al pago
            prePay.pay(balance);
            em.merge(prePay);

            //nueva pasada.

            TollPass newPass = new TollPass(null, LocalDate.now(), balance, PaymentTypeData.PRE_PAYMENT, vehicleDB);
            em.merge(newPass);
//            vehicleDB.addPass(newPass);
//            em.merge(vehicleDB);
//            em.merge(customer);
            em.flush();

            //Aca se envia el evento de una pasada prepaga
            VehicleDTO vehicleDTO = null;
            TagDTO tagDTO = new TagDTO(vehicleDB.getTag().getId(), vehicleDB.getTag().getUniqueId().toString());
            if(vehicleDB instanceof NationalVehicle){

                vehicleDTO = new NationalVehicleDTO(vehicleDB.getId(), null, tagDTO, null);

            }else if( vehicleDB instanceof  ForeignVehicle){
                vehicleDTO = new ForeignVehicleDTO(vehicleDB.getId(), null, tagDTO);
            }
            TollPassDTO newPassDTO = new TollPassDTO(null, newPass.getPassDate().toString(), newPass.getCost(), newPass.getPaymentType(), vehicleDTO);
            firePREPayTollPassEvent(newPassDTO);

        }catch (Exception e) {
            System.err.println("Algo salio mal " +  e.getMessage());
        }

    }

    @Transactional
    public void postPay(Long tagId, Double cost) throws Exception{
        try {

            CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

            Tag tag = em.find(Tag.class, tagId);

            CriteriaQuery<Vehicle> vehicleCB = criteriaBuilder.createQuery(Vehicle.class);
            Root<Vehicle> vehicleRoot = vehicleCB.from(Vehicle.class);

            CriteriaQuery<Link> linkCB = criteriaBuilder.createQuery(Link.class);

            Root<Link> linkRoot = linkCB.from(Link.class);

            vehicleCB.select(vehicleRoot)
                    .where(criteriaBuilder.equal(vehicleRoot.get("tag"), tag));

            Vehicle vehicleDB = em.createQuery(vehicleCB).getSingleResult();

            em.clear();

            linkCB.select(linkRoot)
                    .where(criteriaBuilder.equal(linkRoot.get("vehicle"), vehicleDB));

            Link linkDB = null;

            try {
                linkDB = em.createQuery(linkCB).getSingleResult();

            } catch (Exception e) {
                System.err.println(e.getMessage());
            }


            User user = linkDB.getUser();

            TollCustomer customer = user.getTollCustomer();

            if (customer == null) {
                throw new IllegalArgumentException("TollCustomer no encontrado para el tag dado");
            }

            POSTPay postPay = customer.getPostPay();

            if (postPay == null) {
                throw new IllegalArgumentException("Cuenta postpaga no encontrada para el tag dado.");
            }

            //Armado userDTO
            UserDTO userDTO = null;

            //Armo el customerDTO utilizando una funcion auxiliar.
            TollCustomerDTO customerDTO = getTollCustomerDTO(user);

            //armo la lista de vinculos, armo el auto, sus pasadas, su placa si tiene, etc.
            List<Link> vehicles = user.getLinkedCars();
            List<LinkDTO> linkListDTO = new ArrayList<>();
            LinkDTO linkObject;

            //pasadas
            List<TollPassDTO> listTollPassDTO = new ArrayList<>();
            TollPassDTO tollPassObject;

            //vehiculo
            Vehicle vehicle;
            VehicleDTO vehicleDTO = null;
            LicensePlateDTO licencePlate;
            TagDTO tagDTO;

            //en  este bloque se arma la lista de vinculos.
            for (Link link : vehicles) {
                if (link.getVehicle().getTag().getId().equals(tagId)) {
                    vehicle = link.getVehicle();

                    List<TollPass> listTollPass = vehicle.getTollPass();

                    //agrego nueva pasada al vehiculo, asi le mando datos actualizados al otro modulo.
                    TollPass newPass = new TollPass(null, LocalDate.now(), cost, PaymentTypeData.POST_PAYMENT, vehicleDB);
                    em.merge(newPass);
//                    vehicleDB.addPass(newPass);

                    //en este bloque  se arma la lista de pasadas de un vehiculo
                    for (TollPass tollPass : listTollPass) {
                        tollPassObject = new TollPassDTO(tollPass.getId(), tollPass.getPassDate().toString(), tollPass.getCost(), tollPass.getPaymentType());
                        listTollPassDTO.add(tollPassObject);
                    }

                    //en este bloque se arman los vehiculos
                    if (vehicle instanceof NationalVehicle) {

                        tagDTO = new TagDTO(vehicle.getTag().getId(), vehicle.getTag().getUniqueId().toString());
                        licencePlate = new LicensePlateDTO(((NationalVehicle) vehicle).getPlate().getId(), ((NationalVehicle) vehicle).getPlate().getLicensePlateNumber());
                        vehicleDTO = new NationalVehicleDTO(vehicle.getId(), listTollPassDTO, tagDTO, licencePlate);

                    } else if (vehicle instanceof ForeignVehicle) {

                        tagDTO = new TagDTO(vehicle.getTag().getId(), vehicle.getTag().getUniqueId().toString());
                        vehicleDTO = new ForeignVehicleDTO(vehicle.getId(), listTollPassDTO, tagDTO);
                    }

                    //Se añade el objeto a la lista de vinculos.
                    linkObject = new LinkDTO(link.getId(), link.getInitialDate(), link.getActive(), vehicleDTO);
                    linkListDTO.add(linkObject); //obtengo como resultado final una lista de linkDTO.
                }
            }

            //en este bloque se arman los usuarios.
            if (user instanceof NationalUser) {
                userDTO = new NationalUserDTO(user.getId(), user.getEmail(), user.getPassword(), user.getName(),
                        user.getCi(), customerDTO, linkListDTO, null, null);//Es necesario pasar en el usuario sucive y notificaciones?
            } else if (user instanceof ForeignUser) {
                userDTO = new ForeignUserDTO(user.getId(), user.getEmail(), user.getPassword(), user.getName(),
                        user.getCi(), customerDTO, linkListDTO, null);
            }

            //TODO: si esto lanza excepcion, dicha excepcion le debe llegar al modulo de peaje NO HACER CATCH AQUI
            paymentController.notifyPayment(userDTO, vehicleDTO, cost, customerDTO.getPostPayDTO().getCreditCardDTO());
            em.flush();

        } catch (ExternalApiException e) {
            throw e;

        } catch (Exception e){
            throw new Exception("Algo salió mal: " + e.getMessage(), e);
        }
    }

    /**
     * Función auxiliar encargada de realizar un pasaje de objeto  TollCustomer a TollCustomerDTO.
     *
     * @param usr -> recibe un usr, concramente utilizará el objeto TollCustomer de este.
     * @return -> retorna como resultado el armado del objeto TollCustomerDTO
     */
    private static TollCustomerDTO getTollCustomerDTO(User usr) {
        PREPay prePay;
        PrePayDTO prePayDTO = null;

        POSTPay postPay;
        PostPayDTO postPayDTO = null;

        CreditCard card;
        CreditCardDTO cardDTO;

        //armo cuenta postpay si tiene
        if(usr.getTollCustomer().getPostPay() != null){

            card = usr.getTollCustomer().getPostPay().getCreditCard();

            cardDTO = new CreditCardDTO(card.getId(), card.getCardNumber(), card.getName(), card.getExpireDate().toString());

            postPay = usr.getTollCustomer().getPostPay();
            postPayDTO = new PostPayDTO(postPay.getId(), postPay.getAccountNumber(), postPay.getCreationDate(), cardDTO);

        }

        //armo cuenta prepay si tiene
        if(usr.getTollCustomer().getPrePay() != null){
            prePay = usr.getTollCustomer().getPrePay();
            prePayDTO = new PrePayDTO(prePay.getId(), prePay.getAccountNumber(), prePay.getCreationDate(), prePay.getBalance());
        }

        return new TollCustomerDTO(usr.getTollCustomer().getId(), postPayDTO, prePayDTO);
    }


}
