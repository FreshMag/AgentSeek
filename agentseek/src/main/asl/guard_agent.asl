/* Initial beliefs and rules */
base_position(15,0).
/* Initial goals */

!start.

/* Plans */

+!start : true <-
    link(this);
	.print("link");
	!searchForEnemy.

+!searchForEnemy : enemy_position(X, Y) <-
    -enemy_lost;
    -remote_position;
    !alertAllies;
    !followEnemy.

+!searchForEnemy : (remote_position | enemy_lost) & base_position(Z, W) <-
    !returningToBase.

+!searchForEnemy : enemy_heard(X, Y) <-
    wait(1000);
    !followEnemy.

+!searchForEnemy : not enemy_position(X, Y) <-
    !moveRandom.

+!followEnemy : enemy_position(X, Y) <-
    .wait(500);
    move(X, Y);
    .print("follow enemy");
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
    !searchForEnemy.

+!baseReached: base_reached <-
    -enemy_lost;
    -remote_position;
    .wait(500);
    !searchForEnemy.

+base_reached : true <-
    -enemy_lost;
    -remote_position.

+remote_position : true <-
    .print("received position").

+enemy_position(X, Y) <-
    .print("Enemy in (", X, ", ", Y, ")").
