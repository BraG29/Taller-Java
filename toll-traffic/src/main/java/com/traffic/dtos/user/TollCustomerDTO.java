package com.traffic.dtos.user;

import com.traffic.dtos.account.PostPayDTO;
import com.traffic.dtos.account.PrePayDTO;
import lombok.Data;

@Data
public class TollCustomerDTO {

    private PostPayDTO postPayDTO;
    private PrePayDTO  prePayDTO;

    public TollCustomerDTO() {
    }

    public TollCustomerDTO(PostPayDTO postPayDTO, PrePayDTO prePayDTO) {
        this.postPayDTO = postPayDTO;
        this.prePayDTO = prePayDTO;
    }
}
