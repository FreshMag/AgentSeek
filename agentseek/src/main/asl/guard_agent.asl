/* Initial beliefs and rules */

/* Initial goals */

!start.

/* Plans */

+!start : true <-
    link(this);
	.print("link");
	!searchForEnemy.

+!searchForEnemy : enemy_lost & base_position(X, Y) <-
    !returningToBase.

+!searchForEnemy : enemy_position(X, Y) <-
    !alertAllies;
    !followEnemy.

+!searchForEnemy : not enemy_position(X, Y) <-
    !moveRandom.

+!followEnemy : enemy_position(X, Y) <-
    .wait(500);
    move(X, Y);
    .print("follow enemy");
    !searchForEnemy.

+!moveRandom : not enemy_position(X, Y) <-
    .wait(500);
    .print("search enemy in the surroundings");
    moveRandom(random);
    !searchForEnemy.

+!returningToBase : (remote_position(X, Y) | enemy_lost) & base_position(X, Y) <-
    .wait(500);
    .print("returning to base");
    move(X, Y);
    !searchForEnemy.

+!alertAllies : enemy_position(X, Y) <-
    .print("ALARMING");
    .assert(remote_position(X, Y));
    .broadcast(tell, remote_position(X, Y));
    .retract(remote_position(X, Y)).

/* Plan to handle received broadcast message */
+!remote_position_message : received(remote_position(X, Y)) <-
    .print("RECEIVED REMOTE: (", X, ", ", Y, ")");
    .assert(remote_position(X, Y)).

+enemy_position(X, Y) <-
    .print("Enemy in (", X, ", ", Y, ")").
