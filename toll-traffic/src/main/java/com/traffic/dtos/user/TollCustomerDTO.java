package com.traffic.dtos.user;

import com.traffic.dtos.account.PostPayDTO;
import com.traffic.dtos.account.PrePayDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TollCustomerDTO {

    private PostPayDTO postPayDTO;
    private PrePayDTO  prePayDTO;

}
