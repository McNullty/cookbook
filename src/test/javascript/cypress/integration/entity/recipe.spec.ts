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

describe('Recipe e2e test', () => {
  const recipePageUrl = '/recipe';
  const recipePageUrlPattern = new RegExp('/recipe(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const recipeSample = { name: 'Brand contingency deposit' };

  let recipe: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/recipes+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/recipes').as('postEntityRequest');
    cy.intercept('DELETE', '/api/recipes/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (recipe) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/recipes/${recipe.id}`,
      }).then(() => {
        recipe = undefined;
      });
    }
  });

  it('Recipes menu should load Recipes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('recipe');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Recipe').should('exist');
    cy.url().should('match', recipePageUrlPattern);
  });

  describe('Recipe page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(recipePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Recipe page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/recipe/new$'));
        cy.getEntityCreateUpdateHeading('Recipe');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', recipePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/recipes',
          body: recipeSample,
        }).then(({ body }) => {
          recipe = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/recipes+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [recipe],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(recipePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Recipe page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('recipe');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', recipePageUrlPattern);
      });

      it('edit button click should load edit Recipe page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Recipe');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', recipePageUrlPattern);
      });

      it('last delete button click should delete instance of Recipe', () => {
        cy.intercept('GET', '/api/recipes/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('recipe').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', recipePageUrlPattern);

        recipe = undefined;
      });
    });
  });

  describe('new Recipe page', () => {
    beforeEach(() => {
      cy.visit(`${recipePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Recipe');
    });

    it('should create an instance of Recipe', () => {
      cy.get(`[data-cy="name"]`).type('Junction').should('have.value', 'Junction');

      cy.get(`[data-cy="description"]`).type('invoice parsing').should('have.value', 'invoice parsing');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        recipe = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', recipePageUrlPattern);
    });
  });
});
