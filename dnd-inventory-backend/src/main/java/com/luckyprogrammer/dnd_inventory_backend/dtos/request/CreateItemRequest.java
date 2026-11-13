package com.luckyprogrammer.dnd_inventory_backend.dtos.request;

public record CreateItemRequest(
        String name,
        String description,
        Long price,
        Integer weight,
        Long categoryId) {

}
