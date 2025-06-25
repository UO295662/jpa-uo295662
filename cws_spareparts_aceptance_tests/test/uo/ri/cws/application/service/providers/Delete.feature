Feature: Delete a provider
  As a Manager
  I want to delete a provider
  in case have not been used to keep the system clean

  Scenario: Delete an existing unused provider
    Given a provider
    When I remove the provider
    Then the provider no longer exists
    
  Scenario: Try to remove a non existing provider
    When I try to remove a non existent provider
    Then an error happens with an explaining message

  Scenario: Try to remove a used provider with order lines
    Given a provider with orders registered
    When I try to remove the provider
    Then an error happens with an explaining message

  Scenario: Try to remove a used provider with suppliers
    Given a provider with supplies registered
    When I try to remove the provider
    Then an error happens with an explaining message
