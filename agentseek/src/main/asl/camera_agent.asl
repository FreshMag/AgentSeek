
!start_behavior.

// Initial beliefs
+!start_behavior <-
    link(this);
    .print("Starting camera_agent behavior");
    !check_surroundings.

+!check_surroundings : not seesPlayer <-
    .wait(seesPlayer, 7000);
    .print("Player detected!");
    !alarm_agents.

-!check_surroundings : not seesPlayer <-
    .print("Have not seen player in 7 seconds, turning");
    !turn_around;
    !check_surroundings.

+!alarm_agents <-
    .print("ALARMING THE AGENTS!!!");
    !monitor_player.

+!monitor_player : seesPlayer <-
    .print("Continuing to monitor player");
    .wait(1000);
    !monitor_player.

+!monitor_player : not seesPlayer <-
    .print("Lost sight of player. Returning to initial position.");
    --seePlayer;
    !check_surroundings.

+!turn_around <-
    .print("Turning to check surroundings.");
    io.github.agentseek.util.jason.get_random_available_angle(X);
    rotate(X).

// Ensure camera_agent does not watch a wall
+!check_for_wall <-
    ?not see(wall); // Check there's no wall
    !turn_around.