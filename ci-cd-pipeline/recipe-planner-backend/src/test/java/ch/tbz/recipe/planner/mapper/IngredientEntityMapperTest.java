package ch.tbz.recipe.planner.mapper;

import ch.tbz.recipe.planner.domain.Ingredient;
import ch.tbz.recipe.planner.domain.Unit;
import ch.tbz.recipe.planner.entities.IngredientEntity;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

class IngredientEntityMapperTest {

    private final IngredientEntityMapper mapper = new IngredientEntityMapperImpl();

    @Test
    void entityToDomain_mapsAllFields() {
        UUID ingredientId = UUID.randomUUID();
        IngredientEntity entity = new IngredientEntity(ingredientId, "Milk", "fresh", Unit.LITRE, 1);

        Ingredient ingredient = mapper.entityToDomain(entity);

        SoftAssertions soft = new SoftAssertions();
        soft.assertThat(ingredient.getId()).isEqualTo(ingredientId);
        soft.assertThat(ingredient.getName()).isEqualTo("Milk");
        soft.assertThat(ingredient.getComment()).isEqualTo("fresh");
        soft.assertThat(ingredient.getUnit()).isEqualTo(Unit.LITRE);
        soft.assertThat(ingredient.getAmount()).isEqualTo(1);
        soft.assertAll();
    }

    @Test
    void domainToEntity_mapsAllFields() {
        UUID ingredientId = UUID.randomUUID();
        Ingredient domain = new Ingredient(ingredientId, "Sugar", "white", Unit.GRAMM, 100);

        IngredientEntity entity = mapper.domainToEntity(domain);

        SoftAssertions soft = new SoftAssertions();
        soft.assertThat(entity.getId()).isEqualTo(ingredientId);
        soft.assertThat(entity.getName()).isEqualTo("Sugar");
        soft.assertThat(entity.getComment()).isEqualTo("white");
        soft.assertThat(entity.getUnit()).isEqualTo(Unit.GRAMM);
        soft.assertThat(entity.getAmount()).isEqualTo(100);
        soft.assertAll();
    }

    @Test
    void entitiesToDomains_mapsListOfIngredients() {
        List<IngredientEntity> entities = List.of(
                new IngredientEntity(UUID.randomUUID(), "Milk", "fresh", Unit.LITRE, 1),
                new IngredientEntity(UUID.randomUUID(), "Sugar", "white", Unit.GRAMM, 100));

        List<Ingredient> ingredients = mapper.entitiesToDomains(entities);

        SoftAssertions soft = new SoftAssertions();
        soft.assertThat(ingredients).hasSize(2);
        soft.assertThat(ingredients.get(0).getName()).isEqualTo("Milk");
        soft.assertThat(ingredients.get(0).getUnit()).isEqualTo(Unit.LITRE);
        soft.assertThat(ingredients.get(0).getAmount()).isEqualTo(1);
        soft.assertThat(ingredients.get(1).getName()).isEqualTo("Sugar");
        soft.assertThat(ingredients.get(1).getUnit()).isEqualTo(Unit.GRAMM);
        soft.assertThat(ingredients.get(1).getAmount()).isEqualTo(100);
        soft.assertAll();
    }

    @Test
    void domainsToEntities_mapsListOfIngredients() {
        List<Ingredient> domains = List.of(
                new Ingredient(UUID.randomUUID(), "Eggs", null, Unit.PIECE, 4),
                new Ingredient(UUID.randomUUID(), "Flour", null, Unit.DECILITRE, 2));

        List<IngredientEntity> entities = mapper.domainsToEntities(domains);

        SoftAssertions soft = new SoftAssertions();
        soft.assertThat(entities).hasSize(2);
        soft.assertThat(entities.get(0).getName()).isEqualTo("Eggs");
        soft.assertThat(entities.get(0).getUnit()).isEqualTo(Unit.PIECE);
        soft.assertThat(entities.get(0).getAmount()).isEqualTo(4);
        soft.assertThat(entities.get(1).getName()).isEqualTo("Flour");
        soft.assertThat(entities.get(1).getUnit()).isEqualTo(Unit.DECILITRE);
        soft.assertThat(entities.get(1).getAmount()).isEqualTo(2);
        soft.assertAll();
    }
}