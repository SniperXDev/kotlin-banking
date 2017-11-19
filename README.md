Axon Banking
---
My attempt at following the Axon Banking live coding tutorial found at youtube.

I am attempting to to the tutorial in Kotlin with Spring Boot 2.0 and Gradle build.
All of which are new or new versions to me. 

Videos found here:
* https://www.youtube.com/watch?v=s2zH7BsqtAk
* https://www.youtube.com/watch?v=Fj365BufWNU
* https://www.youtube.com/watch?v=qqk2Df_0Pm8

To run:
* In memory db: start BankingApplication.kt
* Persistent db:
  * start h2 (with script h2 folder)
  * start BankingApplication.kt with -Dspring.profiles.active=h2

TODO
* Make more Kotlin idiomatic
* Add more tests

