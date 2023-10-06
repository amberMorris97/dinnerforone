package com.example.DinnerForOne;

import com.example.DinnerForOne.controller.GroceryListController;
import com.example.DinnerForOne.model.Ingredient;
import com.example.DinnerForOne.service.GroceryListService;
import com.example.DinnerForOne.util.UrlValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GroceryListControllerTest {
    @Mock
    private GroceryListService groceryListService;
    private GroceryListController groceryListController;
    private UrlValidator urlValidator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        urlValidator = new UrlValidator();
        groceryListController = new GroceryListController(groceryListService, urlValidator);
    }

    @Test
    void testFetchIngredientsForRecipeValidResponse() {
        String validUrl = "https://api.test.com";
        List<Ingredient> mockIngredients = Collections.singletonList(new Ingredient());

        when(groceryListService.fetchIngredientsForRecipe(validUrl)).thenReturn(mockIngredients);

        ResponseEntity<?> responseEntity = groceryListController.fetchIngredientsForRecipe(validUrl);

        verify(groceryListService).fetchIngredientsForRecipe(validUrl);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(mockIngredients, responseEntity.getBody());
    }

    @Test
    void testFetchIngredientsForRecipeMissingUrl() {
        String missingUrl = "";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            groceryListController.fetchIngredientsForRecipe(missingUrl);
        });

        assertEquals("URL cannot be null or empty.", exception.getMessage());
    }

    @Test
    void testFetchIngredientsForRecipeInvalidUrl() {
        String invalidUrl = "invalid-url";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            groceryListController.fetchIngredientsForRecipe(invalidUrl);
        });

        assertEquals("Invalid URL format.", exception.getMessage());
    }

    @Test
    void testFetchIngredientsForRecipeServiceError() {
        String validUrl = "https://validurl.com";
        String errorMessage = "Service error message";

        when(groceryListService.fetchIngredientsForRecipe(validUrl)).thenThrow(new RuntimeException(errorMessage));

        ResponseEntity<?> responseEntity = groceryListController.fetchIngredientsForRecipe(validUrl);

        verify(groceryListService).fetchIngredientsForRecipe(validUrl);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody() instanceof String);
        assertTrue(((String) responseEntity.getBody()).contains(errorMessage));
    }
}
