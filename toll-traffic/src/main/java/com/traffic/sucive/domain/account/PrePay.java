package com.traffic.sucive.domain.account;


import com.traffic.sucive.domain.account.Account;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
public class PrePay extends Account {

    private Double balance;

    public PrePay() {
    }

    public PrePay(Integer accountNumber, LocalDate creationDate, Double balance) {
        super(accountNumber, creationDate);
        this.balance = balance;
    }
}
