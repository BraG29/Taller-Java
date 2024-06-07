package com.traffic.events;

import com.traffic.dtos.vehicle.TollPassDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PREPayTollPassEvent extends CustomEvent {

    TollPassDTO tollPass;

}
