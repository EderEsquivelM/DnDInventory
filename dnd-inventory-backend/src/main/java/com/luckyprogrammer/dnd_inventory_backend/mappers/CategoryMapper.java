package com.luckyprogrammer.dnd_inventory_backend.mappers;

import org.springframework.stereotype.Component;

import com.luckyprogrammer.dnd_inventory_backend.dtos.request.CreateCategoryRequest;
import com.luckyprogrammer.dnd_inventory_backend.dtos.response.CategoryResponse;
import com.luckyprogrammer.dnd_inventory_backend.entities.Category;
import com.luckyprogrammer.dnd_inventory_backend.repositories.ItemClassRepository;

@Component
public class CategoryMapper {

    private final ItemClassRepository itemRepository;

    public CategoryMapper(ItemClassRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public CategoryResponse toResponse(Category category) {
        if (category == null) {
            return null;
        }
        return new CategoryResponse(category.getId(),
                category.getName(),
                itemRepository.findById(category.getItemClass().getId())
                        .map(itemClass -> itemClass.getName())
                        .orElse(null));
    }

    public Category createCategory(CreateCategoryRequest createCategoryRequest) {
        Category category = new Category();
        category.setName(createCategoryRequest.name());
        category.setItemClass(itemRepository.findById(createCategoryRequest.itemClassId())
                .orElseThrow());
        return category;
    }

    public Category updateCategory(Category category, String name, Long itemClassId) {
        if (name != null)
            category.setName(name);
        if (itemClassId != null)
            category.setItemClass(itemRepository.findById(itemClassId)
                    .orElseThrow());

        return category;
    }

}
