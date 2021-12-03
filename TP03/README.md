**GALDEANO Nasri**  
**PROTIÈRE Axel**

**5IRC**
# **TP 3 - Load tests** (incomplete)

## Setting up everything

Lancer d'abord les bases de données en se plaçant `./db/` puis: `docker-compose up -d`. Attendre que Liquibase finisse de ...

Ajouter jaeger : 
~~~
  jaeger:
    image: jaegertracing/opentelemetry-all-in-one
    ports:
        - "16686:16686"
    environment:
        - JAEGER_AGENT_HOST=jaeger
    networks:
        - db_resa_net
    restart: on-failure
~~~

Lancer ensuite l'app (utiliser `--build` pour rebuild à chaque fois)

## First Tests

<span style="color:red">**1)** What are those parameters used for ? </span>

 - nbUsers : C'est le nombre de requêtes envoyées pour le test.
 - rampDuration : C'est le temps du test de charge.

## Results

<span style="color:red">**2)** Why are the results improving with the number of times I run the tests ? </span>

Car comme vu dans le TP01, la compilation à la volée permet de mieux gérer certains aspects de la compilatio. Il y a aussi la mise en cache de certaines données en base de données.

<span style="color:red">**3)** What can companies do to both absorb those previsible spikes
while limiting their costs ? </span>

Les entreprises peuvent utiliser de l'autoscaling schedulé grace à AWS par exemple.

## Swarm


<span style="color:red">**4)** What is the difference between "limits" and "reservations" ? </span>

 - Limits = Memeoire que chaque service ne peut pas dépasser.
 - Reservations = Réservation de mémoire hôte pour l'ensemble de la stack

## Maintaining your system

## Further testing

## JVM tuning

<span style="color:red">**5)** Explain what those two parameters are used for. </span>

 - Xms = Mémoire heap maximale réservée lors du démarrage de la JVM
 - Xmx = Mémoire heap maximale réservée lors de l'exécution de la JVM

## DB query tuning

<span style="color:red">**6)** Explain what is a N+1 problem. Why is it happening in our case
? </span>

<span style="color:red">**7)** What is EAGER fetchtype ? What's the difference with LAZY
fetchtype ? </span>

LAZY = fetch quand on le veut
EAGER = fetch immédiat