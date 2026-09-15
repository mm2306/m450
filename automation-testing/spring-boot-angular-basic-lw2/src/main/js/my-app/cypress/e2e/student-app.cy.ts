describe('Student Management App E2E Tests', () => {
  beforeEach(() => {
    cy.visit('/students');
  });

  it('should display initial student list table with correct headers', () => {
    cy.contains('a', 'List Students').should('be.visible');
    cy.get('table').should('be.visible');
    cy.get('table thead tr th').eq(0).should('contain.text', '#');
    cy.get('table thead tr th').eq(1).should('contain.text', 'Name');
    cy.get('table thead tr th').eq(2).should('contain.text', 'Email');
  });

  it('should navigate to Add Student form and submit a new student', () => {
    // Click navigation link to add student
    cy.contains('a', 'Add Students').click();

    cy.url().should('include', '/addstudents');

    // Fill form fields
    cy.get('input#name').type('Cypress Automated User');
    cy.get('input#email').type('cypress.user@tbz.ch');

    // Submit form
    cy.get('button[type="submit"]').click();

    // Should redirect back to /students
    cy.url().should('include', '/students');

    // Verify new student is displayed in table
    cy.contains('td', 'Cypress Automated User').should('be.visible');
    cy.contains('td', 'cypress.user@tbz.ch').should('be.visible');
  });

  it('should validate invalid input and keep submit button disabled', () => {
    cy.contains('a', 'Add Students').click();

    // Type invalid email
    cy.get('input#name').type('Test User');
    cy.get('input#email').type('invalid-email-format');

    // Submit button should be disabled due to form validation
    cy.get('button[type="submit"]').should('be.disabled');
    cy.contains('Bitte eine gültige E-Mail Adresse eingeben').should('be.visible');
  });
});
