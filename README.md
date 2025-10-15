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
- Interaction avec un modèle Ollama local (llama3).

## Technologies utilisées

- Langage principal : Java
- Backend IA : Ollama
- Bibliothèques :
  - ```HttpClient``` pour les requêtes HTTP
  - ```Gson``` / ```Jackson``` pour la sérialisation JSON
  - ```JUnit ```/ ```Mockito``` pour les tests unitaires
- Outils collaboratifs : GitHub 

## Organisation de l’équipe

|   Nom	  |                 Rôle	                |                        Responsabilités principales                                   |
|---------|-----------------------------------------|--------------------------------------------------------------------------------------|
| [Nom 1] | Chef de projet / Développeur backend	| Gestion du dépôt GitHub, intégration avec l’API Ollama, gestion des requêtes         |
| [Nom 2] | Architecte logiciel / Développeur agent	| Conception du moteur de narration, gestion des états du jeu et logique d’interaction |
| [Nom 3] |	Développeur interface / Testeur	        | Développement de l’interface console, écriture des tests unitaires et documentation  |

## Bonnes pratiques appliquées

- Architecture modulaire : séparation claire entre les modules (API, agent, interface, logique de jeu).
- Programmation défensive : validation des entrées utilisateur, gestion centralisée des erreurs.
- Documentation du code : Javadoc détaillée et commentaires explicites.
- Contrôle de version rigoureux :
  - Commits fréquents et explicites (Utilisation de verbe à l'impératif, etc...)
  - Branches dédiées par fonctionnalité
  - Pull requests et code review avant fusion
- Tests unitaires : couverture des composants critiques (moteur de narration, API Ollama).
- Conventions Java : respect du style, nommage clair, indentation cohérente.

## Limites actuelles et pistes d’amélioration

- Absence de mémoire persistante entre sessions (à implémenter via fichiers JSON ou base de données).
- Gestion limitée des événements complexes ou combats interactifs.
- Potentiel futur :
  - Interface graphique (JavaFX ou web).
  - Sauvegarde de la session

## Auteurs
Projet réalisé par Meryem Mellagui, Gabriel Monczewski et Florian Mordohai dans le cadre du cours Programmation Professionnelle
Encadré par Fabien Escourbiac et Philippe Roussille
Année universitaire 2025