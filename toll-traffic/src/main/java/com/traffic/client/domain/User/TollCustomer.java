package com.traffic.client.domain.User;

import com.traffic.client.domain.Account.POSTPay;
import com.traffic.client.domain.Account.PREPay;
import com.traffic.dtos.user.TollCustomerDTO;
import lombok.Data;

@Data
public class TollCustomer {

    private Long id;
    private POSTPay postPay;
    private PREPay prePay;


    public TollCustomer(){}

    public TollCustomer(Long id, POSTPay postPay, PREPay prePay) {
        this.id = id;
        this.postPay = postPay;
        this.prePay = prePay;
    }
}
