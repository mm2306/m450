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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RecipeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    private Recipe createRecipe(String name) throws Exception {
        Recipe recipe = new Recipe(null, name, "description", "http://localhost/image.jpg",
                List.of(new Ingredient(null, "Tomato", "ripe", Unit.PIECE, 3)));
        MvcResult result = mockMvc.perform(post("/api/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recipe)))
                .andExpect(status().isOk())
                .andReturn();
        return objectMapper.readValue(result.getResponse().getContentAsString(), Recipe.class);
    }

    @Test
    void updateRecipe_updatesExistingRecipe() throws Exception {
        Recipe created = createRecipe("Original name");
        UUID recipeId = created.getId();

        Recipe update = new Recipe(recipeId, "Updated name", "updated description", "http://localhost/new.jpg",
                List.of(new Ingredient(null, "Salt", "", Unit.GRAMM, 10)));
        mockMvc.perform(put("/api/recipes/recipe/{recipeId}", recipeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(recipeId.toString()))
                .andExpect(jsonPath("$.name").value("Updated name"));

        mockMvc.perform(get("/api/recipes/recipe/{recipeId}", recipeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated name"))
                .andExpect(jsonPath("$.description").value("updated description"))
                .andExpect(jsonPath("$.ingredients.length()").value(1))
                .andExpect(jsonPath("$.ingredients[0].name").value("Salt"))
                .andExpect(jsonPath("$.ingredients[0].amount").value(10));
    }

    @Test
    void updateRecipe_ignoresIdInRequestBody() throws Exception {
        Recipe created = createRecipe("Keep id from path");
        UUID recipeId = created.getId();

        Recipe update = new Recipe(UUID.randomUUID(), "Renamed", "", "", List.of());
        mockMvc.perform(put("/api/recipes/recipe/{recipeId}", recipeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(recipeId.toString()))
                .andExpect(jsonPath("$.name").value("Renamed"));
    }

    @Test
    void updateRecipe_unknownId_returnsNotFound() throws Exception {
        Recipe update = new Recipe(null, "Ghost", "", "", List.of());
        mockMvc.perform(put("/api/recipes/recipe/{recipeId}", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isNotFound());
    }
}
