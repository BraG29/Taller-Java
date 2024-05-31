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
import com.traffic.exceptions.NoAccountException;
import com.traffic.payment.Interface.PaymentController;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@ApplicationScoped
public class ClientModuleRepositoryImpl implements ClientModuleRepository{

    @Inject
    private PaymentController paymentController;

    @PersistenceContext
    private EntityManager em;

    List<User> usersList;

    public void update(User  usr){
        //comprobar actualizar lista de usersList
        for(int i = 0; i < this.usersList.size(); i++){
            if(usr.getId().equals(this.usersList.get(i).getId())){
                this.usersList.set(i, usr);
            }
        }
    }



    @Override
    public Optional<User> findByTag(Tag tag) {

        for (User user : this.usersList){
            if (user.getLinkedCars() != null){
                for(Link link : user.getLinkedCars()){
                    if(link.getVehicle().getTag().equals(tag)){
                        return Optional.of(user);
                    }
                }
            }
        }

        return Optional.empty();
    }

    @Override
    public Optional<List<User>> listUsers() {

        if(this.usersList != null){

            return Optional.of(new ArrayList<>(this.usersList));
        }

        return Optional.empty();
    }

    @Transactional
    public void createUser(User user){
        em.persist(user);
        em.flush();
    }

    @Override
    public Optional<User> getUserById(Long id) {

        for (User user : this.usersList){
            if(Objects.equals(user.getId(), id)){//vamo a probar solucion intellij
                return Optional.of(user);
            }
        }

        return Optional.empty();
    }

    @Override
    public Optional<Vehicle> getVehicleByTag(Tag tag) {
        List<Link> links = new ArrayList<>();

        for(User users : this.usersList){

            links = users.getLinkedCars();

            for (Link link : links){
                if(link.getVehicle().getTag().equals(tag)){
                    Vehicle vehicle = link.getVehicle();
                    return Optional.of(vehicle);
                }
            }

        }

        return Optional.empty();
    }


//    @Transactional
    public Optional<List<Account>> getAccountsByTag(Long id) {

        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

        Tag tag = em.find(Tag.class, id);

        CriteriaQuery<Vehicle> vehicleCB = criteriaBuilder.createQuery(Vehicle.class);
        Root<Vehicle> vehicleRoot = vehicleCB.from(Vehicle.class);

        CriteriaQuery<Link> linkCB = criteriaBuilder.createQuery(Link.class);
        Root<Link> linkRoot = vehicleCB.from(Link.class);

        vehicleCB.select(vehicleRoot)
                .where(criteriaBuilder.equal(vehicleRoot.get("tag"), tag));

        Vehicle vehicle = em.createQuery(vehicleCB).getSingleResult();

        em.clear();

        linkCB.select(linkRoot)
                .where(criteriaBuilder.equal(linkRoot.get("vehicle"), vehicle));


        Link link = null;

        try {
            link = em.createQuery(linkCB).getSingleResult();

        } catch (Exception e){
            System.err.println(e.getMessage());
        }

        List<Account> accounts = new ArrayList<Account>();

        if(link.getUser().getTollCustomer().getPrePay() != null) {
            accounts.add(link.getUser().getTollCustomer().getPrePay());
        }

        if(link.getUser().getTollCustomer().getPostPay() != null){
            accounts.add(link.getUser().getTollCustomer().getPostPay());
        }

        return Optional.of(accounts);


        //Sacar Tag -> entidad
        //Con Tag sacas Vehiculo
        //Con el Vehiculo sacas Link

        //Link-> User -> TollCustomer -> Accounts

//        CriteriaQuery<Account> query = criteriaBuilder.createQuery(Account.class);
//
//        //raiz de la consulta:
//        Root<User> userRoot = query.from(User.class);
//
//        //imagina esto como una combinacion de todas las tablas.
//        //cadena de joins para encontrar el usuario cuyo vehiculo coincida con el tag.
//        //usuario -> vinculo, vinculo -> vehiculo, vehiculo -> tag.
//        Join<User, Link> userLinkJoin = userRoot.join("linkedCars");
//        Join<Link, Vehicle> linkVehicleJoin = userLinkJoin.join("vehicle");
//        Join<Vehicle, Tag> vehicleTagJoin = linkVehicleJoin.join("tag");
//        //seleccion de cuentas, hago join entre usuario y cliente peaje.
//        Join<User, TollCustomer> userTollCustomerJoin = userRoot.join("tollCustomer");
//
//        //Lista de predicados
//        List<Predicate> predicates = new ArrayList<>();
//        predicates.add(criteriaBuilder.equal(vehicleTagJoin.get("tagId"), id));
//
//        Join<TollCustomer, PREPay> tollCustomerPREPayJoin = userTollCustomerJoin.join("PREPay", JoinType.LEFT);
//        predicates.add(criteriaBuilder.isNotNull(tollCustomerPREPayJoin.get("id")));
//
//        Join<TollCustomer, POSTPay> tollCustomerPOSTPayJoin = userTollCustomerJoin.join("POSTPay", JoinType.LEFT);
//        predicates.add(criteriaBuilder.isNotNull(tollCustomerPOSTPayJoin.get("id")));
//
//        //filtro por tag.
//        query.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
//
//        List<Account> accounts = em.createQuery(query).getResultList();

//        return accounts.isEmpty() ? Optional.empty() : Optional.of(accounts);
    }

    @Transactional
    public void loadBalance(Long tagId, Double balance) throws Exception {
        try{
            CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
            CriteriaQuery<User> query = criteriaBuilder.createQuery(User.class);

            //raiz de la consulta:
            Root<User> userRoot = query.from(User.class);

            System.out.println("Carga de saldo.");

            Join<User, Link> userLinkJoin = userRoot.join("linkedCars");
            Join<Link, Vehicle> linkVehicleJoin = userLinkJoin.join("vehicle");
            Join<Vehicle, Tag> vehicleTagJoin = linkVehicleJoin.join("tag");

            query.select(userRoot).where(criteriaBuilder.equal(vehicleTagJoin.get("tagId"), tagId));

            List<User> users = em.createQuery(query).getResultList();

            if(users.isEmpty()){
                throw new IllegalArgumentException("Usuario no encontrado para el tag dado");
            }

            TollCustomer customer = users.get(0).getTollCustomer();

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

        } catch (Exception e){

            throw new Exception("Algo salio mal " + e.getMessage());
        }
    }

    @Transactional
    public void prePay(Long tagId, Double balance) throws Exception {
        try{
            CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
            CriteriaQuery<User> query = criteriaBuilder.createQuery(User.class);

            //raiz de la consulta:
            Root<User> userRoot = query.from(User.class);

            System.out.println("Busco usuario por tag");

            Join<User, Link> userLinkJoin = userRoot.join("linkedCars");
            Join<Link, Vehicle> linkVehicleJoin = userLinkJoin.join("vehicle");
            Join<Vehicle, Tag> vehicleTagJoin = linkVehicleJoin.join("tag");

            query.select(userRoot).where(criteriaBuilder.equal(vehicleTagJoin.get("tagId"), tagId));

            List<User> users = em.createQuery(query).getResultList();

            if(users.isEmpty()){
                throw new IllegalArgumentException("Usuario no encontrado para el tag dado");
            }

            User user = users.get(0);

            TollCustomer customer = user.getTollCustomer();

            if(customer == null){
                throw new IllegalArgumentException("TollCustomer no encontrado para el tag dado");
            }

            PREPay prePay = customer.getPrePay();

            if(prePay == null){
                throw new IllegalArgumentException("Cuenta prepaga no encontrada para el tag dado.");
            }
            UserDTO userDTO = null;

            //si no tiene saldo
            if(prePay.getBalance() < balance){
                //lanzo notificacion armo UserDTO con cosas basicas.
                /*
                if(user instanceof NationalUser){
                    userDTO = new NationalUserDTO(user.getId(), user.getEmail(), user.getPassword(),
                            user.getName(), user.getCi(), null, null, null,
                            null);

                }else if (user instanceof ForeignUser){
                    userDTO = new ForeignUserDTO(user.getId(), user.getEmail(), user.getPassword(),
                            user.getName(), user.getCi(), null, null, null);
                }*/

                //TODO evento pago rechzadao
                // communicationController.notifyNotEnoughBalance(userDTO);

                throw new IllegalArgumentException("Saldo insuficiente.");
            }

            //se procede al pago
            prePay.pay(balance);
            em.merge(prePay);

            //nueva pasada.
            TollPass newPass = new TollPass(null, LocalDate.now(), balance, PaymentTypeData.PRE_PAYMENT);
            for(Link link : user.getLinkedCars()){
                Vehicle vehicle = link.getVehicle();
                if(vehicle.getTag().getId().equals(tagId)){
                    vehicle.addPass(newPass);
                    em.merge(vehicle);
                }
            }

            em.merge(customer);
            em.flush();

        }catch (Exception e) {
            throw new Exception("Algo salió mal: " + e.getMessage(), e);
        }

    }

    @Transactional
    public void postPay(Long tagId, Double cost) throws Exception{
        try{
            CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
            CriteriaQuery<User> query = criteriaBuilder.createQuery(User.class);

            //raiz de la consulta:
            Root<User> userRoot = query.from(User.class);

            System.out.println("Busco usuario por tag");

            Join<User, Link> userLinkJoin = userRoot.join("linkedCars");
            Join<Link, Vehicle> linkVehicleJoin = userLinkJoin.join("vehicle");
            Join<Vehicle, Tag> vehicleTagJoin = linkVehicleJoin.join("tag");

            query.select(userRoot).where(criteriaBuilder.equal(vehicleTagJoin.get("tagId"), tagId));

            List<User> users = em.createQuery(query).getResultList();

            if(users.isEmpty()){
                throw new IllegalArgumentException("Usuario no encontrado para el tag dado");
            }

            User user = users.get(0);

            TollCustomer customer = user.getTollCustomer();

            if(customer == null){
                throw new IllegalArgumentException("TollCustomer no encontrado para el tag dado");
            }

            POSTPay postPay = customer.getPostPay();

            if(postPay == null){
                throw new IllegalArgumentException("Cuenta postpaga no encontrada para el tag dado.");
            }

            UserDTO userDTO = null;

            //Armo el customerDTO utilizando una funcion auxiliar.
            TollCustomerDTO customerDTO = getTollCustomerDTO(user);

            //armo la lista de vinculos, armo el auto, sus pasadas, su placa si tiene, etc.
            List<Link> vehicles = user.getLinkedCars();
            List<LinkDTO> linkListDTO = new ArrayList<>();
            LinkDTO linkObject;

            List<TollPass> listTollPass;
            List<TollPassDTO> listTollPassDTO = new ArrayList<>();
            TollPassDTO tollPassObject;

            Vehicle vehicle;
            VehicleDTO vehicleDTO = null;
            LicensePlateDTO licencePlate;
            TagDTO tagDTO;

            //en  este bloque se arma la lista de vinculos.
            for (Link link : vehicles) {
                if (link.getVehicle().getTag().getId().equals(tagId)) {

                    vehicle = link.getVehicle();

                    //agrego nueva pasada al vehiculo, asi le mando datos actualizados al otro modulo.
                    TollPass newPass = new TollPass(null,LocalDate.now(), cost, PaymentTypeData.POST_PAYMENT);
                    vehicle.addPass(newPass);
                    em.merge(vehicle);
                    listTollPass = vehicle.getTollPass();

                    //TODO puede explotar al hacer getId y que sea null.

                    //en este bloque  se arma la lista de pasadas de un vehiculo
                    for (TollPass tollPass : listTollPass){
                        tollPassObject = new TollPassDTO(tollPass.getId(),tollPass.getPassDate(), tollPass.getCost(), tollPass.getPaymentType());
                        listTollPassDTO.add(tollPassObject);
                    }

                    //en este bloque se arman los vehiculos
                    if(vehicle instanceof NationalVehicle){


                        tagDTO = new TagDTO(vehicle.getTag().getId(), vehicle.getTag().getUniqueId().toString());
                        licencePlate = new LicensePlateDTO(((NationalVehicle) vehicle).getPlate().getId() ,((NationalVehicle) vehicle).getPlate().getLicensePlateNumber());
                        vehicleDTO = new NationalVehicleDTO(vehicle.getId(), listTollPassDTO, tagDTO, licencePlate);

                    } else if (vehicle instanceof  ForeignVehicle){
                        tagDTO = new TagDTO(vehicle.getTag().getId(),vehicle.getTag().getUniqueId().toString());
                        vehicleDTO = new ForeignVehicleDTO(vehicle.getId(), listTollPassDTO, tagDTO);
                    }

                    //Se añade el objeto a la lista de vinculos.
                    linkObject = new LinkDTO(link.getId(), link.getInitialDate(), link.getActive(), vehicleDTO);
                    linkListDTO.add(linkObject); //obtengo como resultado final una lista de linkDTO.
                }
            }

            //en este bloque se arman los usuarios.
            if(user instanceof NationalUser){
                userDTO = new NationalUserDTO(user.getId(), user.getEmail(), user.getPassword(), user.getName(),
                        user.getCi(), customerDTO, linkListDTO, null, null);//Es necesario pasar en el usuario sucive y notificaciones?
            } else if(user instanceof  ForeignUser){
                userDTO = new ForeignUserDTO(user.getId(), user.getEmail(), user.getPassword(), user.getName(),
                        user.getCi(), customerDTO, linkListDTO, null);
            }

            paymentController.notifyPayment(userDTO, vehicleDTO, cost, customerDTO.getPostPayDTO().getCreditCardDTO());

            em.flush();

        }catch (Exception e){
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
            cardDTO = new CreditCardDTO(card.getId(), card.getCardNumber(), card.getName(), card.getExpireDate());

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
