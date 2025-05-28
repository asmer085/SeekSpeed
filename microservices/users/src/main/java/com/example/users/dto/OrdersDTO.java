package com.example.users.dto;

import java.util.UUID;

public class OrdersDTO {

    private UUID equipmentId;
    private UUID userId;


    public UUID getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(UUID equipmentId) {
        this.equipmentId = equipmentId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
}
