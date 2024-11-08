
seesPlayer(no).
angle(0).

!start.

+!start : true <-
    link(this);
    .print("Camera is watching");
    !watch.

+!watch : seesPlayer(X) <-
    .print("Still watching");
    .wait(500);
    .print(X);
    !watch.

-!watch : seesPlayer(yes) <-
    !alarm.

+!alarm : seesPlayer(yes) <-
    .print("I see the player!!!").