# 2. List the existing Beats in the Elastic world, and explain in 1-2 lines what data it is supposed to collect.

- File beat : permet de collecter des fichiers de logs
- Metric beat : permet de collecter des indicateurs de performances et des statistiques sur les systèmes et les services. 
- Packet beat : permet de collecter des paquets réseaux pour monitorer et sécuriser son réseau. 
- Winlog beat : permet de collecter des evenements windows afin de garder un oeil sur son infrastructure et sur les informations systèmes. 
- Audit beat : permet de collecter des données du système Linux sans passer par auditd. Peut aussi réutiliser les règles d'audit existante et/ou passer par auditd
- Hear beat : permet de monitrer la disponibilité d'un service en envoyant des requêtes à une liste d'URL pour en mesurer le temps de réponse. 
- Function beat : permet de collecter et monitorer des données provenant de services cloud.

# 3. 


docker-compose.yml : 

``` yml
version: '3.3'

# list des services
services:
  elastic:
    image: docker.elastic.co/elasticsearch/elasticsearch:7.15.2
    container_name: elastic
    ports:
      - "9200:9200"
      - "9300:9300"
    environment:
      - discovery.type=single-node
    networks:
      - tp2-network
# exporter 
# choix d'exporter c'est le plus simple à utiliser avec elastic search
  elasticsearch_exporter:
    container_name: exporter
    image: quay.io/prometheuscommunity/elasticsearch-exporter:latest
    command:
    #permet de se connecter au service elasticsearch
      - '--es.uri=http://elastic:9200'
    restart: always
    #permet à ce service d'être executé après le service elastic
    depends_on:
      - elastic
    ports:
      - "9114:9114"
    #connecte sur le même network que les autres services
    networks:
      - tp2-network

  kibana:
    image: docker.elastic.co/kibana/kibana:7.15.2
    container_name: kibana_container
    ports:
      - "5601:5601"
    environment:
      ELASTICSEARCH_HOSTS: '["http://elastic:9200"]'
    networks:
      - tp2-network

  prometheus:
    image: prom/prometheus:v2.30.2
    ports:
      - "9090:9090"
    volumes:
      - ./prometheus:/etc/prometheus
    networks:
      - tp2-network

  filebeat:
    #permet de prendre en compte le fichier Dockerfile avant de build, qui permet de copier le fichier filebeat.yml dans le container
    build : ./
    image : docker.elastic.co/beats/filebeat:7.15.2
    container_name: filebeat
    #renseigne à filebeat à quel endroit chercher la liste des dockers à récupérer (:ro = readonly)
    volumes:
      - /var/lib/docker/containers:/var/lib/docker/containers:ro
    networks: 
    - tp2-network

networks:
  tp2-network: null
```
filebeat.yml : 

``` yml
#permet de dire à filebeat de récupérer les logs de tous les containers executés par le docker-compose (il cherche dans le dossier renseigné dans volumes de filebeat ci-dessus)
- type: docker
  containers.ids: '*'
#permet d'envoyer tous les logs à cette url (l'url d'elasticsearch)
output.elasticsearch:
  hosts: ["https:/elastic:9200"] 

```

Dockerfile : 

Ce fichier permet de récupérer l'image filebeat, et de copier le fichier filebeat présent dans le dossier actuel dans le container filebeat. De plus il utilise les droits du user root pour donner les permissions de lecture du fichier filebeat.yml au groupe filebeat

``` 
FROM docker.elastic.co/beats/filebeat:7.15.2
COPY filebeat.yml /usr/share/filebeat/filebeat.yml
USER root
RUN chown root:filebeat /usr/share/filebeat/filebeat.yml
USER filebeat
```

Ensuite, on a juste à docker-compose up pour executer tous les containers. 

Problème : filebeat ne se connecte pas à elasticsearch pour une raison inconnue. 