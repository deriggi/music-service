
## Running
Run the application with: ```mvn exec:java```
Doing so will:
- start a server
- create an in memory database
- fetch the data
- populate the database

Application startup happens in the Main class here
[Main.java](src/main/java/com/wurrly/Main.java)
All endpoints described here in the system generated wadl
http://localhost:8080/music/application.wadl

## Testing
``` mvn clean package```

Runs some tests for tracks and artist operations

## Domain
The  [ domain](/src/main/java/com/wurrly/domain/) has two classes, an Artist and a Track (Song). 
Tracks have knowledge of their Artist but Artists do not have knowledge of their child Track. This is a one-directonal relationship.  


## Data Loader
The data loader makes a simple get request to the json file and converts the data into the domain objects, then loads them into the database
[Loader.java](src/main/java/com/wurrly/Loader.java)

## Schema
Two tables in the database are used to manage tracks and artist with artists having many tracks. There is a foreign key in the Track table called artist_id referencing the 
primary key in the Artist

## Technology
- JDBI for data access layer
- Jersey is the rest framwework
- H2 is the in memory database
