package com.goquizit.DTO.outputDTO;

import com.goquizit.model.Player;

import java.util.UUID;

public class PlayerAnswerOutputDTO {

    private UUID id;

    private String value;

    private Player player;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
