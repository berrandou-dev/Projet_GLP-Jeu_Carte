================================================================================
                   TU N'Y PEUX RIEN - Jeu de cartes
================================================================================

AUTEURS : KHODJA Laiza, BOUATMANE Nesrine, BERRANDOU Nassim

EXECUTION SOUS ECLIPSE :

1. Importer le projet :
   File -> Import -> Existing Projects into Workspace
   Selectionner le dossier racine du projet

2. Ajouter la librairie log4j (si besoin) :
   Clic droit sur le projet -> Build Path -> Configure Build Path
   Onglet Libraries -> Add External JARs
   Selectionner log4j-1.2.17.jar

3. Executer :
   Parcourir src/test/TestGame.java
   Clic droit -> Run As -> Java Application

REGLES DU JEU :
- 3 a 5 joueurs (humain + bots)
- Distribuer 5 cartes au debut
- Suivre la carte precedente (+1)
- Le 2 ecrase tout (sauf bombes)
- Les bombes (3+ cartes identiques) ecrasent tout
- Double Joker imbattable
- Pioche si pas de carte jouable

DIFFICULTES DES BOTS :
- Facile : joue aleatoirement
- Moyen : evite les bombes
- Difficile : strategique

BONNE PARTIE !
================================================================================
