package com.traffic.sucive.domain.entities;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "Sucive_Sucive_Customer")
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