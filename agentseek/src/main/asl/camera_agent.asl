
!start_behavior.
playerPosition(0, 0).

+!start_behavior <-
    link(this);
    .print("Starting camera_agent behavior");
    !check_surroundings.

+!check_surroundings <-
    .wait(seesPlayer, 5000);
    .print("Player detected!");
    !alarm_agents.

-!check_surroundings : not seesPlayer <-
    .print("Have not seen player in 7 seconds, turning");
    .wait(500);
    !turn_around;
    !check_surroundings.

+!alarm_agents <-
    .print("ALARMING THE AGENTS!!!");
    !monitor_player.

+!monitor_player : seesPlayer & playerPosition(X, Y) <-
    .broadcast(tell, remotePlayerPosition(X, Y));
    .print("Continuing to monitor player");
    .wait(1000);
    !monitor_player.

+!monitor_player : not seesPlayer <-
    .print("Lost sight of player. Returning to initial position.");
    !check_surroundings.

+!turn_around <-
    .print("Turning to check surroundings.");
    io.github.agentseek.util.jason.get_random_available_angle(X);
    rotate(X).
