Feature: Delete a SparePart
	As a Manager
	I want to delete a spare part
	in case have not been used to keep the system clean

  Scenario: Delete an existing spare part
    Given a registered spare part
    When I remove the spare part
    Then the spare part gets removed from the system

  Scenario: Try to remove a non existing spare part
		When I try to remove a non existent spare part
		Then an error happens with an explaining message
  
  Scenario: Try to remove a used spare part with order lines
    Given a registered spare part that has order lines
		When I try to remove the spare part
		Then an error happens with an explaining message

	Scenario: Try to remove a used spare part with suppliers
    Given a registered spare part that has suppliers
		When I try to remove the spare part
		Then an error happens with an explaining message

	Scenario: Try to remove a null spare part
		When I try to remove a null spare part
		Then argument is rejected with an explaining message
		