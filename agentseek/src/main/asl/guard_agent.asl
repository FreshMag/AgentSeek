/* Initial beliefs and rules */
base_position(30,30).
/* Initial goals */

!start.

/* Plans */

+!start : true <-
    link(this);
	.print("link");
	!searchForEnemy.

+!searchForEnemy : enemy_position(X, Y) <-
    -remote_position[source(_)];
    -remotePlayerPosition[source(_)];
    !alertAllies;
    !followEnemy.

+!searchForEnemy : enemy_heard(X, Y) <-
    !followEnemy.

+!searchForEnemy : remotePlayerPosition(X, Y) & not remote_position <-
    !followEnemy.

+!searchForEnemy : base_reached & remote_position <-
    !defendBase.

+!searchForEnemy : remote_position & base_position(X, Y) & not base_reached <-
    !returningToBase.

+!searchForEnemy : not enemy_position(X, Y) <-
    !moveRandom.

+!followEnemy : enemy_position(X, Y) <-
    move(X, Y);
    .wait(500);
    .print("follow enemy");
    !searchForEnemy.

+!followEnemy : enemy_heard(X, Y) <-
    move(X, Y);
    .wait(500);
    !searchForEnemy.

+!followEnemy : remotePlayerPosition(X, Y) <-
    move(X, Y);
    -remotePlayerPosition(X, Y)[source(_)];
    .wait(500);
    !searchForEnemy.

+!alertAllies : enemy_position(X, Y) <-
    .print("alerting allies");
    .broadcast(tell, remote_position).

+!moveRandom : not enemy_position(X, Y) <-
    .print("search enemy in the surroundings");
    moveRandom(random);
    .wait(500);
    !searchForEnemy.

+!returningToBase : remote_position & base_position(X, Y) <-
    .print("returning to base");
    move(X, Y);
    .wait(500);
    !searchForEnemy.

+!defendBase : base_reached & remote_position <-
    stop;
    .wait(enemy_position(X, Y), 3000);
    !searchForEnemy.

-!defendBase : not enemy_position(X, Y) <-
    -remote_position[source(_)];
    checkSurroundings;
    !searchForEnemy.

+remotePlayerPosition(X, Y) : true <-
    .print("received camera remote enemy position").

+enemy_position(X, Y) <-
    .print("Enemy in (", X, ", ", Y, ")").
