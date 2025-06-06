# Refactoring Java

The code creates an information slip about movie rentals.
Rewrite and improve the code after your own liking.

Think: you are responsible for the solution, this is a solution you will have to put your name on.


## Handing in the assignment

Reason how you have been thinking and the decisions you took. 
You can hand in the result any way you feel (git patch, pull-request or ZIP-file).
Note: the Git history must be included.


## To run the test:

```
javac src/*.java
java -cp src Main
```

---
## To build and run via the jar file in the target folder:

```
mvn clean package
java -jar target\refactoring-java-1.0-SNAPSHOT.jar
```

## To build and run via Docker:

```
mvn clean package
docker build -t movie-rental-info-app .
docker run movie-rental-info-app
```

---
Please see [Task Notes](TASK_NOTES.md) and "P.S." comments for my commentary and process insights.
