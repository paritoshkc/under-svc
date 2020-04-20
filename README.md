# undersvc

`undersvc` is a backend service for [under](https://gitlab.com/ase-under/under).
It's primary function is to act as a global source of truth for:
* user ratings
* travel group status

Information is served as JSON over a REST API.

How to start the undersvc application
---

1. Run `./gradlew shadowJar` for Linux and 'gradlew.bat shadowJar' for Windows to build  an uber-JAR containing all dependencies.
2. Either:
  - Run directly: `java -jar build/libs/undersvc-0.0.1-all.jar server config.yml`
  - Or run via `docker-compose`: `docker-compose up --build --detach`
3. Visit `http://localhost:8081/healthcheck` to verify the application is running and healthy.


Note: If you get an error while loading on step-2 then check the config.yml file for the database and use the one which is not being used while commenting the one that is in use.

Deployment
---

Builds and deployment are done automatically via Gitlab CI (see: `.gitlab-ci.yml`).

The application is run in Google Kubernetes Engine (see: `deployment.yml`).

Production secrets are stored in Kubernetes and mounted as a volume at runtime (again, see `deployment.yml`).

All you need to do is `git push origin master` and everything should happen automatically.

Talking to it
---
To list active groups:
```bash
curl http://$HOST:$PORT/groups
```

Example:
```bash
$ curl http://localhost:8080/groups \
[{"groupState":"Forming","points":"MULTIPOINT ((37.516455 126.721757))","memberUUIDs":["64656164-6265-6566-2d64-6561642d6265"],"createTime":0,"depTime":1000,"restrictions":{"MaxPeople":3}}]
```

To create a new group:
```bash
curl -XPOST http://$HOST:$PORT/groups -H 'Content-Type: application/json' --data '$GROUP_JSON'
```

Example for Creating a new group (Use curl commands)
```bash
$ curl -XPOST http://localhost:8080/groups \
    -H 'Content-Type: application/json' \
    --data '{"groupState":"Forming","points":"MULTIPOINT ((37.516455 126.721757))","memberUUIDs":["64656164-6265-6566-2d64-6561642d6265"],"createTime":0,"depTime":1000,"restrictions":{"MaxPeople":3}}'

curl -XPOST http://localhost:8080/groups \
    -H 'Content-Type: application/json' \
    --data '{"groupState":"Forming","points":"MULTIPOINT ((30.23 12.77))","memberUUIDs":["deadbeef-cafe-babe-dead-deadcafebabe"],"createTime":0,"depTime":1000,"restrictions":{"MaxPeople":3}}'

```
Example for getting a group by ID
```bash
$ curl http://localhost:8080/groups/1
```

Example for updating a group by ID (added 2 members in groupID-1)
```bash
$ curl -XPOST http://localhost:8080/groups/1 \
    -H 'Content-Type: application/json' \
    --data '{"groupState":"Forming","points":"MULTIPOINT ((37.516455 126.721757))","memberUUIDs":["64656164-6265-6566-2d64-6561642d6265","deadbeef-cafe-babe-dead-deadcafebabe","dead1728-1111-2222-3333-deadcafebabe"],"createTime":0,"depTime":1000,"restrictions":{"MaxPeople":3}}'

```

Example for deleting a group by ID (deleting groupID-2)
```bash
$ curl http://localhost:8080/groups/2
$ curl -XDELETE http://localhost:8080/groups/2
$ curl http://localhost:8080/groups/2
```

Example User RATINGS for inserting:
```
$ curl -XPOST http://localhost:8080/ratings/64656164-6265-6566-2d64-6561642d6265?rating=2

$ curl -XPOST http://localhost:8080/ratings/64656164-6265-6566-2d64-6561642d6265?rating=4

```
Example User RATINGS for getting
```
$ curl http://localhost:8080/ratings/64656164-6265-6566-2d64-6561642d6265

```


Example:
You can check on the browser by hitting http://localhost:8080/groups to check if the Json loads on the browser.

Database Stuff
---

To create the database locally, run:

```bash
$ java -jar undersvc.jar db migrate config.yml
```

Currently you need to do the same thing in production manually if changes need to be made to the schema. This could possibly be automated by adding an [init container](https://kubernetes.io/docs/concepts/workloads/pods/init-containers/) to the Kubernetes deployment manifest.
