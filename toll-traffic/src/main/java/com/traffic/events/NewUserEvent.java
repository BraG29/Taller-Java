package com.traffic.events;

import com.traffic.dtos.user.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NewUserEvent extends CustomEvent {

    private UserDTO user;

    public NewUserEvent(String description, UserDTO user){
        super(description);
        this.user = user;
    }

}
