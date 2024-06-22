package com.traffic.events;

import com.traffic.dtos.vehicle.TollPassDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
/**
 * Este evento lanza una nueva pasada cuando se paga con el metodo pre pago.
 * También sirve para registra un pago con metodo pre pago en el modulo de monitoreo.
 */
public class PREPayTollPassEvent extends CustomEvent {

    TollPassDTO tollPass;

}
