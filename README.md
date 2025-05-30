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
## Comments about the test assignment:

The general refactoring approach:
1. Ensure you understand the code.
2. Create a separate refactoring branch from the trunk (main/master/develop).
3. Ensure sufficient test coverage to run all future changes against.
4. Refactor the code in small increments (red-green refactoring), commit frequently (CI/CD),
   leave meaningful commit descriptions and optionally docs (I prefer self-documenting code over docs and comments).
5. Code review and solution refinement.


From a quick glance, I see the following points for improvement:
- Tests shouldn't be run through the Main class, but instead be moved into the separate test classes that test Services.
- There should be more than one test case and both happy and error paths should be covered. We can utilize parametrized tests.
- Break main processing into two steps, calc and format. Formatting (toString) via a separate object instead of built-in?
- Separate test for formatting (String output) and calculation part.
- Return optional result? Need to look into edge-case handling for this whole logical block.
- Separate packages and better class&method names for clear logical separation.
- A better gitignore to cover Intellij - (had to be done immediately for ease of commits).
- Most of the data objects seem to be simple immutable Data classes, so we can redefine them as Records. They don't require testing.

Most of the issues are in the RentalInfo class:
- RentalInfo is a poor name for a service class and seems to also serve as a data class, and .statement is just poor naming -> separate Classes and communicate through Interfaces (new Interface + RentalService(new ToStringFormatter + Interface))?
- HashMap with movies shouldn't be placed here and should likely be an Enum.
- String result should be a StringBuilder instead (Buffer not required because no multi-threading is present).
- The for-if loop is too messy (looks like a for-switch, actually), can be decomposed and likely optimized. Consider functional approach with a stream combined with an external function containing a switch-yield.
- The styling of if blocks is inconsistent, there are single-line ifs with no brackets, which is considered poor style.
- Need to double-check the logical flow to see if some of the steps can be merged or moved to better spots. The temp assignments in particular seem to be placed haphazardly.


TODO/If it was a "real" project and I had more time:
- Maven project with some quality of life and testing dependencies.
- Turn it into a Spring project with dependency injection instead of creating stuff in Main through new().
- Fluid validation for data objects (@NotNull being the most important)?
- Test Suits and better test syntax.
- Logging.


P.S.  
Since we aren't using Maven or Gradle, running and testing the project becomes quite straightforward! Nor do we need to Mock objects since we have no external dependencies or Beans. The lack of IoC/DI here is concerning, but for such a small project I suppose it isn't a deal-breaker.  
I don't want to complicate this assignment by adding Maven just to get JUnit Suite, RunWith, and the likes, but if it was a real project with higher size&complexity of course I'd be utilizing those along with Spring, Mockito, and maybe Lombok.  
That said, I'd be happy to demonstrate all these changes if you want me to!
