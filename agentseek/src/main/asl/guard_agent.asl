/* Initial beliefs and rules */
base_position(30,30).
/* Initial goals */

!start.

/* Plans */

+!start : true <-
    link(this);
	.print("link");
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
    .print("follow enemy");
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
    .print("alerting allies");
    .broadcast(tell, guard_remote_player_position).

+!moveRandom : not player_position(X, Y) <-
    .print("search enemy in the surroundings");
    moveRandom(random);
    .wait(500);
    !searchForPlayer.

+!returningToBase : guard_remote_player_position & base_position(X, Y) <-
    .print("returning to base");
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

+camera_remote_player_position(X, Y) : true <-
    .print("received camera remote enemy position").

+player_position(X, Y) <-
    .print("Enemy in (", X, ", ", Y, ")").
