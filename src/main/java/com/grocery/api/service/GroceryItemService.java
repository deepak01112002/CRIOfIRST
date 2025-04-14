package com.grocery.api.service;

import com.grocery.api.dto.GroceryItemDTO;
import com.grocery.api.exception.ResourceNotFoundException;
import com.grocery.api.model.GroceryItem;
import com.grocery.api.repository.GroceryItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroceryItemService {

    private final GroceryItemRepository groceryItemRepository;

    @Autowired
    public GroceryItemService(GroceryItemRepository groceryItemRepository) {
        this.groceryItemRepository = groceryItemRepository;
    }

    public List<GroceryItemDTO> getAllGroceryItems() {
        return groceryItemRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public GroceryItemDTO getGroceryItemById(Long id) {
        GroceryItem groceryItem = groceryItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Grocery item not found with id: " + id));
        return convertToDTO(groceryItem);
    }

    public List<GroceryItemDTO> getGroceryItemsByCategory(String category) {
        return groceryItemRepository.findByCategory(category).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<GroceryItemDTO> searchGroceryItems(String name) {
        return groceryItemRepository.findByNameContainingIgnoreCase(name).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public GroceryItemDTO createGroceryItem(GroceryItemDTO groceryItemDTO) {
        GroceryItem groceryItem = convertToEntity(groceryItemDTO);
        GroceryItem savedGroceryItem = groceryItemRepository.save(groceryItem);
        return convertToDTO(savedGroceryItem);
    }

    public GroceryItemDTO updateGroceryItem(Long id, GroceryItemDTO groceryItemDTO) {
        GroceryItem existingGroceryItem = groceryItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Grocery item not found with id: " + id));

        existingGroceryItem.setName(groceryItemDTO.getName());
        existingGroceryItem.setCategory(groceryItemDTO.getCategory());
        existingGroceryItem.setPrice(groceryItemDTO.getPrice());
        existingGroceryItem.setQuantity(groceryItemDTO.getQuantity());

        GroceryItem updatedGroceryItem = groceryItemRepository.save(existingGroceryItem);
        return convertToDTO(updatedGroceryItem);
    }

    public void deleteGroceryItem(Long id) {
        if (!groceryItemRepository.existsById(id)) {
            throw new ResourceNotFoundException("Grocery item not found with id: " + id);
        }
        groceryItemRepository.deleteById(id);
    }

    // Helper methods to convert between Entity and DTO
    private GroceryItemDTO convertToDTO(GroceryItem groceryItem) {
        return new GroceryItemDTO(
                groceryItem.getId(),
                groceryItem.getName(),
                groceryItem.getCategory(),
                groceryItem.getPrice(),
                groceryItem.getQuantity()
        );
    }

    private GroceryItem convertToEntity(GroceryItemDTO groceryItemDTO) {
        GroceryItem groceryItem = new GroceryItem();
        groceryItem.setId(groceryItemDTO.getId());
        groceryItem.setName(groceryItemDTO.getName());
        groceryItem.setCategory(groceryItemDTO.getCategory());
        groceryItem.setPrice(groceryItemDTO.getPrice());
        groceryItem.setQuantity(groceryItemDTO.getQuantity());
        return groceryItem;
    }
}
