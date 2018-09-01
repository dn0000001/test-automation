Feature: Soap Holiday Test

  Scenario: Get the date of a specific holiday
    Given Using the dataSet from the "data/api/SoapGetHolidayDateTestData.xml" file for the Soap Holiday Test
    When I send the request
    Then I receive the proper status and response
