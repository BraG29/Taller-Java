package com.traffic.sucive.domain.user;

import com.traffic.sucive.domain.account.PrePay;
import com.traffic.sucive.domain.account.PostPay;
import lombok.Data;
@Data
public class TollCustomer {

    private PostPay postPay;
    private PrePay prePay;



    public TollCustomer() {
    }

    public TollCustomer(PostPay postPay, PrePay prePay) {
        this.postPay = postPay;
        this.prePay = prePay;
    }
}
