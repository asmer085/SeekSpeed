package com.example.users.dto;

import java.io.Serializable;
import java.util.UUID;

public class UserTypeDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String distance;
    private String results;
    private UUID typeId;
    private Double price;

    UserTypeDTO() {}

    public UserTypeDTO(String distance,
                       String results,
                       Double price,
                       UUID typeId) {
        this.distance = distance;
        this.results = results;
        this.price = price;
        this.typeId = typeId;
    }

    public String getDistance() { return distance; }

    public void setDistance(String distance) { this.distance = distance; }

    public String getResults() { return results; }

    public void setResults(String results) { this.results = results; }

    public Double getPrice() { return price; }

    public void setPrice(Double price) { this.price = price; }

    public UUID getTypeId() { return typeId; }

    public void setTypeId(UUID typeId) { this.typeId = typeId; }

    @Override
    public String toString() {
        return "UserTypeDTO{" +
                "distance='" + distance + '\'' +
                ", results='" + results + '\'' +
                ", price='" + price + '\'' +
                ", typeId='" + typeId + '\'' +
                '}';
    }
}
