Feature: Choonz website

Background: 
Given I have logged in and am on the homepage

Scenario Outline: 
	When I navigate to the read page for <entity>
	Then I will display the record(s) for <entity2> on the page
	
	Examples:
    |  entity  |  entity2  |
    |  tracks  |   tracks  |
    |  genres  |   genres  |
    |  artists |  artists  |
    |  album   |   albums  |
    | playlist | playlists |