Feature: Find by description a spare part
  As a Manager
  I need to recover the spare parts that match a description
  To see its details

  Scenario Outline: Find a spare parts by description
    Given the following relation of spare parts
      | code | description  |
      | SP1  | spare-part-1 |
      | SP2  | spare-part-1 |
      | SP3  | spare-part-2 |
      | SP4  | spare-part-2 |
      | SP5  | spare-part-3 |
      | SP6  | spare-part-4 |
    When I look for spare part reports by description <description>
    Then I get <quantity> spare parts with description <description> and codes <codes>

    Examples: 
      | description      | quantity | codes      |
      | "spare-part-1"   |        2 | "SP1, SP2" |
      | "spare-part-2"   |        2 | "SP3, SP4" |
      | "spare-part-3"   |        1 | "SP5"      |
      | "spare-part-4"   |        1 | "SP6"      |
      | "does-not-exist" |        0 | ""         |

  Scenario: Try to find a spare part report with non existing description
    When I try to find a spare part report with non existing description
    Then an empty list is returned

  Scenario: Try to find a spare part report with null description
    When I try to find a spare part report with null description
    Then argument is rejected with an explaining message
