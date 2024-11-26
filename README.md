<p align="center"><img width=60% src="resources/img/logo.png"></p>


&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
&nbsp;&nbsp;&nbsp;&nbsp;
[![Language][kotlin-shield]][kotlin-url]
[![Language][jason-shield]][jason-url]
[![MIT License][license-shield]][license-url]
[![Conventional Commits][conventional-commits-shield]][conventional-commits-url]

## Overview

Project for Intelligent Systems Engineering course a.y. 2023-2024.

*Agent Seek* is a simple game where the player moves in a 2D environment, seeking the exit that
leads to the next level. The player needs to avoid the agents that are trying to catch him.

<p align="center"><img width=30% src="resources/gifs/basic_structure.gif"></p>

The game features three types of agents, each with distinct behaviors:
1. **Camera Agent** (_Gray_)
   - Stationary but rotates to detect the player in its field of view.
   - Alerts all other agents if it spots the player.
2. **Hearing Agent** (_Red_)
   - Moves through the environment, guided solely by sound.
   - Chases noise sources created by the player.
3. **Guard Agent** (_Blue_)
   - Actively patrols and can see and hear the player.
   - Upon detecting the player, it:
     - Starts chasing immediately.
     - Alerts nearby guards to protect the exit.
     - Responds to alerts from the Camera Agent.

<p align="center"><img width=30% src="resources/gifs/player_spotted.gif"></p>

Player Controls:
- _Movement_: Use W, A, S, D to move.
- _Sneak_: Hold SHIFT to move slower and more silently.
- _Distraction_: Click near your position (within a short range) to create noise and lure agents away from your path.

<p align="center"><img width=30% src="resources/gifs/generating_noise.gif"></p>

## Installation

### Prerequisites:

- Ensure you have Java installed (version 17 or later recommended).

### Steps to Run:

1.	Download the latest release JAR file from the Releases page.
2.  Open a terminal or command prompt and run the following command:

```shell
java -jar agentseek-<VERSION>.jar
```

## Credits

This project was developed as part of the Intelligent Systems Engineering course (Academic Year 2023–2024).

Feel free to report any issues or suggest improvements via the Issues page.

<!--
***
    GITHUB SHIELDS VARIABLES
***
-->

[kotlin-shield]: https://img.shields.io/badge/Kotlin-7F52FF?style=flat&logo=Kotlin&logoColor=white

[kotlin-url]: https://kotlinlang.org/

[jason-shield]: https://img.shields.io/badge/Jason-AFAFAF

[jason-url]: https://jason-lang.github.io/

[license-shield]: https://img.shields.io/github/license/FreshMag/AgentSeek.svg?style=flat

[license-url]: https://github.com/FreshMag/AgentSeek/blob/master/LICENSE

[conventional-commits-shield]: https://img.shields.io/badge/Conventional%20Commits-1.0.0-%23FE5196?logo=conventionalcommits

[conventional-commits-url]: https://conventionalcommits.org
