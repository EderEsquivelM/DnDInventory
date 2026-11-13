package com.luckyprogrammer.dnd_inventory_backend.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.luckyprogrammer.dnd_inventory_backend.dtos.request.CreateItemRequest;
import com.luckyprogrammer.dnd_inventory_backend.dtos.request.UpdateItemRequest;
import com.luckyprogrammer.dnd_inventory_backend.dtos.response.ItemResponse;
import com.luckyprogrammer.dnd_inventory_backend.entities.Item;
import com.luckyprogrammer.dnd_inventory_backend.entities.ItemClass;
import com.luckyprogrammer.dnd_inventory_backend.mappers.ItemMapper;
import com.luckyprogrammer.dnd_inventory_backend.repositories.ItemClassRepository;
import com.luckyprogrammer.dnd_inventory_backend.repositories.ItemRepository;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    private final ItemMapper itemMapper;

    private final ItemClassRepository itemClassRepository;

    public ItemService(ItemRepository itemRepository, ItemMapper itemMapper, ItemClassRepository itemClassRepository) {
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
        this.itemClassRepository = itemClassRepository;
    }

    @Transactional(readOnly = true)
    public Optional<ItemResponse> getItemById(Long id) {
        Optional<Item> itemOptional = itemRepository.findById(id);

        if (itemOptional.isPresent())
            return Optional.of(itemMapper.toResponse(itemOptional.orElseThrow()));

        return Optional.empty();
    }

    @Transactional(readOnly = true)
    public Page<ItemResponse> searchPublicItems(
            String name,
            Long minPrice,
            Long maxPrice,
            Integer weight,
            Long categoryId,
            int page,
            int size,
            String sortBy,
            String sortDir) {

        Sort.Direction direction = sortDir.equalsIgnoreCase("desc")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<Item> items = itemRepository.searchItems(true, name, minPrice, maxPrice, weight, categoryId, pageable);

        if (!items.isEmpty())
            return items.map(item -> itemMapper.toResponse(item));

        return Page.empty();
    }

    @Transactional(readOnly = true)
    public Page<ItemResponse> searchAdminItems(
            Boolean enable,
            String name,
            Long minPrice,
            Long maxPrice,
            Integer weight,
            Long categoryId,
            int page,
            int size,
            String sortBy,
            String sortDir) {

        Sort.Direction direction = sortDir.equalsIgnoreCase("desc")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<Item> items = itemRepository.searchItems(enable, name, minPrice, maxPrice, weight, categoryId, pageable);

        if (!items.isEmpty())
            return items.map(item -> itemMapper.toResponse(item));

        return Page.empty();
    }

    @Transactional
    public ItemResponse createItem(CreateItemRequest item) {
        if (itemRepository.existsByName(item.name())) {
            throw new IllegalArgumentException("Item con el nombre " + item.name() + " esta en uso.");
        }

        return itemMapper.toResponse(
                itemRepository.save(itemMapper.createItem(item)));
    }

    @Transactional
    public Optional<ItemResponse> updateItem(Long id, UpdateItemRequest item) {
        if (itemRepository.existsByName(item.name())) {
            throw new IllegalArgumentException("Item con el nombre " + item.name() + " esta en uso.");
        }

        Optional<Item> itemOptional = itemRepository.findById(id);

        if (itemOptional.isPresent())
            return Optional.of(itemMapper.toResponse(
                    itemRepository.save(itemMapper.updateItem(
                            item, itemOptional.orElseThrow()))));

        return Optional.empty();
    }

    @Transactional
    public Optional<ItemResponse> toggleItem(Long id) {
        Optional<Item> itemOptional = itemRepository.findById(id);

        if (itemOptional.isPresent()) {
            itemOptional.get().setEnable(!itemOptional.get().isEnable());
            return Optional.of(itemMapper.toResponse(
                    itemRepository.save(itemOptional.orElseThrow())));
        }

        return Optional.empty();
    }

    @Transactional
    public Optional<ItemResponse> deleteItem(Long id) {
        Optional<Item> itemOptional = itemRepository.findById(id);

        if (itemOptional.isPresent()) {
            itemRepository.delete(itemOptional.orElseThrow());
            return Optional.of(itemMapper.toResponse(itemOptional.orElseThrow()));
        }

        return Optional.empty();
    }

    public List<ItemClass> getAllItemClasses() {
        return itemClassRepository.findAll();
    }
}
