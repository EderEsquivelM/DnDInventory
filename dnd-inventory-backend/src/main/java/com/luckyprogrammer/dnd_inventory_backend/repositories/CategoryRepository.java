package com.luckyprogrammer.dnd_inventory_backend.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.luckyprogrammer.dnd_inventory_backend.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findById(Long id);

    boolean existsByName(String name);

    @Query("SELECT c FROM Category c WHERE " +
            "(:name IS NULL OR c.name LIKE %:name%) AND " +
            "(:enable IS NULL OR c.enable = :enable) AND" +
            "(:itemClassId IS NULL OR c.itemClass.id = :itemClassId)")
    Page<Category> searchCategories(@Param("name") String name,
            @Param("enable") Boolean enable,
            @Param("itemClassId") Long itemClassId,
            Pageable pageable);

    @Query("SELECT c FROM Category c WHERE c.enable = true AND" +
            "(:itemClassId IS NULL OR c.itemClass.id = :itemClassId)")
    Page<Category> findByItemClassId(@Param("itemClassId") Long itemClassId, Pageable pageable);
}
