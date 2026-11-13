package com.luckyprogrammer.dnd_inventory_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.luckyprogrammer.dnd_inventory_backend.entities.ItemClass;

public interface ItemClassRepository extends JpaRepository<ItemClass, Long> {
     
    

}
