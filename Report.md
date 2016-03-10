# µTest #
-----------------------

# Report #

1. Introduction
2. Architecture
	* Développement
	* Opérationnelle
3. Analyse
	* Points faibles
	* Points forts
4. Conclusion

## 1. Introduction ##
Le but du plugin est de fournir un outil qui permet de donner de la confiance dans les tests des développeurs, mais aussi pour Ops un outil performant et facile à déployer.
## 2. Architecture ##

Le framework est basé sur un plugin maven, donc le code est organisé de manière à avoir des parties qui gérent la chaine de buid (à savoir les mojos) ainsi que des parties plus "mutations/selecteurs".

### Développement ###
Dès le début, on savait qu'on allait manipuler différents artifacts ayant le même nom mais ayant des version différentes.
Le méchanisme de chargement de classes dynamiques s'est avéré autant utile que diabolique.
La principale approche pour faciliter était d'englober des comportements (lancement des tests sur toutes les classes, et ce pour différents mutants) dans une classe et charger ça dynamiquement, que de laisser directement le mojos charger les differents artifacts nécessaires puis les instancier.
De plus, en séparant les préoccupations, cela nous permettrait d'évoluer dans le futur plus facilement et rapidement (par exemple passer de manipulations sur l'arbre syntaxique Java à des manipulations de bytecode grâce à l'interface Instrumentation fournie par java).

### Opérationnelle ###
Pour les Ops, le produit est un réel avantage car il leur permet de disposer d'un outil totalement intégré et une chaîne de build homogène et évolutive à souhait.
Pour déployer le plugin, il n'y a rien de plus simple : il suffit de l'ajouter sur un repository dédier tel que nexus et de l'incorporer dans un serveur d'intégration continue (i.e. Jenkins ou Hudson).

## 3. Analyse ##
Comme tout produit ne vient pas qu'avec forces ou des faiblaisses, voici une analyse de ce que l'on considère trés bien, bien ou à modifier.

### Points faibles ###
Pour l'instant, la gestion des dependences transitives est totalement assurée (spoon qui dépend de beaucoup d'artifacts mais cela est géré), en revanche les dépendances directes sont un probléme à régler rapidement sur la prochaine itération (si il y'en avait une). On a découvert cela assez tardivement, on avait deux solutions sous la main : une à base Jcabi, l'autre de JDOM.
De plus, la gestion des paths n'est pas cross-platform à quelques endroits, il faudrait donc faire du refactoring pour que cela fonctionne sur n'importe quel os.
Le code doit être refactoré aussi pour prise en main plus facile.

### Points forts ###
Comme dit plus haut le plugin sépare tout ce qui est pilotage de la chaîne de build, de la génération de mutants et des lancements de tests. Ce qui permettrait de changer d'outil de chaine de build tout en conservant les étapes les plus importantes, ou bien changer d'outil de mutation sans impacter la chaîne de build.
Au niveau de la rapidité, le fait d'utiliser le plugin apporte un gain de vitesse intéressant (vu que l'on évite de lancer des commandes maven à la suite). Ce qui serait encore plus intéressant serait paralléliser avec thread, mais le temps nous as manqué.

## 4. Conclusion ##
En définitive le projet fut trés intense techniquement du fait d'une utilisation de java assez peu conventionnelle, mais le résultat produit dans le laps de temps est plutôt intéressant.
