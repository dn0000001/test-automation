Feature: True North Hockey Canada BDD Test

  Scenario: Perform Search
    Given Using the dataSet from the "data/ui/TNHC_TestData.xml" file for the True North Hockey Canada Test
    And I am on the True North Hockey Canada website
    When I enter the search fields
    Then I can perform a search
