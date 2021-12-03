**GALDEANO Nasri**  
**5IRC**
# **TD 3 - JAEGER Tracing**

## Running Jaeger
---
### **Configuring Jaeger**

### **Configuring Jaeger**


**1)** Créer un docker compose avec l'image d'elastic search exposant le port 9200, et avec la variable d'environnement `single-node` pour simplifier le développement. Il faut aussi faire tourner ce docker dans un network : 
~~~
##############docker.compose.yml
version: "3.7"

networks:
    resa-network:

services:
    elasticsearch:
        container_name: elasticsearch
        image: docker.elastic.co/elasticsearch/elasticsearch:7.15.2
        ports:
        - "9200:9200"
        environment:
        - discovery.type=single-node
        networks:
        - resa-network
~~~
**2)** Pour tester : `http://localhost:9200/_cluster/health` : 
~~~
{
    "cluster_name": "docker-cluster",
    "status": "green",
    "timed_out": false,
    ...
}
~~~
### **Installing Kibana**
**1)** Ajouter dans docker-compose :
~~~
##############docker.compose.yml
    ...
    kibana:
        container_name: kibana
        image: docker.elastic.co/kibana/kibana:7.15.2
        ports:
        - "5601:5601"
        environment:
        - ELASTICSEARCHURL=http://elasticsearch
        networks:
        - resa-network
~~~
**2)** Pour tester : accéder à `http://localhost:5601/` sur le navigateur. 
### **API Usage**
**1)** Accéder aux dev tools (volet de gauche : Management)

**2)** Saisir `GET _cluster/health` dans la fenêtre de droite et executer. On reçoit la même réponse que lorsqu'on appelle `http://localhost:9200/_cluster/health`.

### **Créer un index**

Maintenant il faut créer un index nommé `recipes` permettant d'indexer le document suivant : 
~~~
{
  "name": "Poulet au curry",
  "ingredients": [
    "blanc de poulet",
    "curry",
    "oignons",
    "poivre",
    "sel"
  ],
  "kitchen_tools": [
    "poêle",
    "couteau"
  ],
  "preparation_time": 5,
  "cook_time": 15
}
~~~
Pour cela, il faut définir un mapping explicit de nos données. Le voici :
~~~
PUT /recipes
{
  "mappings": {
    "properties": {
      "name":    { 
        "type": "text" 
        "fields": {
          "keyword" : {
            "type" : "keyword",
          }
        }
      },  
      "ingredients":    { "type": "keyword" },  
      "kitchen_tools":    { "type": "keyword" },  
      "preparation_time":   { "type": "integer"  },
      "cook_time":   { "type": "integer"  }     
     
    }
  }
}
~~~
On utilise une requête PUT/{nom de l'index} pour créer l'index. On mappe ensuite chacun des éléments avec son type.
>Mais pourquoi on ne spécifie rien pour `ingredients` et `kitchen_tools`, qui sont différents car ce sont des arrays ?

Car ElasticSearch n'a pas de type dédié aux Arrays, chaque field peut contenir 0 ou plusieurs valeurs par défaut. Cependant, toutes les valeurs dans la chaine de caractère doivent être du même type de donnée.

> Text, keyword ? quelle diff ?

 Le premier va analyser chaque mots de la phrase tandis que le second va prendre toute la chaîne en tant que mot clé. Dans notre cas, on a voulu mapper un champ a un keyword ET un texte, le champ `name`. On a vait ça de la manière suivante: 
~~~
"the_field_you_want_to_map_to_text_and_keyword" : {
  "type" : "text",
  "fields" : {
    "keyword" : {
      "type" : "keyword",
    }
  }
},
~~~
### **Indexer un document**

Une fois l'index créé, on peut maintenant indexer notre premier document.

**1)** Insérer le document dans ElasticSearch grâce à l'appel suivant dans kibana :
~~~
POST recipes/_doc
{
  {{DOC}}
}
~~~
On remarque que l'index utilisé est specifié dés la création du document. 

**2)** On peut vérifier la création du document grâce à `GET recipes/_search` qui nous retourne : 
~~~
{
  "took" : 592,
  "timed_out" : false,
  "_shards" : {
    "total" : 1,
    "successful" : 1,
    "skipped" : 0,
    "failed" : 0
  },
  "hits" : {
    "total" : {
      "value" : 1,
      "relation" : "eq"
    },
    "max_score" : 1.0,
    "hits" : [
      {{DOC}}
    ]
  }
}
~~~

### **Indexer une collection de documents**

On ne veut pas toujours indexer un seul document. Pour en indexer plusieurs à la fois, ElasticSearch propose l'enfdpoint `<index>/_bulk`.

