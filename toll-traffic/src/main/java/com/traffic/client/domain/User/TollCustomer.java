package com.traffic.client.domain.User;

import com.traffic.client.domain.Account.POSTPay;
import com.traffic.client.domain.Account.PREPay;
import com.traffic.dtos.user.TollCustomerDTO;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity(name = "ClientModule_TollCustomer")
public class TollCustomer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "POSTPay_id")
    private POSTPay postPay;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "PREPay_id")
    private PREPay prePay;


    public TollCustomer(){}

    public TollCustomer(Long id, POSTPay postPay, PREPay prePay) {
        this.id = id;
        this.postPay = postPay;
        this.prePay = prePay;
    }

    @Override
    public String toString() {
        return "Cuenta/s del cliente [ Id:" + id + ", postPaga: " + postPay.toString() + ", prePaga: " + prePay.toString();
    }
}
