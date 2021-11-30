**GALDEANO Nasri**  
**PROTIÈRE Axel**

**5IRC**
# **TP 1 - Prometheus et Grafana**

## Introduction
---

**1)** Créer un répertoir "TP01/" par exemple, forker puis cloner le repo `https://github.com/nas-irc/sample-application-students.git` créé à partir du fork.

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
      - targets : ['localhost:8080']
~~~

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
    - /home/lumapps/Documents/devops2/TP01/prometheus/prometheus.yml:/etc/prometheus/prometheus.yml
    networks:
    - app-network
...
~~~