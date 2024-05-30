package com.traffic.sucive.domain.account;

import com.traffic.sucive.domain.account.Account;
import com.traffic.sucive.domain.account.CreditCard;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
public class PostPay extends Account {

    private CreditCard creditCard;

    public PostPay() {
    }

    public PostPay(Integer accountNumber, LocalDate creationDate, CreditCard creditCard) {
        super(accountNumber, creationDate);
        this.creditCard = creditCard;
    }
}
