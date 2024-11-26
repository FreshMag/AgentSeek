
!start.

+!start : true <-
    link(this);
	!searchForPlayer.

+!searchForPlayer : player_heard(X, Y) <-
    wait(1000);
    move(X, Y);
    !searchForPlayer.

+!searchForPlayer : not player_heard(X, Y) <-
    wait(1000);
    moveRandom;
    !searchForPlayer.