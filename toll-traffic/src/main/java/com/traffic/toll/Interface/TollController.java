package com.traffic.toll.Interface;

import java.util.Optional;
import com.traffic.dtos.vehicle.IdentifierDTO;
import com.traffic.exceptions.InvalidVehicleException;
import com.traffic.exceptions.PersistenceErrorException;

/**
 * <p>
 *     Controlador que expone los metodos del modulo Peaje
 * </p>
 * <p>
 *     Se encarga de gestionar los precios de las tarifas asi como
 *     de verificar si los vehiculos son aptos para pasar por peaje.
 * </p>
 */
public interface TollController {

    /** Verifica si un vehiculo esta habilitado para pasar por peaje
     * @param identifier identificador del vehiculo puede ser de tipo {@link com.traffic.dtos.vehicle.TagDTO}
     *                   o {@link com.traffic.dtos.vehicle.LicensePlateDTO}
     * @return devolvera un <code>Optional</code> con un <code>Boolean</code> el cual estara en <strong>true</strong>
     *                   si el vehiculo esta habilitado para pasar, caso contrario sera <strong>false</strong>
     * @throws IllegalArgumentException si idenitifier es <code>null</code>
     * @throws InvalidVehicleException si el vehiculo no existe
     * @throws PersistenceErrorException si hubo algun error a la hora de persisitir los datos
     */
    public Optional<Boolean> isEnabled(IdentifierDTO identifier) throws IllegalArgumentException,
            InvalidVehicleException, PersistenceErrorException;

    /**
     * Actualiza el valor de la tarifa comun
     * @param amount nuevo precio para la tarifa
     * @throws IllegalArgumentException
     */
    public void updateCommonTariff(Double amount) throws IllegalArgumentException;

    /**
     * Actualiza el valor de la tarifa preferencial
     * @param amount nuevo precio para la tarifa preferencial
     * @throws IllegalArgumentException
     */
    public void updatePreferentialTariff(Double amount) throws IllegalArgumentException;
}
