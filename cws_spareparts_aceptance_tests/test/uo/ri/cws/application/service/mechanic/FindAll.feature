Feature: Find all mechanics
  As a Manager
  I need to know data about all mechanics

  Scenario Outline: Find all existing mechanic
    Given the following relation of mechanics
      | nif   | name   | surname   |
      | NIF-1 | Name-1 | Surname-1 |
      | NIF-2 | Name-2 | Surname-2 |
      | NIF-3 | Name-3 | Surname-3 |
    When we read all mechanics
    Then we get the following list of mechanics
      | mechanic               |
      | NIF-1,Name-1,Surname-1 |
      | NIF-2,Name-2,Surname-2 |
      | NIF-3,Name-3,Surname-3 |
