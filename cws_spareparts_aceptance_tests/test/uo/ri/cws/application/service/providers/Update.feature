Feature: Update a provider
  As a Manager
  I want to update a provider
  For been able to solve workorders and earn some money selling spareparts

  Scenario: Update an existing provider
    Given a provider
    When I update the provider for all its fields
    Then the provider results updated for all the fields except the nif and id

  Scenario: Try to update a stale provider
    Given a provider
    When I try to update the provider with wrong version
    Then an error happens with an explaining message

  Scenario: Try to update an non existing provider
    When I try to update a non existing provider
    Then an error happens with an explaining message

  Scenario: Try to update to repeated fields
    Given the providers with
      | nif   | name | email        | phone |
      | 1234z | P1   | p1@email.com |   123 |
      | 5678x | P2   | p2@email.com |   456 |
    When I try to update the provider with nif "5678x" to <name>, <email> and <phone>
      | name | email        | phone |
      | P1   | p1@email.com |   123 |
    Then an error happens with an explaining message

  Scenario Outline: Try to update with wrong parameters
    Given a provider
    When I try to update the provider to <name>, <email> and <phone>
    Then argument is rejected with an explaining message

    Examples: 
      | name       | email                    | phone       |
      | "new-name" | "provider-new.email.com" | "new-phone" |
      | ""         | "provider-new@email.com" | "new-phone" |
      | "new-name" | ""                       | "new-phone" |
      | "new-name" | "provider-new@email.com" | ""          |
