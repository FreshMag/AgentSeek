/* Initial beliefs and rules */
base_position(25,0).
/* Initial goals */

!start.

/* Plans */

+!start : true <-
    link(this);
	.print("link");
	!searchForEnemy.

+!searchForEnemy : enemy_position(X, Y) <-
    -enemy_lost[source(_)];
    -remote_position[source(_)];
    !alertAllies;
    !followEnemy.

+!searchForEnemy : enemy_heard(X, Y) <-
    !followEnemy.

+!searchForEnemy : base_reached <-
    !searchBase.

+!searchForEnemy : (remote_position | enemy_lost) & base_position(Z, W) & not base_reached <-
    !returningToBase.

+!searchForEnemy : not enemy_position(X, Y) <-
    !moveRandom.

+!followEnemy : enemy_position(X, Y) <-
    move(X, Y);
    .print("follow enemy");
    !searchForEnemy.

+!followEnemy : enemy_heard(X, Y) <-
    move(X, Y);
    !searchForEnemy.

+!alertAllies : enemy_position(X, Y) <-
    .print("alerting allies");
    .broadcast(tell, remote_position).

+!moveRandom : not enemy_position(X, Y) <-
    .print("search enemy in the surroundings");
    moveRandom(random);
    !searchForEnemy.

+!searchBase : <-
    defendBase.

+!returningToBase : (remote_position | enemy_lost) & base_position(X, Y) <-
    .print("returning to base");
    move(X, Y);
    !searchForEnemy.

+remote_position : true <-
    .print("received position").

+enemy_position(X, Y) <-
    .print("Enemy in (", X, ", ", Y, ")").

+enemy_lost <-
    +enemy_lost.
