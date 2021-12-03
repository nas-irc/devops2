# README

The reservation application is an API exposing train tickets open data.
The data is stored inside a postgresql database initialized by liquibase (a migration tool).
Every image is ready to go with Docker and docker-compose.

## Quickstart

Using docker-compose:
> docker-compose up -d

This starts the database, liquibase and the backend.

## Manual start

Start only the database and liquibase:
> docker-compose up -d resa-db resa-liquibase

Run your app with your favorite IDE or manually with java:
> java -jar -Dspring.profiles.active=dev target/reservation-1.0.0.jar

Of course you will need to have your app built before running it (use maven)

NB: on the first run liquibase will initialize a LOT of data for you, this may take some time.

## API documentation

You can find a static documentation of the api here:
[api-docs.yaml](api-docs.yaml)

You can check it out on swagger editor (just copy paste its content in the editor):
[https://editor.swagger.io/](https://editor.swagger.io/)

An open api documentation is embedded in your application:
[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## API usage

Here are some use cases of the reservation application.

### Search a train ticket

Use the search endpoint to find a train ofr a specific date:
> curl --location --request GET 'http://localhost:8080/api/travels/search?from=StopArea:OCE87723197&to=StopArea:OCE87686006&date=2021-12-10'

Here are a couple stations id:
| Station | Id  |
| :---:   | :-: |
| Gare de Lyon-Part-Dieu | StopArea:OCE87723197 |
| Gare de Paris-Gare-de-Lyon | StopArea:OCE87686006 |
| Gare de Marseille-St-Charles | StopArea:OCE87751008 |
| Gare de Bordeaux-St-Jean | StopArea:OCE87581009 |

You can find almost all train stations (and more) in the table: *stops*

### Get technical information on a train

With a train reference you can get information on the train with:
> curl -X 'GET' 'http://localhost:8080/api/trains/12'

### Book a ticket

Knowing a travel id you can book a ticket like this:
> curl --location --request POST 'http://localhost:8080/api/bookings' \
--header 'Content-Type: application/json' \
--data-raw '{
"travelId": 3741,
"seatBookingRequests": [
{
"seatId": 2,
"firstName": "Jacques",
"lastName": "Uzi",
"birthdate": "1998-07-17",
"email": "email",
"phoneNumber": "string"
}
],
"date": "2021-12-10",
"stationA": "StopArea:OCE87723197",
"stationB": "StopArea:OCE87686006"
}'

### And more

Explore the API documentation and play with the API:
* create trains
* change ticket prices
* get booking invoices
