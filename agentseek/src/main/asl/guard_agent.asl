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

+!returningToBase : enemy_lost & base_position(X, Y) <-
    .wait(500);
    .print("returning to base");
    move(X, Y);
    !searchForEnemy.

+enemy_position(X, Y) <- .print("Enemy in (", X, ", ", Y, ")").
