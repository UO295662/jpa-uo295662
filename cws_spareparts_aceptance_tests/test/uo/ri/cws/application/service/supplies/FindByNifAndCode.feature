Feature: Find supplies by provider and spare
  As a Manager
  I need to recover supplies for provider and spare 
  To see its details ans check the orders produced

  Scenario Outline: Find supplies by provider nif and spare code
    Given the following relation of supplies
      | nif | code | price | days |
      | 123 | SP1  |   1.0 |    1 |
      | 123 | SP2  |   2.0 |    2 |
      | 123 | SP3  |   3.0 |    3 |
      | 456 | SP1  |   4.0 |    4 |
      | 456 | SP2  |   5.0 |    5 |
      | 789 | SP1  |   6.0 |    6 |
    When I look for a supply by nif <nif> and code <code>
    Then I get the supplies for that provider nif <nif> and spare code <code> with price <price> ans delivery <days> days

    Examples: 
      | nif   | code  | price | days |
      | "123" | "SP1" |   1.0 |    1 |
      | "123" | "SP2" |   2.0 |    2 |
      | "123" | "SP3" |   3.0 |    3 |
      | "456" | "SP1" |   4.0 |    4 |
      | "456" | "SP2" |   5.0 |    5 |
      | "789" | "SP1" |   6.0 |    6 |

  Scenario: Try to find a non existent supply
    Given a provider for supplies
    And a registered spare part with no supplies
    When I try to find the non existing supply for that spare and provider
    Then empty is returned

  Scenario: Try to find a supply with null nif and spare code
    When I try to find a supply with null nif and spare code
    Then argument is rejected with an explaining message
