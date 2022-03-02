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

describe('Ingredient e2e test', () => {
  const ingredientPageUrl = '/ingredient';
  const ingredientPageUrlPattern = new RegExp('/ingredient(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const ingredientSample = { name: 'niches deposit' };

  let ingredient: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/ingredients+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/ingredients').as('postEntityRequest');
    cy.intercept('DELETE', '/api/ingredients/*').as('deleteEntityRequest');
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
  });

  it('Ingredients menu should load Ingredients page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('ingredient');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Ingredient').should('exist');
    cy.url().should('match', ingredientPageUrlPattern);
  });

  describe('Ingredient page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(ingredientPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Ingredient page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/ingredient/new$'));
        cy.getEntityCreateUpdateHeading('Ingredient');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', ingredientPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/ingredients',
          body: ingredientSample,
        }).then(({ body }) => {
          ingredient = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/ingredients+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [ingredient],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(ingredientPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Ingredient page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('ingredient');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', ingredientPageUrlPattern);
      });

      it('edit button click should load edit Ingredient page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Ingredient');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', ingredientPageUrlPattern);
      });

      it('last delete button click should delete instance of Ingredient', () => {
        cy.intercept('GET', '/api/ingredients/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('ingredient').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', ingredientPageUrlPattern);

        ingredient = undefined;
      });
    });
  });

  describe('new Ingredient page', () => {
    beforeEach(() => {
      cy.visit(`${ingredientPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Ingredient');
    });

    it('should create an instance of Ingredient', () => {
      cy.get(`[data-cy="name"]`).type('Metal synthesize').should('have.value', 'Metal synthesize');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        ingredient = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', ingredientPageUrlPattern);
    });
  });
});
