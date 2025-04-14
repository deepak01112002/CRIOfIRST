package com.grocery.api.controller;

import com.grocery.api.dto.GroceryItemDTO;
import com.grocery.api.service.GroceryItemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/grocery-items")
public class GroceryItemController {

    private final GroceryItemService groceryItemService;

    @Autowired
    public GroceryItemController(GroceryItemService groceryItemService) {
        this.groceryItemService = groceryItemService;
    }

    @GetMapping
    public ResponseEntity<List<GroceryItemDTO>> getAllGroceryItems() {
        List<GroceryItemDTO> groceryItems = groceryItemService.getAllGroceryItems();
        return new ResponseEntity<>(groceryItems, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroceryItemDTO> getGroceryItemById(@PathVariable Long id) {
        GroceryItemDTO groceryItem = groceryItemService.getGroceryItemById(id);
        return new ResponseEntity<>(groceryItem, HttpStatus.OK);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<GroceryItemDTO>> getGroceryItemsByCategory(@PathVariable String category) {
        List<GroceryItemDTO> groceryItems = groceryItemService.getGroceryItemsByCategory(category);
        return new ResponseEntity<>(groceryItems, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<GroceryItemDTO>> searchGroceryItems(@RequestParam String name) {
        List<GroceryItemDTO> groceryItems = groceryItemService.searchGroceryItems(name);
        return new ResponseEntity<>(groceryItems, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<GroceryItemDTO> createGroceryItem(@Valid @RequestBody GroceryItemDTO groceryItemDTO) {
        GroceryItemDTO createdGroceryItem = groceryItemService.createGroceryItem(groceryItemDTO);
        return new ResponseEntity<>(createdGroceryItem, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GroceryItemDTO> updateGroceryItem(
            @PathVariable Long id, @Valid @RequestBody GroceryItemDTO groceryItemDTO) {
        GroceryItemDTO updatedGroceryItem = groceryItemService.updateGroceryItem(id, groceryItemDTO);
        return new ResponseEntity<>(updatedGroceryItem, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroceryItem(@PathVariable Long id) {
        groceryItemService.deleteGroceryItem(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
