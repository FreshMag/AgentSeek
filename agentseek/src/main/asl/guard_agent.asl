defend_base_time(3000).
base_position(30,30).

!start.

+!start : true <-
    link(this);
	!searchForPlayer.

+!searchForPlayer : player_position(X, Y) <-
    -guard_remote_player_position[source(_)];
    -camera_remote_player_position[source(_)];
    !alertAllies;
    !followEnemy.

+!searchForPlayer : enemy_heard(X, Y) <-
    !followEnemy.

+!searchForPlayer : camera_remote_player_position(X, Y) & not guard_remote_player_position <-
    !followEnemy.

+!searchForPlayer : base_reached & guard_remote_player_position <-
    !defendBase.

+!searchForPlayer : guard_remote_player_position & base_position(X, Y) & not base_reached <-
    !returningToBase.

+!searchForPlayer : not player_position(X, Y) <-
    !moveRandom.

+!followEnemy : player_position(X, Y) <-
    move(X, Y);
    !searchForPlayer.

+!followEnemy : enemy_heard(X, Y) <-
    move(X, Y);
    !searchForPlayer.

+!followEnemy : camera_remote_player_position(X, Y) <-
    move(X, Y);
    -camera_remote_player_position(X, Y)[source(_)];
    !searchForPlayer.

+!alertAllies : player_position(X, Y) <-
    .broadcast(tell, guard_remote_player_position).

+!moveRandom : not player_position(X, Y) <-
    moveRandom;
    !searchForPlayer.

+!returningToBase : guard_remote_player_position & base_position(X, Y) <-
    move(X, Y);
    !searchForPlayer.

+!defendBase : defend_base_time(T) & base_reached & guard_remote_player_position <-
    stop;
    .wait(player_position(X, Y), T);
    !searchForPlayer.

-!defendBase : not player_position(X, Y) <-
    -guard_remote_player_position[source(_)];
    checkSurroundings;
    !searchForPlayer.
