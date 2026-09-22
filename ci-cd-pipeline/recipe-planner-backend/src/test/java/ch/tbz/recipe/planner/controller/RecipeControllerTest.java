package ch.tbz.recipe.planner.controller;

import ch.tbz.recipe.planner.domain.Ingredient;
import ch.tbz.recipe.planner.domain.Recipe;
import ch.tbz.recipe.planner.domain.Unit;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RecipeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getRecipes_returnsListOfRecipes() throws Exception {
        mockMvc.perform(get("/api/recipes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[0].name").isNotEmpty())
                .andExpect(jsonPath("$[0].ingredients").isArray());
    }

    @Test
    void getRecipe_returnsRecipeById() throws Exception {
        UUID recipeId = createRecipe("Spaghetti Carbonara");

        mockMvc.perform(get("/api/recipes/recipe/{recipeId}", recipeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(recipeId.toString()))
                .andExpect(jsonPath("$.name").value("Spaghetti Carbonara"));
    }

    @Test
    void addRecipe_createsAndReturnsRecipe() throws Exception {
        Recipe recipe = new Recipe(
                UUID.randomUUID(),
                "Lasagne",
                "Classic Italian",
                "https://img.example.com/lasagne.jpg",
                List.of(new Ingredient(UUID.randomUUID(), "Tomato", "ripe", Unit.KILOGRAMM, 1)));

        mockMvc.perform(post("/api/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recipe)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.name").value("Lasagne"))
                .andExpect(jsonPath("$.ingredients", hasSize(1)))
                .andExpect(jsonPath("$.ingredients[0].name").value("Tomato"));
    }

    private UUID createRecipe(String name) throws Exception {
        Recipe recipe = new Recipe(
                UUID.randomUUID(),
                name,
                "description",
                "https://img.example.com/recipe.jpg",
                List.of(new Ingredient(UUID.randomUUID(), "Tomato", "ripe", Unit.GRAMM, 200)));

        MvcResult result = mockMvc.perform(post("/api/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recipe)))
                .andExpect(status().isOk())
                .andReturn();

        return objectMapper.readValue(result.getResponse().getContentAsString(), Recipe.class).getId();
    }
}