# ProjectMutationTesting

l’équipe se compose de :

       - Nicolas Sarroche (Parcours AL)
       - Yasin Eroglu (Parcours CASPAR)
       - Yassine Tijani (Parcours AL)

le produit que l’on souhaite réaliser est un Framework de tests par mutation, le principe étant d’introduire dans un
programme donné une mutation particulière à un endroit particulier, puis faire passer ce nouveau programme mutant dans
notre banc de test. Le travail réalisé sera sous la forme d’un plugin Maven, ce qui permet une intégration plus facile
avec la chaîne de build.



Concernant la chaîne de build elle est pilotée grâce à Maven, ses phases se décrivent de la manière suivante  :


1. validate/initialize


2. generate sources (pour des soucis de performance, la génération de code est parallélisée) :
      a.    en entrée cette phase reçoit des fichiers sources .java, grâce à Spoon on procède à une manipulation sur
            l’arbre syntaxique du code java afin d’introduire des mutations.
      b.    en sortie de la phase on obtient du code source contenant une mutation qui sera placé sous
            target/generated-sources
      c.    outlis : Spoon


3. compile : cette phase compile tout les mutants ainsi que le code source de manière parallèle (compilation
   parallèle supportée depuis Maven 3) :
      a.    en entrée cette phase reçoit des fichiers sources .java du programme principal ainsi que le code source des
            différents mutants
      b.    en sortie sont produits des fichiers .class se trouvant target/classes
      c.    outils : javac


4. test-compile : cette phase compile tout les tests unitaires
      a.    en entrée elle prend le code source des tests unitaires
      b.    en sortie on récupère les .class de nos tests sous target/test-classes
      c.    outils : javac


5. test  : cette phase lance les tests unitaires pour tous les mutants ainsi que le code source en parallèle. Pour que
   la chaîne continue, les tests du programme principal doivent passer obligatoirement. Mais vu que la majorité des
   mutants sont censés ne pas passer les tests, le fait qu’ils ne les passent pas ne doit pas interrompre la chaîne de
   build.
      a.    en entée cette phase prend les .class du programme principal, des mutants ainsi que des test unitaires.
      b.    en sortie cette phase on produit un fichier XML “report.xml” et des fichiers au format .txt sous
            le répertoire target/surefire-reports.
      c.    outils : Junit Framework  / plugin : Maven Surefire Plugin (pour pouvoir produire les rapports).


6. build-report : cette phase est une phase personnalisée que l’on introduit dans la chaîne de build afin de lancer
   la fabrication de la page HTML de rapport.
      a.    en entrée elle reçoit les “reports.xml” qui étaient à la sortie de la phase de test.
      b.    en sortie cette phase nous produit un fichier au format HTML.
      c.    outils : bootstrap, highchart (la fabrication des rapports sera automatisée grâce à java).


On constate donc que grâce à Maven, on arrive à piloter la chaîne de Build d’une manière fluide, que ce soit en modifiant
le pom.xml du plugin, ou par l’ajout de Mojos (par exemple en parallélisant les tests avec des threads et en les lançant
depuis un Mojo)


- Quelles mutations, où les appliquer, comment les appliquer ?

Dans la conception d’une application de qualité, il est essentiel d’utiliser des tests unitaires. Mais une bonne
couverture de code ne signifie pas forcement que le code est bien testé, ni que les tests sont pertinents.
Une des techniques qui permet de remédier à ce problème est le processus d’introduction de mutants dans notre code source.
Nous allons donc prendre notre code métier sur lequel nous souhaitons tester nos tests et y introduire des mutants.


- Quelles mutations ?

Une mutation étant une modification du code source, nous allons donc effectuer différentes actions en introduisant
différents bugs afin de voir la réaction des tests. Si nos tests passent toujours, c’est qu’ils ne sont pas pertinents,
nos mutants on donc survécu. Les bugs qui seront à introduire sont de différents types :
     - Nous aurons la possibilité de modification des opérateurs mathématiques par exemple, en transformant un “+” par un “-”.
     - la modification des opérateurs de comparaison, soit transformer un “==” par un “!=”.
     - La modification des bornes des opérateurs de comparaison, soit transformer un “<” par un “>”.
     - La modification de condition booléenne, soit transformer un “true” par un “false”.
     - La modification d’une variable dans une méthode.
     - La suppression d’instruction ou d’attribut d’une classe.
     - la modification des paramètres qui sont passés à une méthode ou un constructeur.

Un petit nombre de mutations bien sélectionnées suffit à avoir des résultats précis, en effet à trop faire muter
notre code on est à peu près sûr qu’il ne passera pas les tests, donc cela représente peu d'intérêt pour
notre utilisateur (qui n’est autre qu’un développeur).
Il faut donc casser du code, mais le casser d’une manière intelligente.

- Où les appliquer ?

La phase de génération de mutants consiste à parcourir les classes et pour chacune déterminer si des mutations
sont applicables. Cela s’effectue donc lors de la phase generate-sources de la chaîne de Build.
Nous pourrons donc appliquer des mutations dans les différentes classes de notre programme, dans les différentes
méthodes, dans les différentes conditions de boucle…La mutation peut être appliquée là où l’on veux tant que cela
reste pertinent, par exemple appliquer une mutation aux classes qui implémentent une certaine
interface, apporterait de la flexibilité à l’utilisateur final.

- Comment les appliquer ?

Spoon nous permet de récupérer des noeuds de l’arbre syntaxique grâce à des “queries”, qui sont sous la forme de paths
ou de filtres. Cependant la solution des paths n’est pas envisageable car elle présuppose une connaissance de la structure du code.
Vient ensuite le rôle des processeurs Spoon. Un processeur Spoon permet de faire une analyse de code pour un
seul élément, grâce à la méthode “process”. On l’applique pendant la phase generate source a l’aide d’un Mojo qui sera
destiné à cette phase là.
Ensuite, on compile le tout pendant la phase compile de la chaîne de build, puis on lance les tests dans la phase test.
On exécute les tests pour chaque mutant. Quand un des tests échoue, on dit que le mutant est tué. Dans le cas contraire,
on dit qu’il a survécu. On pourra donc par la suite faire une analyse de ce qui s’est passé.