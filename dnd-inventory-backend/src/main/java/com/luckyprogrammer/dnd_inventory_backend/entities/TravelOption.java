package com.luckyprogrammer.dnd_inventory_backend.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "travel_options")
@PrimaryKeyJoinColumn(name = "item_id")
@Getter
@Setter
@NoArgsConstructor
public class TravelOption extends Item {

    @Column(length = 50)
    private String speed;

    @Column(name = "carrying_capacity")
    private Integer carryingCapacity;
}
