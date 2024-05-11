package com.traffic.dtos.user;

import com.traffic.dtos.tariff.CommonTariffDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SuciveCustomerDTO {

    private Long id;
    private CommonTariffDTO commonTariff;


}
