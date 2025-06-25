Feature: Update a spare part
  As a Manager
  I want to update a spare part
  For been able to solve workorders and earn some money selling spareparts

  Scenario: Update an existing spare part
    Given a registered spare part
    When I update the spare part to new values for all its fields
    Then the spare changes all the fields except the code and the id

  Scenario: Try to update an non existing spare part
    When I try to update a non existent spare part
    Then an error happens with an explaining message

  Scenario: Try to update a stale spare part
    Given a registered spare part
    When I try to update the spare part with wrong version
    Then an error happens with an explaining message

  Scenario Outline: Try to update with wrong values
    Given a registered spare part
    When I try to update the spare part with data <description>, <price>, <stock>, <minStock>, <maxStock>
    Then an error happens with an explaining message

    Examples: 
      | description | price  | stock | minStock | maxStock |
      | "sparepart" | -100.0 |    10 |        5 |       25 |
      | "sparepart" |  100.0 |    -1 |        5 |       25 |
      | "sparepart" |  100.0 |    10 |       -1 |       25 |
      | "sparepart" |  100.0 |    10 |        5 |       -1 |

  Scenario Outline: Try to update with wrong arguments
    Given a registered spare part
    When I try to update the spare part with data <description>, <price>, <stock>, <minStock>, <maxStock>
    Then argument is rejected with an explaining message

    Examples: 
      | description | price | stock | minStock | maxStock |
      | ""          | 100.0 |    10 |        5 |       25 |

  Scenario Outline: Try to update with null spare part
    When I try to update a null spare part
    Then argument is rejected with an explaining message
