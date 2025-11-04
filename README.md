# Projet Agent Intelligent – Maître de Jeu Interactif
## Objectif du projet

Ce projet a pour objectif de concevoir et développer un agent intelligent jouant le rôle d’un maître de jeu dans un jeu de rôle interactif.
L’agent raconte une histoire, décrit un univers et réagit dynamiquement aux choix ou actions de l’utilisateur.
Ce dernier peut interagir librement en langage naturel pour influencer le déroulement du scénario.

Le projet repose sur Ollama comme backend d’inférence locale, et met l’accent sur une architecture logicielle claire,
modulaire et robuste, conforme aux bonnes pratiques de programmation professionnelle.

## Fonctionnalités principales

- Génération et narration dynamique d’histoires interactives.
- Compréhension et gestion des actions de l’utilisateur en langage naturel.
- Interaction avec un modèle Ollama local (llama3.1).


## Instalation et exécution
1. Cloner le dépôt GitHub :
   ```bash
   git clone <URL_DU_DEPOT>
   ```
2. Naviguer dans le répertoire du projet :
   ```bash
    cd projet-agent
   ```
3. Lancer Ollama localement avec le modèle llama3.1 :
   ```bash
   ollama serve
   ```
4. Mettre à jour les variables d'environnement
   ```bash
   export APP_URL=http://localhost:11434/
   export APP_MODEL=llama3.1
   ```

5. Construire le projet avec Gradle :
   ```bash
   ./gradlew build
   ```
6. Exécuter l'application :
   ```bash
   ./gradlew run
   ```

## Utilisation de l’application
Lancez l'application et l'agent commencera à narrer une histoire. Vous pouvez interagir en entrant des commandes ou des actions en langage naturel. Par exemple :
- "I want to explore the dark forest."
- "Talk to the mysterious stranger." 

L'agent répondra en fonction de vos choix, adaptant l'histoire en conséquence.  
**L'agent fonctionne uniquement en anglais pour des raisons de compatibilité avec l'API donjon et dragon utiliser par l'agent.**


## Technologies utilisées

- Langage principal : Java
- Backend IA : Ollama
- Bibliothèques :
  - ```HttpClient``` pour les requêtes HTTP
  - ```Gson``` / ```Jackson``` pour la sérialisation JSON
  - ```JUnit ```/ ```Mockito``` pour les tests unitaires
  - ```Langchain4j``` pour l’intégration avec Ollama
  - ```Outils collaboratifs``` : GitHub

## Organisation de l’équipe

| Nom	               | Rôle	                                               | Responsabilités principales                                       |
|--------------------|-----------------------------------------------------|-------------------------------------------------------------------|
| Gabriel Monczewski | Chef de projet / Développeur / Testeur	             | Gestion du dépôt GitHub, Développement, test unitaires            |
| Florian Mordohai   | Architecte logiciel / Développeur / Documentation	 | Conception architecture logicielle, Développement, documentation  |
| Meryem Mellagui    | 	Développeur / Présentation                         | Développement d'outils et agents                                  |

## Bonnes pratiques appliquées

- Architecture modulaire : séparation claire entre les modules (API, agent, interface, logique de jeu).
- Programmation défensive : validation des entrées utilisateur, gestion centralisée des erreurs.
- Documentation du code : Javadoc détaillée et commentaires explicites.
- Contrôle de version rigoureux :
  - Commits fréquents et explicites (Utilisation de verbe à l'impératif, etc...)
  - Branches dédiées par fonctionnalité
  - Pull requests et code review avant fusion
- Tests unitaires : couverture des composants critiques (moteur de narration).
- Conventions Java : respect du style, nommage clair, indentation cohérente.

## Limites actuelles et pistes d’amélioration

- Gestion limitée des événements complexes ou combats interactifs.
- Potentiel futur :
  - Interface graphique (JavaFX ou web).
  - Sauvegarde de la session

## Auteurs
Projet réalisé par Meryem Mellagui, Gabriel Monczewski et Florian Mordohai dans le cadre du cours Programmation Professionnelle

Encadré par Fabien Escourbiac et Philippe Roussille

Année universitaire 2025