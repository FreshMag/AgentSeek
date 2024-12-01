
# [1.0.3] - 2024-11-26
Improvements to game components and agent behaviors. 

## New Components
- Implemented a component to exit the game on victory and game over screens
- Implemented a component responsible for emitting noise when the player clicks on the screen.
- Implemented a component that triggers a  when the player collides with an enemy object.

## Agent Behavior Improvements:
- Refined the behavior of the camera agent by removing print statements and optimizing the monitoring logic.
- Enhanced the guard agent's behavior by cleaning up print statements and improving the logic for following enemies and alerting allies.
- Updated the hearing agent's plans to streamline the search for the player.

## Documentation
- Added missing documentation
- Enriched the project's README.md


# [1.0.2] - 2024-11-18
## Features
### New Agents:
* : Added new behavior plans for the camera agent, including , , , and .
* : Introduced new behavior plans for the guard agent, such as , , , , and .
* : Added behavior plans for the hearing agent, including  and .

### Enhancements to Sensor Components:
* : Enhanced the  to include new functionalities such as adding reactions and updating the visual representation of the noise sensor.
* : Improved the  by adding new methods to rotate and set the direction of the sensor, and enhanced the detection logic to filter out blacklisted names.

## Refactoring and Code Improvements
* : Refactored the  to improve readability and performance by filtering out objects named Player from the collision detection.
* : Added new methods to control the movement direction and state of the , and adjusted the maximum velocity.
* : Modified the  to react to noise detections and update the visual feedback accordingly.

### New Utility Functions:
* : Added a new utility function  to calculate a random velocity for game objects based on their surroundings.


# [1.0.1] - 2024-11-07
## Fixes
- Fixed CI not pushing also on the source branch


# [1.0.0] - 2024-11-07
## First release
- Implementation of a simple game engine 
- Implementation of a simple Swing view 
- Added support for Jason agents

