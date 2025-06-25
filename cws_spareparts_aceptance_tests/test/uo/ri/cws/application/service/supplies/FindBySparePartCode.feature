Feature: Find supplies by spare part code
  As a Manager
  I need to recover all the supplies for a spare part 
  To see its details ans check the orders produced

  Scenario Outline: Find supplies by spare part code
    Given the following relation of supplies
      | code | nif |
      | SP1  | 123 |
      | SP1  | 456 |
      | SP1  | 789 |
      | SP2  | 123 |
      | SP2  | 456 |
      | SP3  | 123 |
    When I look for it by spare part code <code>
    Then I get a list of supplies with code <code> and nifs <nifs>

    Examples: 
      | code       | nifs            |
      | "SP1"      | "123, 456, 789" |
      | "SP2"      | "123, 456"      |
      | "SP3"      | "123"           |
      | "does-not" | ""              |

  Scenario: Try to find the supplies for a non existing spare part
    When I try to find the supplies for a non existing spare part
    Then an empty list is returned

  Scenario: Try to find the supplies for a null spare code
    When I try to find the supplies for a null spare code
    Then argument is rejected with an explaining message
