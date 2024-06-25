package com.traffic.events;

import com.traffic.dtos.vehicle.TollPassDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
/**
 * Este evento lanza una nueva pasada cuando se paga con el metodo pre pago.
 * También sirve para registra un pago con metodo pre pago en el modulo de monitoreo.
 */
public class PREPayTollPassEvent extends CustomEvent {

    TollPassDTO tollPass;

    public PREPayTollPassEvent(String description, TollPassDTO tollPass) {
        super(description);
        this.tollPass = tollPass;
    }

    public PREPayTollPassEvent(TollPassDTO tollPass) {
        this.tollPass = tollPass;
    }
}
