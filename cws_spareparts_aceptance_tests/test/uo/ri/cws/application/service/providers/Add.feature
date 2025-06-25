Feature: Add a provider
  As a Manager
  I want to register a provider 
  Because we need to buy somebody the spare parts we sell

  Scenario: Add a non existing provider
    When I add a new non existing provider
    Then the provider results added to the system

  Scenario: Try to add a provider with a repeated nif
    Given a provider
    When I try to add a new provider with same nif
    Then an error happens with an explaining message

  Scenario: Try to add a provider with different nif but rest of attributes repeated
    Given a provider with
      | nif          | name | email           | phone         |
      | "123456789z" | "P1" | "p1@spares.com" | "123-456-789" |
    When I try to add a new provider with
      | nif                          | name | email           | phone         |
      | "different-but-all-the-rest" | "P1" | "p1@spares.com" | "123-456-789" |
    Then an error happens with an explaining message

  Scenario Outline: Try to add a provider with wrong fields
    When I try to add a new provider with <nif>, <name>, <email>, <phone>
    Then argument is rejected with an explaining message

    Examples: 
      | nif          | name | email           | phone         |
      | "123456789z" | "P1" | "p1-spares.com" | "123-456-789" |
      | ""           | "P1" | "p1@spares.com" | "123-456-789" |
      | "123456789z" | ""   | "p1@spares.com" | "123-456-789" |
      | "123456789z" | "P1" | ""              | "123-456-789" |
      | "123456789z" | "P1" | "p1@spares.com" | ""            |
