package com.luckyprogrammer.dnd_inventory_backend.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.luckyprogrammer.dnd_inventory_backend.dtos.request.CreateItemRequest;
import com.luckyprogrammer.dnd_inventory_backend.dtos.request.UpdateItemRequest;
import com.luckyprogrammer.dnd_inventory_backend.dtos.response.ItemResponse;
import com.luckyprogrammer.dnd_inventory_backend.entities.ItemClass;
import com.luckyprogrammer.dnd_inventory_backend.services.ItemService;

@RestController
@RequestMapping("/dnd/v1.0.0/items")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getItemById(@PathVariable Long id) {

        Optional<ItemResponse> itemResponseOptional = itemService.getItemById(id);

        if (itemResponseOptional.isPresent())
            return ResponseEntity.status(HttpStatus.OK).body(itemResponseOptional.get());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

    }

    @GetMapping("/search")
    public ResponseEntity<?> searchPublicItems(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long minPrice,
            @RequestParam(required = false) Long maxPrice,
            @RequestParam(required = false) Integer weight,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Page<ItemResponse> items = itemService.searchPublicItems(name, minPrice, maxPrice, weight, categoryId, page,
                size, sortBy, sortDir);

        if (items.isEmpty())
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body("No hay items que cumplan con los criterios de búsqueda.");

        return ResponseEntity.status(HttpStatus.OK).body(items);
    }

    @PostMapping("/register")
    public ResponseEntity<?> createItem(@RequestBody CreateItemRequest item) {
        ItemResponse createdItem = itemService.createItem(item);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdItem);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateItem(@PathVariable Long id, @RequestBody UpdateItemRequest item) {
        Optional<ItemResponse> updatedItem = itemService.updateItem(id, item);

        if (updatedItem.isPresent())
            return ResponseEntity.status(HttpStatus.OK).body(updatedItem.get());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<?> toggle(@PathVariable Long id) {
        Optional<ItemResponse> toggledItem = itemService.toggleItem(id);

        if (toggledItem.isPresent())
            return ResponseEntity.status(HttpStatus.OK).body(toggledItem.orElseThrow());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteItem(@PathVariable Long id) {
        Optional<ItemResponse> deletedItem = itemService.deleteItem(id);

        if (deletedItem.isPresent())
            return ResponseEntity.status(HttpStatus.OK).body(deletedItem.orElseThrow());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleValidationExceptions(IllegalArgumentException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @GetMapping("/classes")
    ResponseEntity<?> getAllItemClasses() {
        List<ItemClass> itemClasses = itemService.getAllItemClasses();

        if (itemClasses.isEmpty())
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        return ResponseEntity.status(HttpStatus.OK).body(itemClasses);

    }
}
