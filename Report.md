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
le but du plugin est de fournir un outil qui permet de donner de la confiance dans les tests des developpeurs, mais aussi pour Ops un outil perferformant et façile à deployer.
## 2. Architecture ##

le framework est un basé sur un plugin maven, donc le code est organiser de maniére à avoir des parties qui gérent la chaine de buid (a savoir le mojos) ainsi que des parties plus mutations / selecteurs

### Développement ###
tout d'abord, et dés le début, on savais qu'on allait manipuler différents artifacts ayant le même nom mais ayant des version différentes.
le méchanisme de chargement de classes dynamique c'est avéré autant utile que diabolique.
la principale approche pour faciliter etait d'englober des comportement (lancement des test sur toutes les classes, et ce pour différents mutants) dans une classe et charger ça dynamiquement, que de laisser directement le mojos charger les differents artifacts nécessaire puis les instancier.
de plus, en séparant les préoccupations, cela nous permettrais d'évoluer dans le futur plus facilement et rapidement (par exemple passer de manipulations sur l'arbre syntaxique Java à de manipulation de bytecode grâce à l'interface Instrumentation fournie par java)

### Opérationnelle ###
pour les Ops le produit est un réel avantage car il leur permet de disposer d'un outil totalement intégré et une chaîne de build homogéne et evolutive à souhait.
pour déployer le plugin il n'y a rien de plus simple : il suffit de l'ajouter sur un repository dédier tel que nexus et de l'incorporer dans un serveur d'intégration continue (style jenkins ou hudson).

## 3. Analyse ##
comme tout produit ne viens pas qu'avec force ou des faiblaisses voici une analyse sur ce que l'on considére trés bien, bien ou à modifier.

### Points faibles ###
en ce qui concerne les faiblaisses du projet :\
pour l'instant la gestion des dependences transitive est totalement assuré (spoon qui dépend de beaucoup d'artifacts mais cela est géré), en revanche les dépendances directes sont un probléme a régler rapidement sur la prochaine itération (si il y'en avait une), on a découvert cela assez tardivement, on avait deux solution sous la main une à base Jcabi et JDOM, mais à 22:29 où j'écris ceci, JDOM en avait décidé autrement.\
de plus la gestion des paths n'est pas cross-platform à quelques endroits, il faudras donc faire du refactoring pour que ça tourne sur n'importe quel os.
le code doit être refactorer aussi pour prise en main plus facile.

### Points forts ###
comme dit plus haut le plugin sépare tout ce qui est pilotage de la chaîne de build de la génération de mutant et des lancement de test, ce qui permettrais de changer d'outils de chaine de build tout en conservant les étapes les plus importantes, ou bien changer d'outil de mutation sans impacter la chaîne de build.\
au niveau de la rapiditée le fait d'utiliser le plugin apporte un gain de vitesse intéressant (vu qu'on évite de lancer des commandes maven à la suite), ce qui serait encore plus intéressant serait paralléliser avec thread, mais encore une fois le temps nous as manqué.

## 4. Conclusion ##
en définitive le projet fut trés intense techniquement du fait d'une utilisation de java assez peux conventionnelle, mais le résultat produit dans le laps de temps est plutôt intéressant.
