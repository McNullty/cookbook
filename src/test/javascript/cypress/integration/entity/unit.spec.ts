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

describe('Unit e2e test', () => {
  const unitPageUrl = '/unit';
  const unitPageUrlPattern = new RegExp('/unit(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const unitSample = { name: 'Product' };

  let unit: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/units+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/units').as('postEntityRequest');
    cy.intercept('DELETE', '/api/units/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (unit) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/units/${unit.id}`,
      }).then(() => {
        unit = undefined;
      });
    }
  });

  it('Units menu should load Units page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('unit');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Unit').should('exist');
    cy.url().should('match', unitPageUrlPattern);
  });

  describe('Unit page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(unitPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Unit page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/unit/new$'));
        cy.getEntityCreateUpdateHeading('Unit');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', unitPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/units',
          body: unitSample,
        }).then(({ body }) => {
          unit = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/units+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [unit],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(unitPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Unit page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('unit');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', unitPageUrlPattern);
      });

      it('edit button click should load edit Unit page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Unit');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', unitPageUrlPattern);
      });

      it('last delete button click should delete instance of Unit', () => {
        cy.intercept('GET', '/api/units/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('unit').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', unitPageUrlPattern);

        unit = undefined;
      });
    });
  });

  describe('new Unit page', () => {
    beforeEach(() => {
      cy.visit(`${unitPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Unit');
    });

    it('should create an instance of Unit', () => {
      cy.get(`[data-cy="name"]`).type('microchip Rubber Metal').should('have.value', 'microchip Rubber Metal');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        unit = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', unitPageUrlPattern);
    });
  });
});
