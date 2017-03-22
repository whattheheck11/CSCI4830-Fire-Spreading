# CSCI4830-Fire-Spreading
Multi-threaded simulation of the semi-random spread of fire.
Authors: Chase Heck, Clint Olsen

Project Proposal
	For this project, we would like to investigate simulating the spread of fire using concurrency. Present models of this phenomenon are complex and difficult to model mathematically. Instead, we would like to simulate the behavior of a spreading fire using a concurrent program that take into account several factors to model an algorithm. These factors could potentially include the effects of wind, season, and precipitation. This is an interesting problem due to the fact that fire spreads in numerous directions at once and thus lends itself to a multithreaded approach. If handled linearly, this simulation would most likely be inefficient due to the large number of variables. The main difficulty with this problem will be the probabilistic factors that determine whether surrounding areas will catch fire.

