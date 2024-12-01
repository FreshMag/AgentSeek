
!start.
reaction_time(1000).

+!start : true <-
    link(this);
	!searchForPlayer.

+!searchForPlayer : reaction_time(T) & player_heard(X, Y) <-
    wait(T);
    move(X, Y);
    !searchForPlayer.

+!searchForPlayer : reaction_time(T) & not player_heard(X, Y) <-
    wait(T);
    moveRandom;
    !searchForPlayer.