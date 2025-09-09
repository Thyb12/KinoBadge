## Conception du projet *KinoBadge*

Avant de démarrer le développement, une réflexion a été menée pour identifier les besoins et organiser les fonctionnalités principales de l’application.

### 1. Écran principal

* **Liste de badges** : affichage des différents badges récupérés depuis l’API.
* **Affichage des badges** : présentation sous forme d’icône + titre.
* **Bouton de changement d’affichage** : possibilité de basculer entre deux modes de visualisation des badges (compact ou étendu, avec barre de progression).

### 2. Détail d’un badge

Chaque badge peut être sélectionné pour afficher une page de détails comprenant :

* L’image (débloquée ou non selon l’état du badge).
* Le titre.
* La catégorie.
* La progression associée.
* La date de déblocage (si applicable).
* La description du badge.

### 3. Points complémentaires identifiés

* **Cache API** : réflexion sur la mise en place d’un cache. Dans la version retenue, un nouvel appel est effectué entre les vues pour simplifier la logique.
* **Accessibilité** :

  * Gestion de l’affichage avec des polices agrandies (*Big Font*).
  * Compatibilité avec **TalkBack** (lecteur d’écran Android).
