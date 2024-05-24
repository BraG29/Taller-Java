package com.traffic.client.domain.Vehicle;

import lombok.Data;

@Data
public class Tag {

    private Long tagId;

    public Tag(){
    }

    public Tag(Long tag){
        this.tagId = tag;
    }

    @Override
    public String toString() {
        return "Tag [" + tagId + "]";
    }
}
