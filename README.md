# SCALA_Project

Scala project at HEIG-VD |  2019

Châtillon Jérémie, Gonzalez Lopez Daniel,  Smith James

## But

Le but de ce projet est de réaliser une  application pour le ChillOut permettant de consulter les articles (infos, prix,  popularité), d'attribuer des notes à ces derniers et de pouvoir demander un  serveur s'il n'y a personne sur place. La requête serait alors transmise aux  staffs via un groupe Telegram, où ils pourront s'organiser.

Il y aurait donc une partie staff pour pouvoir entrer les données (articles, prix, ...) qui nécessitera une authentification. 

Et une partie accessible à tous pour voir  les articles et leurs informations, pour pouvoir "appeler" un staff si personne  n'est présent et pour pouvoir noter les articles. Pour cette partie, on part  sans authentification, sur une base de confiance.

## Technologies
- Developpement de l'application en fullstack avec le framework Play (Scala)
- Base de données SQL avec Slick. Notre application n'ayant pas  de critère spécifique, partir sur une base de données SQL assez simple paraît  être la meilleure solution.

# Environment de test

Pour pouvoir run le projet en local, il faut lancer le docker-compose situé dans le dossie topology. Ce dernier lancera la base de données avec un phpmyadmin.

# Conception

Nous avons commencé par  réaliser un [UML](about:./Doc/Modelisation/SCALA_Project_UML.sly) de la base de  données. Cela nous a permis de savoir les différents modèles et DAO qu'on devait  développer.

La partie complexe de notre  modélisation fut de modéliser les bières (*beers*) et les  boissons sans alcool (*softdrinks*). Toutes deux possèdent des  caractéristiques similaires: nom, prix, description, image, etc., mais les  bières possédées des attributs supplémentaires comme le taux  d'alcool.

Nous avons décidé de faire une table  boissons (drinks) contenant toutes les propriétés communes aux bières et aux  boissons sans alcool et de faire une table pour les bières et une table pour les  boissons sans alcool. Toutes deux auront une clé étrangère vers la table des  boissons. Ce sera l'unique attribut de la table des boissons sans alcool, mais  la table des bières possèdera des propriétés supplémentaires comme son taux  d'alcool ou encore sa provenance.

Le choix de faire une table des boissons  sans alcool contenant uniquement un lien avec la table drink fut fait pour  pouvoir récupérer uniquement les boissons sans alcool au  besoin.

# Réalisation

Nous avons commencé à faire les  différentes DAO pour accéder aux différentes tables. Le manque de compréhension  de la technologie et par la complexité de notre lien entre les tables bières,  boissons sans alcool et boisson nous a relativement compliqué la  tâche.

Nous étions partis dans un  premier temps sur le développement d'une API et d'un frontend développé en  Angular, mais la complexité des données nous a poussés à changer pour faire une  application *FullStack*. Nous avons donc dû refaire  tous les contrôleurs pour qu'ils retournent des vues. Cela nous à fait perdre un  temps considérable.

Par manque de temps, nous nous sommes  fixé de faire:

- La demande d'un serveur via un chanel *Telegram*.
- L'affichage des bières et des boissons sans  alcool.
- L'ajout de nouvelles bières.

## Partie Telegram

Pour cette partie, nous avons 2 choses. Tout d'abord, il y a un bouton accessible depuis la page princiale qui sert à faire une demande de staff. Ensuite, il y a une page formulaire accessible depuis le menu qui permet de changer le chat sur lequel le bot Telegram envoie la requête.

La création du bot Telegram s'est faite très facilement grâce au bot officiel prévu de Telegram. Ensuite, il a simplement fallu trouver l'id du chat (plusieurs bot disponibles le permettent).

Au niveau Play/Slick, nous avons créé une DAO pour le chat de sorte à pouvoir récupérer et changer le id du chat. Pour cette partie, nous avons pris la décision de ne garder qu'un seul id à la fois, de manière à ce que le bot ne puisse être "associé" qu'à un seul chat à la fois. Nous avons donc une fonction qui supprime tout et ajoute le nouveau chatId lorsqu'on remplit le formulaire prévu à cet effet.

Ensuite, nous avons simplement ajouté un contrôleur lié au bouton pour demander un staff, nous avons fait appel à la DAO pour récupérer le chatId et utilisé ce dernier pour envoyer un message sur le groupe en question grâce à l'API Telegram et la méthode sendMessage.

Tout ceci est fonctionnel. Les seules choses qu'il reste à faire c'est :

1. Mettre le token du bot Telegram dans des variables d'environnement (actuellement en dur dans le code, pas safe)
2. Mettre l'accès au formulaire pour changer le chat id dans la partie admin.

# Problèmes rencontrés

Notre manque de connaissance  dans *Play* et *Slick* nous a posé un  nombre de problèmes considérable et une perte de temps folle. Nous avons dû nous  reprendre à plusieurs fois sur la modélisation de nos *modèles*, *DAO* et *controllers*. 

Nous avons pas bien compris la génération  de formulaire de Play, ce dernier permet de faire une validation de champs, mais  permet de prendre des types étonnant comme:

- [`text`](https://www.playframework.com/documentation/2.6.x/api/scala/play/api/data/Forms$.html#text%3AMapping[String]): maps to `scala.String`, optionally takes `minLength` and `maxLength`.
- [`nonEmptyText`](https://www.playframework.com/documentation/2.6.x/api/scala/play/api/data/Forms$.html#nonEmptyText%3AMapping[String]): maps to  `scala.String`, optionally takes `minLength` and `maxLength`.
- [`number`](https://www.playframework.com/documentation/2.6.x/api/scala/play/api/data/Forms$.html#number%3AMapping[Int]): maps to `scala.Int`, optionally takes `min`, `max`, and `strict`.
- [`longNumber`](https://www.playframework.com/documentation/2.6.x/api/scala/play/api/data/Forms$.html#longNumber%3AMapping[Long]): maps to  `scala.Long`, optionally takes `min`, `max`, and `strict`.
- [`bigDecimal`](https://www.playframework.com/documentation/2.6.x/api/scala/play/api/data/Forms$.html#bigDecimal%3AMapping[BigDecimal]): takes  `precision` and `scale`.
- [`date`](https://www.playframework.com/documentation/2.6.x/api/scala/play/api/data/Forms$.html#date%3AMapping[Date]), [`sqlDate`](https://www.playframework.com/documentation/2.6.x/api/scala/play/api/data/Forms$.html#sqlDate%3AMapping[Date]): maps to `java.util.Date`, `java.sql.Date`, optionally takes `pattern` and `timeZone`.
- [`email`](https://www.playframework.com/documentation/2.6.x/api/scala/play/api/data/Forms$.html#email%3AMapping[String]): maps to `scala.String`, using an email regular  expression.
- [`boolean`](https://www.playframework.com/documentation/2.6.x/api/scala/play/api/data/Forms$.html#boolean%3AMapping[Boolean]): maps to `scala.Boolean`.
- [`checked`](https://www.playframework.com/documentation/2.6.x/api/scala/play/api/data/Forms$.html#checked%3AMapping[Boolean]): maps to `scala.Boolean`.
- [`optional`](https://www.playframework.com/documentation/2.6.x/api/scala/play/api/data/Forms$.html): maps to `scala.Option`.

*Source: la doc officielle de Play*

Mais aucun de ces types ne se  map directement avec un *float*. Celui qui s'en  rapproche le plus et le *bigDecimal,*  mais après des heures de recherches et de tests posent encore  problème.

De plus, les formulaires nécessitent des  *MessagesProvider* avec lesquels, nous avons rencontré des problèmes de  version.

La personnalisation de ces formulaires  avec notre propre style nous a paru plus complexe que ça ne devrait  l'être.

Tous ces petits problèmes accumulés nous  firent perdre un temps qui nous empêcha de faire plus de  fonctionnalité.

Par manque de temps, nous n'avons pas pu implémenter:

- La gestion des sessions
- L'ajout de boisson
- La modification de boisson
- Les votes/ratings
- Le detail d'une boisson

# Conclusion

Les problèmes rencontrés ne  furent pas liés aux manques de connaissances de *Scala,* mais  à l'utilisation de Play et Slick qui furent littéralement un enfer. Nous ne  voyons aucun cas où nous choisirons cette technologie dans un  futur projet  comparé à toutes les autres technologies sur le marché actuel. Nous avons,  finalement, que très peu codé en Scala et nous n'avons pas pu mettre en  pratique les forces de ce langage et du paradigme fonctionnel.  
