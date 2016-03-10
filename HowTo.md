# µTest # 
---------------

# How To # 

1. Un plugin Maven
	* S'intégrer dans une chaine
	* Générer des mutants
	* Compiler et exécuter les mutants
	* Produire un rapport HTML
2. Des processeurs
	* Processeur BinaryOpMutation
	* Processeur BinaryOpLoop
	* Processeurs IfCondTrueMutation & IfCondFalseMutation
	* Processeur ScopeFieldMutation
	* Processeur ThisRemovalMutation
	* Processeur UnaryIncrementMutation
3. Des sélecteurs
	* Package
	* ComplexityLocator
4. Intégration dans un projet "demo"
	* Edition du pom.xml
	* Exécution



## 1. Un plugin Maven ##

Nous avons conçu ce framework de mutation µTest comme un plugin Maven. Il s'inscrit donc directement dans la chaine "construction" (build) de ce dernier.

### S'intégrer dans une chaine ###
Afin de générer des programmes mutants et de lancer les tests dessus, nous avons choisit de nous inscrire directement dans la chaine de "construction" (build) de Maven en quatre étapes. µTest permet donc de générer des mutants d'un programme d'origine avec le goal "generate-mutations", et ce, apres la phase "????" de Maven. Vient ensuite le moment de compiler les mutants ainsi générés à l'aide du goal "compile-mutants". La troisième étape, "mutation-test", permet de lancer les tests du projet d'origine sur les altérations du code produites. L'ultime étape, "generate-report", permet de générer le rapport résultant des tests au format HTML.

### Générer des mutants ###
La génération de mutants qui est possible grâce au goal "generate-mutations", se fait par le biais de la classe "MutationMojo". Pour chaque mutation, pour chaque selecteur et pour chaque package spécifiés dans le pom, un dossier appelé mX (X étant le numéro de la mutation) est créé et contient un copie du projet sur lequel ont été appliquées les mutations en fonction des sélecteurs.

INTEGRER DU CODE DE DEMO
 
### Compiler et exécuter les mutants ###
Une fois que tous les mutants sont générés, la classe "CompileMutantsMojo" permet de les compiler.  A DETAILLER

Pour exécuter les tests sur les mutants créés, on utilise la réflexion de Java pour appeler dynamiquement la méthode "runTests" de "IsolatedTestRunner" avec deux paramètres : une liste de classe à tester et un ClassLoader. La liste de classe a été calculée par "TestMojo"
~~~Java
Class<????> classRunner = Class.forName("fr.unice.polytech.mojos.IsolatedTestRunner", true, cl);
Object runner = classRunner.getConstructor(String.class).newInstance(project.getBasedir() + "/target/surefire-reports/mutants/" + fi.getName());
Method runMethod = classRunner.getMethod("runTests", List.class, URLClassLoader.class);
runMethod.invoke(runner, names, cl);
~~~

La méthode "runTests" se charge d''invoquer JUnitCore pour exécuter les tests.

~~~Java
...
Result result = jUnitCore.run(classes);
...
~~~


### Produire un rapport HTML ###
La production de notre rapport au format HTML se fait en 4 étapes. Lors des tests effectués par le JUnitCore, on analyse le résultat de ces tests pour produire un XML par test.

INTRODUIRE CODE / IMAGE

Une fois que tous les tests sont terminés, on récupère chaque fichier XML produits que l'on fusionne en un unique XML grâce au "JUnitResultProducer".

INTRODUIRE CODE / IMAGE

Pour convertir cet unique XML en HTML, on utilise une transformation XSL qui nous permet d'afficher le nombre total de tests effectués, le nombre total de tests en erreur, et le nombre total de tests qui ont réussis. Ensuite, on afficher le résultat, test par test,  en détaillant pour chacun ceux qui ont réussi et ceux qui ont échoué.

INTRODUIRE CODE / IMAGE

De manière à avoir un rendu plus esthétique et plus lisible, on utilise Bootstrap  pour afficher les informations en couleurs (Vert les tests sont OK, jaune ignorés, rouge échoués, et noir "mort-nés"). HighChart permet une lecture plus aisée des résultats puisque l'on les affiche  sous forme de diagramme circulaire.

INTRODUIRE CODE / IMAGE

## 2. Les processeurs ##

Les processeurs sont "????????". Le framework µTest dispose de diférents type de processeurs et sont au nombre de sept. Chaque processeur est construit sur la même base : une fonction isToBeProcessed qui permet de savoir si l'on doit ou non lancer la fonction "process" sur cet élément, et la fonction "process" qui permet de prendre l'élément et de le modifier.


### Processeur BinaryOpMutation ###
"BinaryOpMutation" analyse les éléments qui lui sont passés en paramètre. On vérifie que l'on récupère bien un "CtBinaryOperator" (un opérateur binaire quelconque) et l'on le remplace par une opération de division.

~~~Java
CtBinaryOperator op = (CtBinaryOperator)candidate;
op.setKind(BinaryOperatorKind.DIV);
~~~ 
### Processeur BinaryOpLoop ###
"BinaryOpLoop" est une variation de "BinaryOpMutation" et permet de ne modifier que les opérateurs binaires qui sont à l'intérieur d'un corps de boucle. 
~~~Java
CtLoop ctFor = candidate.getParent(new TypeFilter<>(CtLoop.class));
return ctFor != null;
~~~

### Processeurs IfCondTrueMutation & IfCondFalseMutation ###
Ces deux processeurs peuvent être utilisés pour remplacer les conditions des "if" par "true" ou "false". On vérifie que l'élément que l'on récupère est un "CtIf" (un "if" du langage Java) et l'on remplace sa condition.

~~~Java
CtIf op = (CtIf) candidate;
op.getCondition().replace(new CtLiteralImpl<Boolean>().setValue(false));
~~~

### Processeur ScopeFieldMutation ###

Le processeur "ScopeFieldMutation" permet de modifier la portée d'une variable en ajoutant le mot clé "final".
~~~Java
CtField op = (CtField)ctField;
op.setVisibility(ModifierKind.FINAL);
~~~ 

### Processeur ThisRemovalMutation ###

Le processeur "ThisRemovalMutation" permet de supprimer les appels à "this". On vérifie que le candidat est bien de type "CtThisAccess" et l'on l'efface.

~~~Java
candidate.delete();
~~~

### Processeur UnaryIncrementMutation ###

Le processeur "UnaryIncrementMutation" permet de changer les conditions d'une boucle "for" en modifiant le comparateur en "<=".

~~~Java
CtForImpl ctFor = (CtForImpl) candidate;
CtBinaryOperatorImpl expr = (CtBinaryOperatorImpl) ctFor.getExpression();
if(expr.getKind().equals(BinaryOperatorKind.LT) || expr.getKind().equals(BinaryOperatorKind.GT)){
	expr.setKind(BinaryOperatorKind.LE);
}
~~~

## 3 . Les sélecteurs ##

Les sélecteurs sont "??????". Le framework µTest dispose de diférents type de sélecteurs.

### Package ###
Définir un package dans le pom permet de dire que les mutations vont se porter uniquement sur les classes du package spécifié. Il est possible de définir plusieurs balises <package> englobées d'une unique balise <packages>.

### ComplexityLocator ###
"ComplexityLocator" permet de choisir si l'on mute une classe ou non en fonction d'une majoration de sa complexité cyclomatique.

INTRODUIRE CODE / IMAGE

## 4. Intégration dans un projet "demo" ##
L'utilisation du frameword µTest est relativement simple, et consiste en deux étapes : l'édition du pom.xml, et l'exécution.
### Edition du pom.xml ###
Il est nécessaire de modifier le pom.xml du projet maven que l'on souhaite faire muter. Il faut juste ajouter la balise plugin correspondant au framework µTest. 
~~~xml
<plugin>       
	<groupId>fr.unice.polytech.mojos</groupId>
	<artifactId>my-app</artifactId>
	<version>1.0-SNAPSHOT</version>
</plugin>
~~~

Il faut cepdant ajouter une balise <executions> qui permet de spécifier les goals que le framework va devoir atteindre. Il en existe quatre : generate-mutations, compile-mutants, mutation-test, et generate-report.

~~~xml
<executions>
	    <execution>
	        <goals>
	            <goal>generate-mutations</goal>
	            <goal>compile-mutants</goal>
	            <goal>mutation-test</goal>
	            <goal>generate-report</goal>
	        </goals>
	    </execution>
	</executions>
~~~

Le framework µTest a aussi besoin d'une balise <configuration> qui spécifie les processeurs, les sélecteurs et les packages.
~~~xml
<configuration>
	<processors>
	    <processor>UnaryIncrementMutation</processor>
	    <processor>ThisRemovalMutation</processor>
	</processors>
	<locators>
	    <locator>ComplexityLocator</locator>
	</locators>
	<packages>
	    <package>fr.unice.polytech.project.model</package>
	</packages>
</configuration>
~~~

Ce qui pourrait donner, par exemple : 

~~~xml
<plugin>
    <configuration>
        <processors>
            <processor>UnaryIncrementMutation</processor>
            <processor>ThisRemovalMutation</processor>
        </processors>
        <locators>
            <locator>ComplexityLocator</locator>
        </locators>
        <packages>
            <package>fr.unice.polytech.project.model</package>
        </packages>
    </configuration>
    <groupId>fr.unice.polytech.mojos</groupId>
    <artifactId>my-app</artifactId>
    <version>1.0-SNAPSHOT</version>
    <executions>
        <execution>
            <goals>
                <goal>generate-mutations</goal>
                <goal>compile-mutants</goal>
                <goal>mutation-test</goal>
                <goal>generate-report</goal>
            </goals>
        </execution>
    </executions>
</plugin>
~~~

### Exécution ###
