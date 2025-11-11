Feature: Pets clinic

  Scenario: Should not add petType as user
    Given I am logged in as "user"
    When I add petType to clinic
      | Name | Dog |
    Then I should be forbidden

  Scenario: Should add petType to clinic
    Given I am logged in as "admin"
    When I add petType to clinic
      | Name | Cat |
    Then I should have created petType
      | Name | Cat |
    Then I should have petTypes
      | Name |
      | Cat  |

  Scenario: Should not remove petType as user
    Given I am logged in as "admin"
    And I add petType to clinic
      | Name | Cat |
    When I am logged in as "user"
    And I remove the created petType from the clinic
    Then I should be forbidden

  Scenario: Should remove petType from  clinic
    Given I am logged in as "admin"
    And I add petType to clinic
      | Name | Dog |
    And I add petType to clinic
      | Name | Cat |
    When I remove the created petType from the clinic
    Then I should have petTypes
      | Name |
      | Dog  |
