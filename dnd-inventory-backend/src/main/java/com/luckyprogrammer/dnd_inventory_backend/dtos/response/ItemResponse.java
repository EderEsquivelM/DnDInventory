package com.luckyprogrammer.dnd_inventory_backend.dtos.response;

public record ItemResponse(
        Long id,
        String name,
        String description,
        Long price,
        Integer weight,
        String categoryName) {

}
