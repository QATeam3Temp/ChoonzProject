Feature: Choonz website

Background: 
Given I have accessed the homepage

Scenario Outline: 
	When I navigate to the create page for <entity>
	Then I will input the field to create a <entity2>
	
	Examples:
    |  entity  | entity2  |
    |  tracks  |   track  |
    |  genres  |   genre  |
    |  artists |  artist  |
    |  album   |   album  |
    | playlist | playlist |