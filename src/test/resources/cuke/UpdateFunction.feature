Feature: Choonz website

Background: 
Given I have logged in and am on the homepage

Scenario Outline: 
	When I navigate to the update page for <entity>
	Then I will input the new details to update record(s) for <entity2>
	
	Examples:
    |  entity  |  entity2  |
    |  tracks  |   tracks  |
    |  genres  |   genres  |
    |  artists |  artists  |
    |  album   |   albums  |
    | playlist | playlists |