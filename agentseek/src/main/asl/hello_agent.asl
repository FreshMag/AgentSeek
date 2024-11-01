/* Initial beliefs and rules */

/* Initial goals */

!start.

/* Plans */

+!start : true <-
    link(this);
	.print("hello world");
	yell("Action!").