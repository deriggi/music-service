
## Domain
The  [ domain](/src/main/java/com/wurrly/domain/) has two classes, an Artist and a Track (Song). The data model can be retrieved as a one-directional model with
Tracks having knowledg of their Artist. Bi-directioanal model, in which the Artsts could be implemented however fetching artists 
with many tracks could be an expensive data fetch for bigger fetch with prolific artsts

## Data Loader
A data loader makes a simple get request to the json file and converts the data into the domain objects, then loads them into the database
[Loder.java](Loader.java)

## Shchema
Two tables, tracks and artist with artistss having many tracks. There is a foreign key in the Track table called artist_id referencing the 
primary key in the Artist
