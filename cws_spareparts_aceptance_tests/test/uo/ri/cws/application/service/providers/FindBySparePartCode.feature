Feature: Find a provider by spare parts supplied
  As a Manager
  I need to know what providers can supply a spare part
  To see its details and then check the orders generated

  Scenario Outline: Find an existing provider by supplied spare part code
    Given the following relation of supplies code-nif
      | code | nif |
      | SP1  | 123 |
      | SP1  | 456 |
      | SP1  | 789 |
      | SP2  | 123 |
      | SP2  | 456 |
      | SP3  | 123 |
    When we look for providers by spare part code <code>
    Then we get a list of <quantity> providers with nifs <nifs>

    Examples: 
      | code  | quantity | nifs            |
      | "SP1" |        3 | "123, 456, 789" |
      | "SP2" |        2 | "123, 456"      |
      | "SP3" |        1 | "123"           |
