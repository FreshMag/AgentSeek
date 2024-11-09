
!start_behavior.

// Initial beliefs
+!start_behavior <-
    link(this);
    .print("Starting camera_agent behavior");
    !check_surroundings.

+!check_surroundings : not seesPlayer <-
    .print("No player in sight");
    .wait(5000);
    ?seesPlayer | .print("Still don't see the player").

// When camera_agent sees a player in front
+seesPlayer <-
    .print("Player detected");
    !alarm_agents.

// Alarm other agents with the player’s position
+!alarm_agents <-
    .print("ALARMING THE AGENTS!!!");
    !monitor_player.

// Monitoring player while it remains in sight
+!monitor_player : seesPlayer <-
    .print("Continuing to monitor player");
    .wait(1000); // Wait before next check to reduce frequency
    !monitor_player.

// If camera_agent no longer sees the player
+!monitor_player : not seesPlayer <-
    .print("Lost sight of player. Returning to initial position.");
    !return_to_initial_position.

// Action to return to the initial position if player is not seen for a while
+!return_to_initial_position <-
    .print("Returning to initial position.");
    !check_surroundings.

// If no player is seen, camera_agent turns around to look for them
+!turn_around <-
    ?not see(player);
    .print("Turning to check surroundings.");
    rotate(90); // Rotate 90 degrees or another angle
    !check_surroundings.

// Ensure camera_agent does not watch a wall
+!check_for_wall <-
    ?not see(wall); // Check there's no wall
    !turn_around.