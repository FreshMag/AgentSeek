
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
    .wait(500);
    !searchForPlayer.

+!followEnemy : enemy_heard(X, Y) <-
    move(X, Y);
    .wait(500);
    !searchForPlayer.

+!followEnemy : camera_remote_player_position(X, Y) <-
    move(X, Y);
    -camera_remote_player_position(X, Y)[source(_)];
    .wait(500);
    !searchForPlayer.

+!alertAllies : player_position(X, Y) <-
    .broadcast(tell, guard_remote_player_position).

+!moveRandom : not player_position(X, Y) <-
    moveRandom;
    .wait(500);
    !searchForPlayer.

+!returningToBase : guard_remote_player_position & base_position(X, Y) <-
    move(X, Y);
    .wait(500);
    !searchForPlayer.

+!defendBase : base_reached & guard_remote_player_position <-
    stop;
    .wait(player_position(X, Y), 3000);
    !searchForPlayer.

-!defendBase : not player_position(X, Y) <-
    -guard_remote_player_position[source(_)];
    checkSurroundings;
    !searchForPlayer.
