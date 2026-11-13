package com.luckyprogrammer.dnd_inventory_backend.mappers;

import org.springframework.stereotype.Component;

import com.luckyprogrammer.dnd_inventory_backend.dtos.request.CreateItemRequest;
import com.luckyprogrammer.dnd_inventory_backend.dtos.request.UpdateItemRequest;
import com.luckyprogrammer.dnd_inventory_backend.dtos.response.ItemResponse;
import com.luckyprogrammer.dnd_inventory_backend.entities.Item;
import com.luckyprogrammer.dnd_inventory_backend.repositories.CategoryRepository;

@Component
public class ItemMapper {

    private final CategoryRepository categoryRepository;

    public ItemMapper(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public ItemResponse toResponse(Item item) {
        return new ItemResponse(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getPrice(),
                item.getWeight(),
                item.getCategory().getName());
    }

    public Item createItem(CreateItemRequest createItemRequest) {
        Item item = new Item();
        item.setName(createItemRequest.name());
        item.setDescription(createItemRequest.description());
        item.setPrice(createItemRequest.price());
        item.setWeight(createItemRequest.weight());
        item.setCategory(categoryRepository.findById(createItemRequest.categoryId())
                .orElseThrow());
        return item;
    }

    public Item updateItem(UpdateItemRequest updateItemRequest, Item existingItem) {
        if (updateItemRequest.name() != null)
            existingItem.setName(updateItemRequest.name());
        if (updateItemRequest.description() != null)
            existingItem.setDescription(updateItemRequest.description());
        if (updateItemRequest.price() != null)
            existingItem.setPrice(updateItemRequest.price());
        if (updateItemRequest.weight() != null)
            existingItem.setWeight(updateItemRequest.weight());
        if (updateItemRequest.categoryId() != null)
            existingItem.setCategory(categoryRepository.findById(updateItemRequest.categoryId()).orElseThrow());
        return existingItem;
    }
}
