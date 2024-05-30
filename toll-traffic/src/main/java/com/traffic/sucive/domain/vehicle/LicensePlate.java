package com.traffic.sucive.domain.vehicle;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LicensePlate extends Identifier {

    private Long id;
    private String licensePlateNumber;

    @Override
    public boolean equals(Object obj){

        LicensePlate licensePlate = (LicensePlate) obj;

        if(this.id == licensePlate.getId()){
            return true;
        }else {
            return false;
        }
    }
}
