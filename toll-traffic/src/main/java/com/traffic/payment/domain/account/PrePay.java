package com.traffic.payment.domain.account;

import com.traffic.client.domain.Account.Account;
import com.traffic.dtos.account.AccountDTO;
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
