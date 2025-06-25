Feature: Find a provider by nif
	As a Manager
	I need to recover a provider by nif
	To see its details

  Scenario Outline: Find an existing provider
    Given the following relation of providers
      | nif | name       | email      |
      | 123 | provider-1 | u@p123.com |
      | 456 | provider-1 | u@p456.com |
      | 789 | provider-2 | u@p789.com |
      | ABC | provider-3 | u@pABC.com |
	  When I look for a provider by nif <nif>
    Then I get a provider with nif <nif> and name <name>

		Examples:
    	| nif  	| name         |
    	| "123" | "provider-1" |
    	| "456" | "provider-1" |
    	| "789" | "provider-2" |
    	| "ABC" | "provider-3" |

  Scenario: Try to find a provider with non existing nif
		When I try to find a non existent provider
		Then empty is returned
