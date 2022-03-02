import { entityItemSelector } from '../../support/commands';
import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('IngredientForRecipe e2e test', () => {
  const ingredientForRecipePageUrl = '/ingredient-for-recipe';
  const ingredientForRecipePageUrlPattern = new RegExp('/ingredient-for-recipe(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const ingredientForRecipeSample = { quantity: 37335 };

  let ingredientForRecipe: any;
  let ingredient: any;
  let unit: any;
  let recipe: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/ingredients',
      body: { name: 'hack' },
    }).then(({ body }) => {
      ingredient = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/units',
      body: { name: 'Multi-lateral' },
    }).then(({ body }) => {
      unit = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/recipes',
      body: { name: 'Table brand Accountability', description: 'syndicate Ergonomic' },
    }).then(({ body }) => {
      recipe = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/ingredient-for-recipes+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/ingredient-for-recipes').as('postEntityRequest');
    cy.intercept('DELETE', '/api/ingredient-for-recipes/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/ingredients', {
      statusCode: 200,
      body: [ingredient],
    });

    cy.intercept('GET', '/api/units', {
      statusCode: 200,
      body: [unit],
    });

    cy.intercept('GET', '/api/recipes', {
      statusCode: 200,
      body: [recipe],
    });
  });

  afterEach(() => {
    if (ingredientForRecipe) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/ingredient-for-recipes/${ingredientForRecipe.id}`,
      }).then(() => {
        ingredientForRecipe = undefined;
      });
    }
  });

  afterEach(() => {
    if (ingredient) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/ingredients/${ingredient.id}`,
      }).then(() => {
        ingredient = undefined;
      });
    }
    if (unit) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/units/${unit.id}`,
      }).then(() => {
        unit = undefined;
      });
    }
    if (recipe) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/recipes/${recipe.id}`,
      }).then(() => {
        recipe = undefined;
      });
    }
  });

  it('IngredientForRecipes menu should load IngredientForRecipes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('ingredient-for-recipe');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('IngredientForRecipe').should('exist');
    cy.url().should('match', ingredientForRecipePageUrlPattern);
  });

  describe('IngredientForRecipe page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(ingredientForRecipePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create IngredientForRecipe page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/ingredient-for-recipe/new$'));
        cy.getEntityCreateUpdateHeading('IngredientForRecipe');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', ingredientForRecipePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/ingredient-for-recipes',
          body: {
            ...ingredientForRecipeSample,
            ingredient: ingredient,
            unit: unit,
            recipe: recipe,
          },
        }).then(({ body }) => {
          ingredientForRecipe = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/ingredient-for-recipes+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [ingredientForRecipe],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(ingredientForRecipePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details IngredientForRecipe page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('ingredientForRecipe');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', ingredientForRecipePageUrlPattern);
      });

      it('edit button click should load edit IngredientForRecipe page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('IngredientForRecipe');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', ingredientForRecipePageUrlPattern);
      });

      it('last delete button click should delete instance of IngredientForRecipe', () => {
        cy.intercept('GET', '/api/ingredient-for-recipes/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('ingredientForRecipe').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', ingredientForRecipePageUrlPattern);

        ingredientForRecipe = undefined;
      });
    });
  });

  describe('new IngredientForRecipe page', () => {
    beforeEach(() => {
      cy.visit(`${ingredientForRecipePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('IngredientForRecipe');
    });

    it('should create an instance of IngredientForRecipe', () => {
      cy.get(`[data-cy="quantity"]`).type('28886').should('have.value', '28886');

      cy.get(`[data-cy="ingredient"]`).select(1);
      cy.get(`[data-cy="unit"]`).select(1);
      cy.get(`[data-cy="recipe"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        ingredientForRecipe = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', ingredientForRecipePageUrlPattern);
    });
  });
});
