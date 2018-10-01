# github-Searcher

written by Robert Schardt 2018

## How to run the project

1. clone this repository and extract it
2. install sbt (https://www.scala-sbt.org/)
3. navigate into project-root-dir and type "sbt run" to start the server
4. you could also use "sbt test" to see the test-results
5. type "http://localhost:8080/search/language" in your browser to start the search
Note: instead of the string "language" you can type in any programming language you want to search for

## How to run the project as Docker-Container

1. navigate into project-root-dir
2. make sure you have docker and sbt installed
3. run "sbt docker" to re/create the dockerfile 
Note: the dockerfile can be also found under "target/docker/Dockerfile"
4. run "docker run -p 8080:8080 githubsearcher/githubsearcher"
5. type "http://localhost:8080/search/language" in your browser to start the search
Note: instead of the string "language" you can type in any programming language you want to search for

## The task
- Create a service which provides a single API endpoint to search for GitHub users by the programming language they use in their public repositories.
- Each user returned in the response of the search request should at least contain the username, name, avatar url and number of followers.
- Use the GitHub APIs (https://developer.github.com/v3/) to retrieve the information.
- The service should be developed with a technology that you feel comfortable with. Feel free to use the libraries you find suitable for this task.
- The service should be covered with tests you find suitable for this task.
- Create a Dockerfile to run the service.
- Please use Git for this project. When you're done, compress your local repository as a zip archive and attach it to the email.
- The goal isn't to build a ready product, but rather to get an idea of your skills. Most people spent no longer than four hours.

