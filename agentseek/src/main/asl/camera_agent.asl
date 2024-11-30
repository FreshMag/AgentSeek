
!start_behavior.
playerPosition(0, 0).
keep_position_time(5000).
reaction_time(1000).

+!start_behavior <-
    link(this);
    !check_surroundings.

+!check_surroundings : keep_position_time(T) <-
    .wait(seesPlayer, T);
    !alarm_agents.

-!check_surroundings : not seesPlayer <-
    !turn_around;
    !check_surroundings.

+!alarm_agents <-
    !monitor_player.

+!monitor_player : reaction_time(T) & seesPlayer & playerPosition(X, Y) <-
    .broadcast(tell, camera_remote_player_position(X, Y));
    .wait(T);
    !monitor_player.

+!monitor_player : not seesPlayer <-
    !check_surroundings.

+!turn_around <-
    io.github.agentseek.util.jason.get_random_available_angle(X);
    rotate(X).
