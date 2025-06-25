Feature: Find by code a spare part
  As a Manager
  I need to recover a spare part
  To see its details

  Scenario Outline: Find an existing spare part
    Given the following relation of spare parts
      | code | description  |
      | SP1  | spare-part-1 |
      | SP2  | spare-part-2 |
      | SP3  | spare-part-3 |
      | SP4  | spare-part-4 |
    When I look for a spare part by code <code>
    Then I get the spare part with code <code> and description <description>

    Examples: 
      | code  | description    |
      | "SP1" | "spare-part-1" |
      | "SP2" | "spare-part-2" |
      | "SP3" | "spare-part-3" |
      | "SP4" | "spare-part-4" |

  Scenario: Try to find a spare with non existing code
    When I try to find a non existing spare part
    Then empty is returned

  Scenario: Try to find a null spare part
    When I try to find a null spare part
    Then argument is rejected with an explaining message
