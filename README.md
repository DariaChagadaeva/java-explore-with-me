# Explore with me
ExploreWithMe allows users to post events, send requests for participation, add comments and review event compilations.

The project adopts a microservices architecture, comprising a main service and a statistics service that collects statistics on event views by users. 

The API consists of three paths: 
admin - for moderation, 
private - for authorized users, 
public - for all users.

Both services follow a RESTful design and are built using Spring Boot and Maven. Each service stores its data in a separate PostgreSQL database.

# Stack
Java 11
Spring Boot
Maven
Lombock
PostgreSQL and H2
Docker

