package com.traffic.suciveservicemock.interfaces;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LicensePlateDTO {

    /*
    Formato del JSON
    {
        "licensePlateNumber" : "ABC123"
    }
    */

    private String licensePlateNumber;
}
