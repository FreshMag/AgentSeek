/* Initial goals */

!start.

/* Plans */

+!start : true <-
    link(this);
	.print("link");
	!think.

+!think : enemy_heard(X, Y) <-
    wait(1000);
    move(X, Y);
    !think.

+!think : not enemy_heard(X, Y) <-
    wait(1000);
    moveRandom(random);
    !think.