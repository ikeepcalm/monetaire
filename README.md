# Monetaire - What is it?
Monetaire is a vanilla server minecraft plugin that adds a simple economy to the game. 
From now on, players will be able to check their virtual balance, replenish it with diamond ore, 
transfer funds to other players, pay fines set by the Ministry of Finance, and sponsor the Ministry of Finance 
so that it can finance global projects on the server.

# Development Stack
To create the plugin, we used the Paper API to interact with the server, 
the Command API to separate the logic of individual commands from their initialization location, and 
ORMLite to interact with the database (Hibernate or Spring Data would not work,
because the result of the dependency shading process exceeded all possible file size limits).
