package com.goquizit.DTO;

import java.util.List;
import java.util.UUID;

public class CreateUpdatePlayerAnswerDTO {

    private List<UUID> id;

    public List<UUID> getId() {
        return id;
    }

    public void setId(List<UUID> id) {
        this.id = id;
    }
}
