**GALDEANO Nasri**  
**5IRC**
# **TD 1 - Prometheus et Grafana**

## Running Prometheus
---
### **Configuration**

**1)** Créer un répertoir "TD01/prometheus" par exemple, puis créer un fichier `prometheus.yml`.

~~~
#################################./prometheus/prometheus.yml

global:
  # How frequently to scrape targets by default.
  scrape_interval: 1m
  # How long until a scrape request times out.
  scrape_timeout: 10s
  # How frequently to evaluate rules.
  evaluation_interval: 1m
  # The labels to add to any time series or alerts when communicating with
  # external systems (federation, remote storage, Alertmanager).
  #external_labels:
  #  <labelname>: <labelvalue> ...
  # File to which PromQL queries are logged.
  # Reloading the configuration will reopen the file.
  #query_log_file: <string>

scrape_configs :
  # The job name assigned to scraped metrics by default.
  - job_name: td01
    # How frequently to scrape targets from this job.
    #scrape_interval: global_config.scrape_interval
    # Per-scrape timeout when scraping this job.
    #scrape_timeout: global_config.scrape_timeout
    # The HTTP resource path on which to fetch metrics from targets.
    metrics_path: /metrics
    # Configures the protocol scheme used for requests.
    scheme: http
    # Optional HTTP URL parameters.
    #params:
    #  <string>: [<string>, ...]
    # List of labeled statically configured targets for this job.
    static_configs:
      - targets : ['localhost:9090']
~~~
Grâce à cette config, on va scraper (récuperer des metrics) toutes les 1 minutes l'endpoint `/metrics` qui est atteignable sur le `localhost:9090`.

**2)** Exécuter la commande `docker run -p 9090:9090 -v /path/to/prometheus.yml:/etc/prometheus/prometheus.yml prom/prometheus` 




### **Exploring Metrics**

On retrouve dans `localhost:9090/metrics`, toutes les infos qui ont étés scrapés. On peut cependant requêter prometheus pour avoir des choses plus précises, par exemple :

- `go_gc_duration_seconds.` : pour mesurer le temps des garbage collections.
- `{quantile="0.5"}.` : pour afficher que les mesures du quantile 0,5 seulement. (on peut aussi utiliser `!=` par exemple)
- Enfin, on peut appliquer des opérations dans les requêtes :
  - `sum(promhttp_metric_handler_requests_total) by (job)` : pour afficher la somme du nombre de reqêtes faîtes sur ce job (requetes sur metrics), peu intéressant.
  - `sum(rate(promhttp_metric_handler_requests_total[5m])) by (job)` : pour afficher le nombre de requêtes par seconde en moyenne sur une fenêtre de 5 minutes, le temps peut être changé. Métrique plus intéressante car elle peut permettre de contrôler le trafic sur un endpoint donné.

## Running Grafana
---

### **Configuration**

**1)** Exécuter la commande `docker run -d --rm --name grafana -p 3000:3000 grafana/grafana:8.2.0` 
**2)** Accéder à `http://localhost:3000/` puis sur le menu de gauche "explore.

### **Add data source in grafana**

 - *Data source* : interface contenant des infos que grafana peut requêter et afficher.

 - Grafana est quasiment considéré comme l'outil de visu par défaut de prometheus, il est donc **plus qu'apte à supporter les fonctionnalités de requetage fournis par le langage PromQL**.

  Afin de connecter Prometheus et Grafana ensemble, la meilleure solution est de les lancer dans le même network. Pour cela, rien de mieux que de lancer un docker-compose :
 ~~~
#################################./docker-compose.yml
version: "3.7"

networks:
    local-network:
        name: local
services:
    prom:
        image: prom/prometheus:v2.30.2
        ports:
        - "9090:9090"
        volumes:
        - /home/lumapps/Documents/devops2/TD01/prometheus/prometheus.yml:/etc/prometheus/prometheus.yml
        networks:
        - local-network

    grafana:
        image: grafana/grafana:8.2.0
        ports:
        - "3000:3000"
        environment:
        - GF_SECURITY_ADMIN_USER=admin
        - GF_SECURITY_ADMIN_PASSWORD=admin
        networks:
        - local-network
 ~~~

 et d'executer les docker grâce à `docker-compose up -d`. 
 Maintenant que tout est lancé sur le même network, il faut ajouter la data source. Pour cela, il faut cliquer sur la roue dentée sur la gauche puis `Data sources` et enfin `add data source`:

  - l'url à spécifier n'est pas `http://localhost:9090` mais `http://prom:9090` (nom du service docker-compose)

On peut désormais effectuer des requêtes PromQL depuis grafana. Par exemple `sum(promhttp_metric_handler_requests_total) by (job)`, etc...



| ATTENTION: l'arrêt du serveur grafana implique la suppression de la configuration |
| --- |
Heureusement, grafana peut être configuré ! (Everything as a code blablablaaaaaaa), grâce à un fichier `datasource.yml` :
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
Que l'on ajoute en tant que volume dans le serveur grafana :
~~~
#################################./docker-compose.yml

    grafana:
        ...
        volumes:
        - /home/lumapps/Documents/devops2/TD01/grafana/provisioning/:/etc/grafana/provisioning/
        ...
~~~
> dans `./grafana/provisioning/`, on peut configurer d'autres choses, comme des dashboards.

### **Add DataBase in grafana**
Jusqu'ici, si le serveur grafana est éteint, toutes les infos récupérées sont perdues. C'est pourquoi il faut ajouter une base de données pour pouvoir tout stocker. 
Pour ceci, le mieux est d'ajouter une base de données postgresql au docker-compose et de lier cette bdd à grafana : 
~~~
#################################./docker-compose.yml

    grafana:
        ...
        environment:
        - GF_SECURITY_ADMIN_USER=admin
        - GF_SECURITY_ADMIN_PASSWORD=secretpassword
        - GF_DATABASE_TYPE=postgres
        - GF_DATABASE_HOST=postgres
        - GF_DATABASE_USER=user
        - GF_DATABASE_PASSWORD=password
        networks:
        - local-network
        restart: on-failure
        ...
    postgres:
        image: postgres:13
        environment:
        - POSTGRES_PASSWORD=password
        - POSTGRES_USER=user
        - POSTGRES_DB=grafana
        networks:
        - local-network
        restart: on-failure
~~~

### **Display**
