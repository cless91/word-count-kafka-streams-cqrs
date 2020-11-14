# :fr: POC kafka-streams sur "wordcount"

## Présentation

Ce projet implémente un "wordcount", dont le but est de compter les mots entrés par l'utilisateur.

"Wordcount" a souvent été qualifié comme étant le "Hello World!" du big data. 
C'est un bon exercice pour prendre en main une technologie, comme Hadoop ou Spark. 

Ce projet fait partie d'un ensemble de mini projets persos, dont le but est de me familiariser avec Kafka Streams.

Pourquoi ? Par simple curiosité pour commencer, c'est une technologie qui m'intrigue depuis un moment, 
qui vient compléter mes connaissances et compétence de Kafka "classique", et ça fait une corde de plus à mon arc

## Pré-requis

- `java jdk 11+`
- `docker` 
    - https://docs.docker.com/engine/install/ubuntu/ pour un guide d'installation de `docker` sur Ubuntu
- `docker-compose`
    - https://docs.docker.com/compose/install/ pour un guide d'installation de `docker-compose`

## Utilisation de l'application

### "Installation"

`mvn clean install`

### Lancement

1. Démarrer Kafka : `docker-compose down && docker-compose up` 
2. Démarrer l'application :
    - via l'IDE de votre choix
    - via maven: `mvn spring-boot:run`

### Ajouter du texte

`curl -X POST -d 'Hello World' http://localhost:8080/write`

### Compter les mots ajoutés 

```
> curl -X GET localhost:8080/count
{"Hello":1,"World":1}
```

### Supprimer un mot
`curl -X DELETE localhost:8080/word/{word}`

### Supprimer tous les mots
`curl -X DELETE localhost:8080/all`

## Quelques commandes utiles
reset an application:
```docker exec -it word-count-kafka-streams-cqrs_kafka_1 
kafka-streams-application-reset --bootstrap-servers kafka:9092 --application-id wordcount
```

list topics:
```
docker exec -it word-count-kafka-streams-cqrs_kafka_1 kafka-topics --bootstrap-server kafka:9092 --list
```

## Architecture et Design (à venir)

# :fr: Proof-of-Concept kafka-streams, on wordcount