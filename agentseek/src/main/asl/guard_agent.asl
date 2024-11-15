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
    -enemy_los[source(_)];
    -remote_position[source(_)];
    !alertAllies;
    !followEnemy.

+!searchForEnemy : enemy_heard(X, Y) <-
    !followEnemy.

+!searchForEnemy : (remote_position | enemy_lost) & base_position(Z, W) <-
    !returningToBase.

+!searchForEnemy : not enemy_position(X, Y) <-
    !moveRandom.

+!followEnemy : enemy_position(X, Y) <-
    .wait(500);
    move(X, Y);
    .print("follow enemy");
    !searchForEnemy.

+!followEnemy : enemy_heard(X, Y) <-
    .wait(500);
    move(X, Y);
    !searchForEnemy.

+!alertAllies : enemy_position(X, Y) <-
    .print("alerting allies");
    .broadcast(tell, remote_position).

+!moveRandom : not enemy_position(X, Y) <-
    .wait(500);
    .print("search enemy in the surroundings");
    moveRandom(random);
    !searchForEnemy.

+!returningToBase : (remote_position | enemy_lost) & base_position(Z, W) <-
    .wait(500);
    .print("returning to base");
    move(Z, W);
    !baseReached;
    !searchForEnemy.

+!baseReached: base_reached <-
    -enemy_lost[source(_)];
    -remote_position[source(_)].

-!baseReached <-
    .print("not reached yet").

+remote_position : true <-
    .print("received position").

+enemy_position(X, Y) <-
    .print("Enemy in (", X, ", ", Y, ")").

+enemy_lost <-
    +enemy_lost.
