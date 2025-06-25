Feature: Find a provider by name
  As a Manager
  I need to recover a provider
  To see its details

  Scenario Outline: Find an existing provider by name
    Given the following relation of providers
      | nif | name       | email      |
      | 123 | provider-1 | u@p123.com |
      | 456 | provider-1 | u@p456.com |
      | 789 | provider-2 | u@p789.com |
      | ABC | provider-3 | u@pABC.com |
    When we look for providers by name <name>
    Then we get <quantity> providers with name <name> and nifs <nifs>

    Examples: 
      | name         | quantity | nifs       |
      | "provider-1" |        2 | "123, 456" |
      | "provider-2" |        1 | "789"      |
      | "provider-3" |        1 | "ABC"      |
