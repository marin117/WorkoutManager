# WorkoutManager

Application for logging workout routines and sharing it with others.

## Dependencies

- Crystal for running server side
- Postgres database

## Installation and running
Install Postgres database and import database models from **server/database/dump.sql** and add inital data from **exercise_insert.sql**.

- Follow the [instructions](https://crystal-lang.org/docs/installation/) to install Crystal lang
- Navigate to directory WorkoutManager/server and run command to install dependencies for Crystal:
 ```crystal deps ```
- Run server with command:
```crystal src/server.cr```

It is advisable to run application in emulator because there is no real backend server. 
