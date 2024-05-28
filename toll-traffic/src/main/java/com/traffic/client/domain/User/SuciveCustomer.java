package com.traffic.client.domain.User;

import com.traffic.client.domain.tariff.CommonTariff;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "ClientModule_SuciveCustomer")
public class SuciveCustomer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "tariff_id")
    private CommonTariff tariff;

    public SuciveCustomer(Long id, CommonTariff tariff) {
        this.id = id;
        this.tariff = tariff;
    }


}
