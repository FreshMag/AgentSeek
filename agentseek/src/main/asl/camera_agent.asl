
!start_behavior.
playerPosition(0, 0).

+!start_behavior <-
    link(this);
    !check_surroundings.

+!check_surroundings <-
    .wait(seesPlayer, 5000);
    !alarm_agents.

-!check_surroundings : not seesPlayer <-
    .wait(500);
    !turn_around;
    !check_surroundings.

+!alarm_agents <-
    !monitor_player.

+!monitor_player : seesPlayer & playerPosition(X, Y) <-
    .broadcast(tell, camera_remote_player_position(X, Y));
    .wait(1000);
    !monitor_player.

+!monitor_player : not seesPlayer <-
    !check_surroundings.

+!turn_around <-
    io.github.agentseek.util.jason.get_random_available_angle(X);
    rotate(X).
