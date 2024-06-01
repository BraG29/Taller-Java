package com.traffic.sucive.domain.entities;
import com.traffic.dtos.user.TollCustomerDTO;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity(name = "Sucive_Toll_Customer")
public class TollCustomer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @OneToOne
//    @JoinColumn(name = "POSTPay_id")
//    private POSTPay postPay;
//
//    @OneToOne
//    @JoinColumn(name = "PREPay_id")
//    private PREPay prePay;


    public TollCustomer(){}
//    public TollCustomer(Long id, POSTPay postPay, PREPay prePay) {
//        this.id = id;
//        this.postPay = postPay;
//        this.prePay = prePay;
//    }
//    @Override
//    public String toString() {
//        return "Cuenta/s del cliente [ Id:" + id + ", postPaga: " + postPay.toString() + ", prePaga: " + prePay.toString();
//    }
}
