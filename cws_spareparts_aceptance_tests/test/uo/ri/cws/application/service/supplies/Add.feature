Feature: Add a supply
  As a Manager
  I want to add a supply
  To known the better prices and then select the best provider for the next order
  and then save money

  Scenario Outline: Add a non existing supply
    Given a provider for supplies
    And a registered spare part with no supplies
    When I add a supply for that provider and spare part for <price> € and <days> days
    Then the supply exists with price <price> € and delivery in <days> days

    Examples: 
      | price | days |
      |  10.0 |   10 |
      |   0.0 |    1 |
      |  10.0 |    0 |

  Scenario: Try to add a repeated supply
    Given a supply
    When I try to add another supply for that provider and spare part
    Then an error happens with an explaining message

  Scenario: Try to add a supply to a non existing provider
    Given a registered spare part with no supplies
    When I try to add a supply for a non existing provider
    Then an error happens with an explaining message

  Scenario: Try to add a supply to a non existing spare part
    Given a provider for supplies
    When I try to add a supply for non existing spare part
    Then an error happens with an explaining message

  Scenario Outline: Try to add a supply with wrong fields
    Given a provider for supplies
    And a registered spare part with no supplies
    When I try to add a new supply with price <price> and delivery term <days>
    Then an error happens with an explaining message

    Examples: 
      | price | days |
      | -0.01 |    1 |
      |  -1.0 |    1 |
      |  10.0 |   -1 |

  Scenario Outline: Try to add a supply with null dto
    When I try to add a null supply
    Then argument is rejected with an explaining message

  Scenario Outline: Try to add a supply with null spare part
    When I try to add a supply with null spare part
    Then argument is rejected with an explaining message
