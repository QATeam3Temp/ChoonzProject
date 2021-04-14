Feature: Choonz website

  Background: 
    Given I have logged and am on the homepage

  Scenario Outline: 
    When I navigate to the delete page for <entity>
    Then I will select the record to delete a <entity2>

    Examples: 
      | entity   | entity2  |
      | tracks   | track    |
      | genres   | genre    |
      | artists  | artist   |
      | album    | album    |
      | playlist | playlist |
