package com.example.users.dtos;

import com.example.users.entity.Type;
import com.example.users.entity.Users;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.UUID;

public class StatisticsDTO {

    @PositiveOrZero(message = "Average pace must be positive or zero")
    @Column()
    private Double averagePace;

    @PositiveOrZero(message = "Best pace must be positive or zero")
    @Column()
    private Double bestPace;

    @PositiveOrZero(message = "Total time must be positive or zero")
    @Column()
    private Double totalTime;

    private UUID userId;
    private UUID typeId;

    /*@ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "type_id")
    private Type type;*/

    public Double getAveragePace() { return averagePace; }

    public void setAveragePace(Double averagePace) { this.averagePace = averagePace; }

    public Double getBestPace() { return bestPace; }

    public void setBestPace(Double bestPace) { this.bestPace = bestPace; }

    public Double getTotalTime() { return totalTime; }

    public void setTotalTime(Double totalTime) { this.totalTime = totalTime; }

    public UUID getUserId() { return userId; }

    public void setUserId(UUID userId) { this.userId = userId; }

    public UUID getTypeId() { return typeId; }

    public void setTypeId(UUID typeId) { this.typeId = typeId; }
}
