# Gatling Tests

It's an application using gatling for testing performance of the KIt-Demo

## Configuration

You need to set some environment variables to run this app. Have a look at `.env` file.

###  *.env content*

```sh
GATLING_TARGET_URL=http://HOST:PORT
GATLING_RAMP_USERS=INT
GATLING_RAMP_DURATION=INT

```

You can configure tests scenarios in `src/test/scala/`.  
Ressources takes place in `src/main/resources/`.

## Running

`docker-compose up`
