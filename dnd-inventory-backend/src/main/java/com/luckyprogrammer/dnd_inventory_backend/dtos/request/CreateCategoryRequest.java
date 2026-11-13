package com.luckyprogrammer.dnd_inventory_backend.dtos.request;

public record CreateCategoryRequest(
        String name,
        Long itemClassId
) {

}
