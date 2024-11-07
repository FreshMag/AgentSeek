/* Initial beliefs and rules */
position(0,0).

/* Initial goals */

!start.

/* Plans */

+!start : true <-
    link(this);
	.print("hello world");
	!yell.

+!yell : position(X, Y) & X > 5 & Y > 5 <-
    yell("I'm going far!");
    .wait(100);
    !yell.

-!yell <-
    .wait(50);
    !yell.