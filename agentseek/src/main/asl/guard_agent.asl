/* Initial beliefs and rules */

/* Initial goals */

!start.

/* Plans */

+!start : true <-
    link(this);
	.print("link");
	!searchForEnemy.

+!searchForEnemy : enemy_lost & base_position(X, Y) <-
    .wait(1000);
    .print("returning to base");
    move(X, Y);
    !searchForEnemy.

+!searchForEnemy : enemy_position(X, Y)  <-
    .wait(1000);
    move(X, Y);
    .print("follow enemy");
    !searchForEnemy.

+!searchForEnemy : not enemy_position(X, Y) <-
    .wait(1000);
    .print("search enemy in the surroundings");
    moveRandom(random);
    !searchForEnemy.


+enemy_position(X, Y) <- .print("Enemy in (", X, ", ", Y, ")").
