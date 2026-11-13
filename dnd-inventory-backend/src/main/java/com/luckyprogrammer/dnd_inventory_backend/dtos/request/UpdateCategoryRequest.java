package com.luckyprogrammer.dnd_inventory_backend.dtos.request;

public record UpdateCategoryRequest(
        String name,
        Long itemClassId
) {

}
