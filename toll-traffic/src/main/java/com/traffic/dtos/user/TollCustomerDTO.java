package com.traffic.dtos.user;

import com.traffic.dtos.account.PostPayDTO;
import com.traffic.dtos.account.PrePayDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TollCustomerDTO {

    private Long id;
    private PostPayDTO postPayDTO;
    private PrePayDTO  prePayDTO;

}
