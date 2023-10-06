package com.example.DinnerForOne;

import com.example.DinnerForOne.model.ExtendedIngredients;
import com.example.DinnerForOne.model.Ingredient;
import com.example.DinnerForOne.service.GroceryListService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GroceryListServiceTest {
    private GroceryListService groceryListService;
    private WebClient webClient;
    private WebClient.RequestHeadersUriSpec<?> requestHeadersUriSpec;
    private WebClient.RequestHeadersSpec<?> requestHeadersSpec;
    private WebClient.ResponseSpec responseSpec;

    @BeforeEach
    void setUp() {
        initMocks();
    }

    private void initMocks() {
        MockitoAnnotations.openMocks(this);
        groceryListService = new GroceryListService();
    }

    private void configureMockWebClient() {
        ExtendedIngredients mockIngredients = new ExtendedIngredients();
        mockIngredients.setExtendedIngredients(Collections.singletonList(new Ingredient()));

        WebClientWithSpecs webClientWithSpecs = new WebClientMockUtils().createMockWebClient(mockIngredients);
        webClient = webClientWithSpecs.getWebClient();
        requestHeadersUriSpec = webClientWithSpecs.getRequestHeadersUriSpec();
        requestHeadersSpec = webClientWithSpecs.getRequestHeadersSpec();
        responseSpec = webClientWithSpecs.getResponseSpec();

        ReflectionTestUtils.setField(groceryListService, "webClient", webClient);
        ReflectionTestUtils.setField(groceryListService, "apiKey", "your_api_key_here");
    }

    @Test
    void testFetchIngredientsForRecipeValidResponse() {
        configureMockWebClient();

        List<Ingredient> extendedIngredients = groceryListService.fetchIngredientsForRecipe("https://recipe.test.com");

        // Verify that the WebClient was called with the expected parameters
        verify(webClient).get();
        verify(requestHeadersUriSpec).uri("/recipes/extract?url=https://recipe.test.com");
        verify(requestHeadersUriSpec).headers(any());
        verify(requestHeadersUriSpec).accept(MediaType.APPLICATION_JSON);
        verify(requestHeadersSpec).retrieve();
        verify(responseSpec).bodyToMono(ExtendedIngredients.class);

        assert extendedIngredients != null;
        assert extendedIngredients.size() == 1;
    }

    @Test
    void testFetchIngredientsForRecipeMissingUrl() {
        GroceryListService groceryListService = new GroceryListService();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            groceryListService.fetchIngredientsForRecipe("");
        });

        assertEquals("URL cannot be null or empty.", exception.getMessage());
    }

    @Test
    void testFetchIngredientsForRecipeInvalidUrl() {
        GroceryListService groceryListService = new GroceryListService();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            groceryListService.fetchIngredientsForRecipe("invalid_url");
        });

        assertEquals("Invalid URL format.", exception.getMessage());
    }

    public class WebClientWithSpecs {
        private WebClient webClient;
        private WebClient.RequestHeadersUriSpec<?> requestHeadersUriSpec;
        private WebClient.RequestHeadersSpec<?> requestHeadersSpec;
        private WebClient.ResponseSpec responseSpec;

        public WebClient getWebClient() {
            return webClient;
        }

        public void setWebClient(WebClient webClient) {
            this.webClient = webClient;
        }

        public WebClient.RequestHeadersUriSpec<?> getRequestHeadersUriSpec() {
            return requestHeadersUriSpec;
        }

        public void setRequestHeadersUriSpec(WebClient.RequestHeadersUriSpec<?> requestHeadersUriSpec) {
            this.requestHeadersUriSpec = requestHeadersUriSpec;
        }

        public WebClient.RequestHeadersSpec<?> getRequestHeadersSpec() {
            return requestHeadersSpec;
        }

        public void setRequestHeadersSpec(WebClient.RequestHeadersSpec<?> requestHeadersSpec) {
            this.requestHeadersSpec = requestHeadersSpec;
        }

        public WebClient.ResponseSpec getResponseSpec() {
            return responseSpec;
        }

        public void setResponseSpec(WebClient.ResponseSpec responseSpec) {
            this.responseSpec = responseSpec;
        }
    }

    public class WebClientMockUtils {

        public WebClientWithSpecs createMockWebClient(ExtendedIngredients mockIngredients) {
            WebClient webClient = Mockito.mock(WebClient.class);

            WebClient.RequestHeadersUriSpec requestHeadersUriSpec = mock(WebClient.RequestHeadersUriSpec.class);
            when(webClient.get()).thenReturn(requestHeadersUriSpec);
            when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersUriSpec);
            when(requestHeadersUriSpec.headers(any())).thenReturn(requestHeadersUriSpec);

            WebClient.RequestHeadersSpec<?> requestHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
            when(requestHeadersUriSpec.accept(any())).thenReturn(requestHeadersSpec);

            WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);
            when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
            when(responseSpec.bodyToMono(ExtendedIngredients.class)).thenReturn(Mono.just(mockIngredients));

            WebClientWithSpecs webClientWithSpecs = new WebClientWithSpecs();
            webClientWithSpecs.setWebClient(webClient);
            webClientWithSpecs.setRequestHeadersUriSpec(requestHeadersUriSpec);
            webClientWithSpecs.setRequestHeadersSpec(requestHeadersSpec);
            webClientWithSpecs.setResponseSpec(responseSpec);

            return webClientWithSpecs;
        }
    }
}


