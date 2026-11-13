package com.luckyprogrammer.dnd_inventory_backend.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "weapons", indexes = {
        @Index(name = "idx_weapons_damage_type_id", columnList = "damage_type_id")
})
@PrimaryKeyJoinColumn(name = "item_id")
@Getter
@Setter
@NoArgsConstructor
public class Weapon extends Item{

    @Column(nullable = false,name = "damage_dice", length = 20)
    private String damageDice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "damage_type_id")
    private DamageType damageType;

    @Column(columnDefinition = "text")
    private String properties;
}
