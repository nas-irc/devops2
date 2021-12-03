**GALDEANO Nasri**  
**PROTIÈRE Axel**

**5IRC**
# **TP 1 - Prometheus et Grafana**

## Introduction

 Créer un répertoir "TP01/" par exemple, forker puis cloner le repo `https://github.com/nas-irc/sample-application-students.git` créé à partir du fork.

Ce repo contient un backend et un frontend déjà opérationnels.
> Rappel sur JVM dans le TP1

## Monitoring your application

Ce que nous voulons, c'est utiliser Prometheus pour monitorer l'appli. Cependant, nous avons besoin d'un enpoint de type `/metrics`, comme celui utilisé lors du TD01 qui fournit à prometheus les données à stocker. Heureusement, une dépendance spring boot nommée `spring-boot-actuator` fournit tout un tas de fonctionnalitée prête à l'emploi pour monitorer l'application... dont un endpoint `/metrics` ! (plus précisément `/api/actuator/metrics`).

L'accès à cet endpoint donne un résultat sous la forme suivante : 

~~~
{
  "names": [
    "hikaricp.connections",
    "hikaricp.connections.acquire",
    "hikaricp.connections.active",
    "hikaricp.connections.creation",
    "hikaricp.connections.idle",
    "hikaricp.connections.max",
    ...
}
~~~

Malheureusement, ce résultat n'est pas compris par prometheus... Cependant !! Il existe une dépendance appelée Micrometer qui permet de faire la "traduction". La seule chose à faire est d'ajouter la dépendance dans le fichier `pom.xml` et "autoriser" les endpoints dans le fichier `src/main/resources/application.yml` (déjà fait dans le tp) : 

~~~
...
<dependency>
  <groupId>io.micrometer</groupId>
  <artifactId>micrometer-registry-prometheus</artifactId> 
</dependency>
...
~~~
~~~
######## src/main/resources/application.yml
...
management:
  metrics:
    export:
      prometheus:
        enabled: true
  server:
    add-application-context-header: false
  endpoints:
    web:
      base-path: /api/actuator
      exposure:
        include: health,info,env,metrics,beans,configprops,prometheus
~~~

On peut donc maintenant aller sur l'endpoint `api/actuator/prometheus` qui nous fournit des métriques compatibles avec prometheus ! 

Passons aux choses sérieuses ! Maintenant que nous avons tous les éléments pour monitorer notre appli, Il faut brancher notre serveur prometheus à l'appli en utilisant une configuration dans un fichier `prometheus/prometheus.yml`
~~~
###### prometheus/prometheus.yml
...

scrape_configs :
  # The job name assigned to scraped metrics by default.
  - job_name: tp01
    # How frequently to scrape targets from this job.
    #scrape_interval: global_config.scrape_interval
    # Per-scrape timeout when scraping this job.
    #scrape_timeout: global_config.scrape_timeout
    # The HTTP resource path on which to fetch metrics from targets.
    metrics_path: /api/actuator/prometheus
    # Configures the protocol scheme used for requests.
    scheme: http
    # Optional HTTP URL parameters.
    #params:
    #  <string>: [<string>, ...]
    # List of labeled statically configured targets for this job.
    static_configs:
      - targets : ['backend:8080']
~~~

>On note bien dans les targets `backend:8080` et non pas localhost car dans un network, c'est le nom qui permet de reconnaître les différents conteneurs entre eux. Le 8080 concerne le fait que l'appli springboot est lancé par défaut sur ce port (comme postgresql sur 5432).

Mais aussi en ajoutant le service `prom` au docker-compose.yml : 
~~~
#######docker-compose.yml
...
  prom:
    container_name: prom
    image: prom/prometheus:v2.30.2
    ports:
    - "9090:9090"
    volumes:
    - ./prometheus/prometheus.yml:/etc/prometheus/prometheus.yml
    networks:
    - app-network
...
~~~
On vérifie en allant sur le endpoint `http://localhost:9090/`. On remarque que les metrics apparaissent dans l'autocomplétion, ce qui indique que tout est bon, nous devons donc maintenant ajouter Grafana ainsi qu'une base de donnée pour ne pas perdre les metrics déja scrapées :

Même chose, on vient d'abord ajouter un fichier `datasources.yml` pour spécifier la source de données :

~~~
############./grafana/provisioning/datasources/datasource.yml
apiVersion: 1
datasources:
  - name: Prometheus
    url: http://prom:9090
    type: prometheus
    access: proxy
    isDefault: true
    version: 1
    editable: true
~~~

Ensuite, on ajoute au `docker-compose.yml` le service grafana ainsi que le service postgres : 

~~~
############./docker-compose.yml

...
  grafana:
    container_name: grafana
    image: grafana/grafana:8.2.0
    ports:
    - "3000:3000"
    volumes:
    - ./grafana/provisioning/:/etc/grafana/provisioning/
    environment:
    - GF_SECURITY_ADMIN_USER=admin
    - GF_SECURITY_ADMIN_PASSWORD=admin
    - GF_DATABASE_TYPE=postgres
    - GF_DATABASE_HOST=postgres
    - GF_DATABASE_USER=user
    - GF_DATABASE_PASSWORD=password
    networks:
    - app-network
    restart: on-failure

  postgres:
    container_name: postgres
    image: postgres:13
    environment:
    - POSTGRES_PASSWORD=password
    - POSTGRES_USER=user
    - POSTGRES_DB=grafana
    networks:
    - app-network
    restart: on-failure
    ...
~~~
On peut maintenant lancer grafana dans `http://localhost:3000/` et créer notre premier dashboard afin d'afficher les différents éléments demandés. 

> Attention, si on stoppe grafana, les dashboards créés sont perdus, c'est pourquoi il est conseiller d'exporter les dashboards en format json et de les stocker dans `./grafana/provisioning/dashboards` accompagné d'un fichier `default.yml` :
~~~
apiVersion: 1

providers:
  - name: TP01_Dashboard    # A uniquely identifiable name for the provider
    folder: TP_01 # The folder where to place the dashboards
    type: file
    options:
      path:  /etc/grafana/provisioning/dashboards
~~~

Toutefois, nous ne sommes pas les premiers dans le cadre de ce tp à vouloir analyser des courbes grafana issues des metrics fournies par jvm. C'est pourquoi la bonne pratique est de se rendre sur https://grafana.com/grafana/dashboards/ et de rechercher quelque chose comme "jvm micrometer". Le moteur de recherche nous retourne les dashboards déjà créés par d'autres utilisateurs que nous pouvons importer dans grafana à l'aide de son id puis exporter en format json. Par exemple <a href='https://grafana.com/grafana/dashboards/4701'> celui-ci </a> qui convient parfaitement.

Tous ces éléments nous permettent donc de répondre aux questions :

<span style="color:red">**1)** What's the difference between the system CPU usage and the
process CPU usage ? </span>
 - Charge système : cpu utilisé sur ma machine (tout le docker) dont les envois réseaux.
 - Charge process : cpu utiliséé par la jvm seulement  (dans le docker)
 - charge process < charge système

 <span style="color:red">**2)** What represents the CPU load ? </span>

La charge CPU indique à quel point les process se stackent sur le systeme complet. Autrement dit, c'est la moyenne sur 1 minute du nombre d'entité executables



<span style="color:red">**3)** What are all those types of thread ? </span>


 - Le ***Daemon thread*** qui est un thread basse-priorité qui est executé en arrière plan pour exécuter desd tâches comme un garbage collection par exemple.
 - Un ***Live Thread*** est un thread qui n'est pas encore mort (comme son nom l'indique), les threads daemon font donc parti du nombre le Live Thread.
 - Le ***thread Peak*** compte le nombre max de thread executé dans la JVM.

<span style="color:red">**4)** What do they all mean (the states) ? </span>

Un thread suit un cycle de vie qui peut prendre différents états :


 - **NEW** : Le thread n'est pas encore démarré. Aucune ressource système ne lui est encore affectée. Seules les méthodes de changement de statut du thread start() et stop() peuvent être invoquées

 - **RUNNABLE** : Le thread est en cours d'exécution : sa méthode start() a été invoquée

 - **BLOCKED** : Le thread est en attente de l'obtention d'un moniteur qui est déjà détenu par un autre thread

 - **WAITING** : Le thread est en attente d'une action d'un autre thread ou que la durée précisée en paramètre de la méthode sleep() soit atteinte.
 
 - **TIMED_WAITING** : Le thread est en attente pendent un certain temps d'une action d'un autre thread. Le thread retournera à l'état Runnable lorsque cette action survient ou lorsque le délai d'attente est atteint

 - **TERMINATED** : Le thread a terminé son exécution. La fin d'un thread peut survenir de deux manières :
     - la fin des traitements est atteinte
     - une exception est levée durant l'exécution de ses traitements

<span style="color:red">**5)** Memory max / used / committed ? </span>

 - Memory max : Mémoire max alloué pour la JVM
 - Memory used : Mémoire utilisée par la JVM à un moment donné
 - Memory committed : Mémoire assurée d'être allouable par la JVM

## Monitoring your database

Il est important de monitorer son application car beaucoup d'erreurs peuvent en surgir. Cependant, ce n'est pas la seule chose importante. Les bases de données par exemples le sont tout autant, car elles peuvent être connectés à beaucoup de services, ce qui implique enormément de solicitations, elles doivent donc être monitored.

Pas d'endpoint `/metrics` fournit par postgres. Heureusement (comme toujours), un [exporter](https://github.com/prometheus-community/postgres_exporter) a été créé pour exposer des metrics à prometheus. On ajoute donc son execution dans le `docker-compose` :

~~~
##############./docker-compose.yml
...
  postgres-exporter:
    container_name: postgres-exporter
    environment:
      - DATA_SOURCE_NAME=jdbc:postgresql://database:5432/SchoolOrganisation
    image: quay.io/prometheuscommunity/postgres-exporter
    networks:
    - app-network
...
~~~

Dans le network, c'est le port `9187` qui est exposé par défaut par l'exporter. L'endpoint par défaut est `/metrics`, on ajoute donc le job suivant dans prometheus : 

~~~
######## ./prometheus/prometheus.yml
  - job_name: tp01_db
    metrics_path: /metrics
    scheme: http
    static_configs:
      - targets : ['postgres-exporter:9187']
~~~

Et de la même manière, on importe [ce dashboard](https://grafana.com/grafana/dashboards/9628) dans grafana afin d'observer les metriques intéressantes.

## Seconde application


## Plug and play


## Alerting

## Inspection


## Conclusion
