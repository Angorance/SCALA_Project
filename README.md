# SCALA_Project

Scala project at HEIG-VD | 2019

## But

Le but de ce projet est de réaliser une application pour le Chill Out permettant de consulter les articles (infos, prix, popularité), d'attribuer des notes à ces derniers et de pouvoir demander un serveur s'il n'y a personne sur place. La requête serait alors transmise au staff (utilisateurs connectés) via une notification et un staff disponible pourra prévenir les autres qu'il s'en occupe.

Il y aurait donc une partie staff pour entrer les données (articles, prix, ...) et recevoir des notifications s'il y a besoin de quelqu'un au Chill Out. Cette partie a besoin d'un système d'authentification.

Et une partie accessible à tous pour voir les articles et leurs informations, pour pouvoir "appeler" un staff si personne n'est présent et pour pouvoir noter les articles. Pour cette partie, on part sans authentification, sur une base de confiance.

## Technologies

* Back-end en REST API.

* Front-end Android et/ou Web

* Base de données NoSQL, MongoDB avec le driver pour Slick. Le choix d'une base de données NoSQL parce que les articles du Chill Out peuvent avoir des données qui varient et le NoSQL apporte une plus grande flexibilité pour cela.

* Flutter dans le cas où on partirait sur on front-end Android. Plus rapide à prendre en main, cross-platform.