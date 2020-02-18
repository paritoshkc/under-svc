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
  - Run directly: `java -jar build/undersvc-0.0.1-all.jar server config.yml`
  - Or run via `docker-compose`: `docker-compose up --build --detach`
3. Visit `http://localhost:8081/healthcheck` to verify the application is running and healthy.


Note: If you get an error while loading on the step-2 then check the config.yml file for the database and use the one which is not being used while commenting the one that is in use.

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
$ curl http://localhost:8080/groups
[{"groupState":"Forming","points":"MULTIPOINT ((37.516455 126.721757))","memberUUIDs":["64656164-6265-6566-2d64-6561642d6265"],"createTime":0,"depTime":1000,"restrictions":{"MaxPeople":3}}]
```

To create a new group:
```bash
curl -XPOST http://$HOST:$PORT/groups -H 'Content-Type: application/json' --data '$GROUP_JSON'
```

Example:
```bash
$ curl -XPOST http://localhost:8080/groups \
    -H 'Content-Type: application/json' \
    --data '{"groupState":"Forming","points":"MULTIPOINT ((37.516455 126.721757))","memberUUIDs":["64656164-6265-6566-2d64-6561642d6265"],"createTime":0,"depTime":1000,"restrictions":{"MaxPeople":3}}'

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
