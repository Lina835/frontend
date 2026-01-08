Restaurant ASIATIK EXPRESS - Front-End (JavaFX)
Interface utilisateur graphique pour la consultation du catalogue et la prise de commande. Ce projet utilise une architecture organisée par packages pour séparer la logique de l'interface, les données et la communication réseau.

📋 Prérequis
Java 17 ou supérieur.

Maven (pour la gestion des dépendances).

Serveur Backend : Doit être lancé pour que les données s'affichent.

⚙️ Procédures pour générer le code
Pour installer les dépendances et compiler le projet, exécutez à la racine du dossier frontend :

___________________

mvn clean compile
___________________

🚀 Lancement du projet
Une fois la compilation terminée, lancez l'application avec :

_________________

mvn javafx:run
_________________


📂 Organisation du Projet (Structure)
Basé sur l'architecture du dossier src/main/java/fr/java/frontend :

api/ : Contient ApiClient.java pour la communication avec le serveur Backend.

cart/ : Gestion du panier d'achat (Cart, CartItem).

model/ : Définition des objets métiers (Category, Dish).

view/ : Contient toutes les vues de l'application :

CatalogueView : Affichage des produits.

DishDetailView : Détails et sélection des options (accompagnements/épices).

CartView & RecapulatifView : Gestion et résumé de la commande.

Router.java : Gère la navigation entre les différentes pages.

Main.java : Point d'entrée de l'application JavaFX.

✨ Fonctionnalités implémentées
Navigation Fluide : Système de routage personnalisé pour changer de vue.

Gestion du Panier : Ajout/Suppression d'articles en temps réel.

Options de Commande : Choix des accompagnements et épices intégrés dans la vue détail.

Statut du Store : Suivi de l'état du restaurant(OPEN / CLOSE) via le package util.

👤 Auteurs
Benhamza Alae
Benbaout Lina