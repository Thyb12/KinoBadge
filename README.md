## Conception du projet *KinoBadge*

Avant de démarrer le développement, j'ai identifier les besoins et organiser les fonctionnalités principales de l’application.

### 1. Écran principal

* **Liste de badges** : affichage des différents badges récupérés depuis l’API.
* **Affichage des badges** : présentation sous forme d’icône + titre.
* **Bouton de changement d’affichage** : Feature que je pourrais faire dans un second temps car je ne sais pas trop comment je pourrais afficher les badges avec la possibilité de basculer entre deux modes de visualisation des badges (compact ou étendu, avec barre de progression).

### 2. Détail d’un badge

Chaque badge peut être sélectionné pour afficher une page de détails comprenant :

* L’image (débloquée ou non selon l’état du badge).
* Le titre.
* La catégorie.
* La progression associée.
* La date de déblocage (si applicable).
* La description du badge.

### 3. Points complémentaires identifiés

* **Cache API** : réflexion sur la mise en place d’un cache. Dans la version retenue je em dit qu'il y a  un cache au niveau api mais possibiliter d'ameliorer le projet en rajoutant un cache.


Possible ajout
* **Accessibilité** :

  * Gestion de l’affichage avec des polices agrandies (*Big Font*).
