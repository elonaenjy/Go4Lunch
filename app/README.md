# Go4LUNCH
Project 7 from OpenClassrooms  
Application collaborative de choix de restaurants
> Développé par Sandrine MAILLARD

## Ecran de connexion
Possibilité de connexion par compte Facebook, Google, Twitter ou mail
  
## Ecran Carte
* Affichage de la liste des 40 restaurants les plus proche de la localisation de l'utilisateur, déterminée par GPS, et avec les api Google APIMap et
APIPlace
* les restaurants indiqués en vert concernent les restaurants déjà choisis par d'autres collègues
* les restaurants indiqués en rouge concernent les restaurants non choisis

### La Liste des restaurants
* Affiche la même liste de restaurant que l'écran Carte mais sous la foirme d'une liste. Sur cette vue est mentyionnée pouer choisiraque restaurant
 le nombre de collaborateurs déjà inscrits
* La liste des restaurants indique à quelle distance vous vous trouvez du restaurant
* La liste affichée par défaut est une liste par distance croissante.choisir
* il est possible de trier par nom croissant (ou decroissant)des restaurant, par distance, par note, par nombre de collègues inscrits

## Détail du restaurant
Accessible à partir de l'écran listView quand on clique sur un item, à partir du choix "Your Lunch" disponible dans le menu drawer (si vous avez
déjà fait un choix), à partir de l'écran liste des collègues si le collègue concerné a déjà choisi un restaurant.
Cet écran comporte :
* Liste des collègues pour le restaurant affiché
* Nom et adress du Restaurant
* Avis sous forme d'étoile
* Possibilté d'appler directement le restaurant
* Possibilité de garder en favoris le restaurant
* Possibilté d'afficher la page web du restaurant
* Possibilté de choisir ce restaurant pour le déjeuner  
  

## Filtre par recherche en autocomplète  
Commencez à taper pour que la carte et la liste se mettent à jour en direct  
  

## Menu latéral
* Retrouvez rapidement votre choix pour le déjeuner
* Personalisez votre application en vhoisissant en particulier de recevoir ou pas des notifications
* Déconnectez vous  
  

## Notiffication
Recevez directement tous les jours à midi précise votre choix de resaturant et les collègues qui mangent avec vous
    
