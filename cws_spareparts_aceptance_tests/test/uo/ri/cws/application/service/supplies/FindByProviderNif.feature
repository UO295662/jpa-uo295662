Feature: Find supplies by provider nif
  As a Manager
  I need to recover all the supplies by a provider 
  To see its details ans check the orders produced

  Scenario Outline: Find supplies by provider nif
    Given the following relation of supplies
      | nif | code |
      | 123 | SP1  |
      | 123 | SP2  |
      | 123 | SP3  |
      | 456 | SP1  |
      | 456 | SP2  |
      | 789 | SP1  |
    When I look for a supply by nif <nif>
    Then I get a list of supplies for provider nif <nif> and spare codes <codes>

    Examples: 
      | nif   | codes           |
      | "123" | "SP1, SP2, SP3" |
      | "456" | "SP1, SP2"      |
      | "789" | "SP1"           |

  Scenario: Try to find the supplies for a non existing provider
    When I try to find the supplies for a non existing provider
    Then an empty list is returned

  Scenario: Try to find the supplies for a null nif
    When I try to find the supplies for a null nif
    Then argument is rejected with an explaining message
