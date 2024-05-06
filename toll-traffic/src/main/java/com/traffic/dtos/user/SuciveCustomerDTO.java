package com.traffic.dtos.user;

import com.traffic.dtos.tariff.CommonTariffDTO;
import lombok.Data;

@Data
public class SuciveCustomerDTO {

    private CommonTariffDTO commonTariff;

    public SuciveCustomerDTO() {
    }

    public SuciveCustomerDTO(CommonTariffDTO commonTariff) {
        this.commonTariff = commonTariff;
    }
}
