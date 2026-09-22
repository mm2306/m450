package ch.tbz.recipe.planner.mapper;

import ch.tbz.recipe.planner.domain.Ingredient;
import ch.tbz.recipe.planner.domain.Recipe;
import ch.tbz.recipe.planner.domain.Unit;
import ch.tbz.recipe.planner.entities.IngredientEntity;
import ch.tbz.recipe.planner.entities.RecipeEntity;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

class RecipeEntityMapperTest {

    private final RecipeEntityMapper mapper = new RecipeEntityMapperImpl();

    @Test
    void entityToDomain_mapsAllFields() {
        UUID recipeId = UUID.randomUUID();
        UUID ingredientId = UUID.randomUUID();
        RecipeEntity entity = new RecipeEntity(
                recipeId,
                "Rösti",
                "Swiss classic",
                "https://img.example.com/roesti.jpg",
                List.of(new IngredientEntity(ingredientId, "Potato", "Rösti potatoes", Unit.KILOGRAMM, 2)));

        Recipe recipe = mapper.entityToDomain(entity);

        SoftAssertions soft = new SoftAssertions();
        soft.assertThat(recipe.getId()).isEqualTo(recipeId);
        soft.assertThat(recipe.getName()).isEqualTo("Rösti");
        soft.assertThat(recipe.getDescription()).isEqualTo("Swiss classic");
        soft.assertThat(recipe.getImageUrl()).isEqualTo("https://img.example.com/roesti.jpg");
        soft.assertThat(recipe.getIngredients()).hasSize(1);
        Ingredient ingredient = recipe.getIngredients().get(0);
        soft.assertThat(ingredient.getId()).isEqualTo(ingredientId);
        soft.assertThat(ingredient.getName()).isEqualTo("Potato");
        soft.assertThat(ingredient.getComment()).isEqualTo("Rösti potatoes");
        soft.assertThat(ingredient.getUnit()).isEqualTo(Unit.KILOGRAMM);
        soft.assertThat(ingredient.getAmount()).isEqualTo(2);
        soft.assertAll();
    }

    @Test
    void domainToEntity_mapsAllFields() {
        UUID recipeId = UUID.randomUUID();
        UUID ingredientId = UUID.randomUUID();
        Recipe domain = new Recipe(
                recipeId,
                "Fondue",
                "Cheese fondue",
                "https://img.example.com/fondue.jpg",
                List.of(new Ingredient(ingredientId, "Cheese", "Gruyère", Unit.GRAMM, 600)));

        RecipeEntity entity = mapper.domainToEntity(domain);

        SoftAssertions soft = new SoftAssertions();
        soft.assertThat(entity.getId()).isEqualTo(recipeId);
        soft.assertThat(entity.getName()).isEqualTo("Fondue");
        soft.assertThat(entity.getDescription()).isEqualTo("Cheese fondue");
        soft.assertThat(entity.getImageUrl()).isEqualTo("https://img.example.com/fondue.jpg");
        soft.assertThat(entity.getIngredients()).hasSize(1);
        IngredientEntity ingredient = entity.getIngredients().get(0);
        soft.assertThat(ingredient.getId()).isEqualTo(ingredientId);
        soft.assertThat(ingredient.getName()).isEqualTo("Cheese");
        soft.assertThat(ingredient.getComment()).isEqualTo("Gruyère");
        soft.assertThat(ingredient.getUnit()).isEqualTo(Unit.GRAMM);
        soft.assertThat(ingredient.getAmount()).isEqualTo(600);
        soft.assertAll();
    }
}