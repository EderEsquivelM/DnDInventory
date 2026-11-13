package com.luckyprogrammer.dnd_inventory_backend.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "armors")
@PrimaryKeyJoinColumn(name = "item_id")
@Getter
@Setter
@NoArgsConstructor
public class Armor extends Item {

    @Column(nullable = false, name = "armor_class")
    private Integer armorClass;

    @Column(name = "strength_requirement")    
    private Integer strengthRequirement;

    @Column(nullable = false, name = "stealth_disadvantage")
    private boolean stealhDisadvantage;
}
