import { getProductCards } from '../support/app.po';

// Needs the backend running on localhost:8090 with the demo data
describe('selling-frontend-e2e', () => {
  beforeEach(() => cy.visit('/'));

  it('should show products on the home page', () => {
    getProductCards().should('have.length.at.least', 4);
  });

  it('should add a product to the cart and ask for sign in at checkout', () => {
    cy.visit('/product/1');
    cy.contains('button', 'Add to cart').click();
    cy.visit('/cart');
    cy.contains('a', 'Continue to checkout').click();
    cy.location('pathname').should('eq', '/login');
  });
});
