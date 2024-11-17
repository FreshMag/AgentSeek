/* Initial goals */

!start.

/* Plans */

+!start : true <-
    link(this);
	.print("link");
	!searchForPlayer.

+!searchForPlayer : player_heard(X, Y) <-
    wait(1000);
    move(X, Y);
    !searchForPlayer.

+!searchForPlayer : not player_heard(X, Y) <-
    wait(1000);
    moveRandom(random);
    !searchForPlayer.