package com.luckyprogrammer.dnd_inventory_backend.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.luckyprogrammer.dnd_inventory_backend.entities.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {

        Optional<Item> findById(Long id);

        boolean existsByName(String name);

        @Query("SELECT i FROM Item i WHERE " +
                        "(:enable IS NULL OR i.enable = :enable) AND " +
                        "(:name IS NULL OR i.name LIKE %:name%) AND " +
                        "(:minPrice IS NULL OR i.price >= :minPrice) AND " +
                        "(:maxPrice IS NULL OR i.price <= :maxPrice) AND " +
                        "(:weight IS NULL OR i.weight = :weight) AND " +
                        "(:categoryId IS NULL OR i.category.id = :categoryId)")
        Page<Item> searchItems(
                        @Param("enable") Boolean enable,
                        @Param("name") String name,
                        @Param("minPrice") Long minPrice,
                        @Param("maxPrice") Long maxPrice,
                        @Param("weight") Integer weight,
                        @Param("categoryId") Long categoryId,
                        Pageable pageable);

}
