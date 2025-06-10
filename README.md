This is a simple Todo app that allows the user to manage a list of tasks. It's a hobby project where I can practice various technical concepts, such as CI/CD and multi-level testing, but not only.

---

Ceci est une application Todo simple qui permet à l'utilisateur de gérer une liste de tâches. C’est un projet personnel où je peux pratiquer divers concepts techniques, comme le CI/CD et les tests à plusieurs niveaux.

---

# Project information

## Tech Stack (in its current form):
- Java 17
  - Spring
  - Maven
- Angular 19
  - PrimeNG (UI)
  - Tailwind CSS
- Docker
- Nginx (as docker image)
- PostgreSQL (as docker image)
- GitHub Actions (CI/CD)

## Build project

### Backend

*Requires Java 17 and Maven*

`mvn clean package`

### Frontend

*Requires node, npm and Angular CLI (see angular.dev)* 

`npm ci`

`npm run build`

## Developpement mode

### Backend

Start Spring application (port 8080)

`mvn spring-boot:run "-Dspring-boot.run.profiles=dev"`

### Frontend

Start Angular application (port 4200)

`npm run start`

## Production mode

*Requires Docker* 

In project root

`docker compose up -d`

The applicaton should be running on port 1337, this can be changed in docker-compose.yaml

*It should work without Docker but instructions won't be provided here*
