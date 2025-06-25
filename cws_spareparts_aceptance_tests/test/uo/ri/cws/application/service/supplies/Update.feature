Feature: Update a supply
  As a Manager
  I want to update a supply
  to keep the prices up to date and then make the best orders for supplies

  Scenario Outline: Update an existing supply
    Given a supply
    When I update that supply to <price> € and <days> days
    Then the supply exists with price <price> € and delivery in <days> days

    Examples: 
      | price | days |
      | 100.0 |    0 |
      |   0.0 |    5 |
      |   0.0 |    0 |
      | 100.0 |    5 |

  Scenario: Try to update an non existing supply
    When I try to update a non exisitng supply
    Then an error happens with an explaining message

  Scenario: Try to update a stale supply
    Given a supply
    When I try to update the supply with wrong version
    Then an error happens with an explaining message

  Scenario Outline: Try to update with wrong parameters
    Given a supply
    When I try to update that supply to <price> and <days>
    Then an error happens with an explaining message

    Examples: 
      | price | days |
      |  -1.0 |    5 |
      | -0.01 |    1 |
      |  10.0 |   -1 |

  Scenario Outline: Try to update a supply with null dto
    When I try to update a null supply
    Then argument is rejected with an explaining message

  Scenario Outline: Try to update a supply with null provider
    Given a supply
    When I try to update that supply with null provider
    Then argument is rejected with an explaining message

 	Scenario Outline: Try to update a supply with null spare
    Given a supply
    When I try to update that supply with null spare
    Then argument is rejected with an explaining message
    