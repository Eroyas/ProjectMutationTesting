# µTest #
-----------------


# Build #


Notre framework de mutations µTest se base sur Maven et peut donc être construit rapidement.

1. Commandes
2. Configuration


## 1. Commandes ##

Pour installer le plugin µTest, il suffit de taper la commande :
~~~shell
mvn install
~~~

## 2. Configuration ##
dans votre projet il faut ajouter la dépendence suivante :

~~~mvn
    <groupId>fr.unice.polytech.mojos</groupId>
        <artifactId>my-app</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependency>
~~~

ensuite une étape intérmediaire est nécessaire, il faut ajouter dans le fichier my-app-1.0-SNAPSHOT.pom vos dépendances et repositories (cette étape en plus due à JDOM qui n'as pas voulu coopérer avec nous ce soir), le fichier se trouve grâce à :

~~~bash
/path/to/.m2/repository/fr/unice/polytech/mojos/my-app/1.0-SNAPSHOT/my-app-1.0-SNAPSHOT.pom
~~~
cette étape doit se faire une fois seulement aprés l'installation.\

Après avoir modifié le pom.xml du projet que l'on souhaite muter en ajoutant la dépendance de notre projet et les configuration nécessaires, il suffit de lancer la commande :

~~~bash
mvn package
~~~
