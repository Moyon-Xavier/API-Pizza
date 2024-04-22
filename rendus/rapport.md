# Pizzeria Pour Les Developeur - API


# Commencer :

## Fonctionnement Général:

Nous avons décidé que notre API etait ouverte, c'est pour cela que vous n'avez pas besoin d'un token pour lire des données,
Cependant si vous souhaitez y apporter des modifications vous devez créer un compte[^1] afin de pouvoir récuperer un token

[^1]: Pour créer un compte contacter manel.boumansour.etu@univ-lille.fr ou xavier.moyon.etu@univ-lille.fr


## Authentification

Bravo vous avez créer un compte, vous pouvez maintenant modifier nos données a condition d'utiliser votre token...
Mais comment récuperer un token ? 
C'est très simple utiliser cette requete 

   

    curl -i -H 'Content-Type: application/json' -d '{"login":"Xavier","password":"passXavier"}' -X GET http://localhost:8080/sae-boumansour-moyon/users/token

Vous pouvez maintenant récuperer votre token et profiter d'un acces illimité ;) , vous avez simplement a ajouter ceci a votre commande curl :
    
    curl .... -H 'Authorization: BASIC monToken'


## Les différents Données

- Ingredients
    - id
    - name
    - price
- Pizzas
  - pno
  - nom
  - prix
  - finalPrice
  - compo
- Commandes 

## Ingredients : 
Voici les différentes requetes que vous pouvez effectuer sur les ingredients
### Get 
---
- Pour obtenir la collection de tous les ingrédients :

        curl -i -H 'Content-Type: application/json' http://localhost:8080/sae-boumansour-moyon/ingredient/ 
 
    Et retourne ce genre de valeur : 

        [{"id":2,"name":"Chèvre","price":3.0},{"id":3,"name":"Miel","price":2.0},{"id":4,"name":"Poivrons","price":1.5},{"id":5,"name":"Mozzarella","price":1.5},{"id":6,"name":"ciboulette","price":0.5},{"id":7,"name":"capres","price":2.0},{"id":8,"name":"Creme fraiche","price":2.0}]
    
---
- Pour obtenir un ingrédient particulier : 

        curl -i -H 'Content-Type: application/json' http://localhost:8080/sae-boumansour-moyon/ingredient/{id} 

    Et retourne ce genre de valeur : 

        {"id":2,"name":"Chèvre","price":3.0}

    Erreur :

    - HTTP Status 404 – Not Found - Si l'ingredient n'existe pas
---
- Pour obtenir uniquement le nom d’un ingrédient spécifique : 

      curl -i -H 'Content-Type: application/json' http://localhost:8080/sae-boumansour-moyon/ingredient/{id}/name 

    Et retourne ce genre de valeur : 
    
        "Chèvre" // Chevre pour fromage de chèvre, on ne veut pas de 
        //De Chevres dans nos pizza

    Erreur :

    - HTTP Status 404 – Not Found 
      - Si l'ingredient n'existe pas

### POST 
---
- Pour ajouter un nouvel ingrédient

      curl -i -H 'Content-Type:application/json' -d '{"name":"Avocats","price":"2.0"}' -H 'Authorization: Basic {token}' -X POST http://localhost:8080/sae-boumansour-moyon/ingredient/

    Et retourne ceci :

        {"id":12,"name":"Avocats","price":2.0}
    
    Erreurs :
    - HTTP Status 500 – Internal Server Error 
        - Si le token n'est pas sous le bon format
        - Si l'ingredient n'est pas sous le bon format (attribut mal nommés)
    - HTTP Status 404 – Not Found 
      - Si l'ingredient n'existe pas 
    -  HTTP Status 403 – Forbidden
       - Si le token n'est pas celui d'un admin
    - HTTP Status 409 – Conflict
      - Si un ingredient ayant le meme id existe déjá
  
### DELETE 
---
- pour supprimer un ingrédient existant


       curl -i -H 'Content-Type:application/json' -d '{"name":"Avocats","price":"2.0"}' -H 'Authorization: Basic {token}' -X DELETE http://localhost:8080/sae-boumansour-moyon/ingredient/1

    Et retourne ceci :

        {"id":1,"name":"Avocats","price":2.0}

    Erreurs : 
    - HTTP Status 500 – Internal Server Error 
        - Si le token n'est pas sous le bon format
    - HTTP Status 404 – Not Found 
      - Si l'ingredient n'existe pas 
    -  HTTP Status 403 – Forbidden
       - Si le token n'est pas celui d'un admin

    
### PUT 
---
- Pour modifier la totalité d'un ingredient : 

        curl -d '{"name":"Frite"} '  -X PUT http://localhost:8080/sae-boumansour-moyon/ingredient/6 -H 'Authorization: Basic token'

    Et retourne l'objet modifié :

        {"id":6,"name":"Frite","price":0.0}
    

### Patch 
---
- Pour modifier seulement une partie d'un ingredient :
    
        curl -d '{"price":1200} '  -X PATCH http://localhost:8080/sae-boumansour-moyon/ingredient/7 -H 'Authorization: Basic monToken'
    Et retourne l'objet modifier : 
        
        {"id":7,"name":"capres","price":1200.0}




## Pizza

### GET 
--- 
- Pour obtenir la collection de toutes les pizzas
    
    
        curl -X GET http://localhost:8080/sae-boumansour-moyon/pizza/ 
    Et retourne une listes de pizzas avec leurs ingredients et le prix total de la pizza : 

        [{"pno":4,"nom":"Orientale","prix":4.0,"finalPrice":4.0,"compo":[],"prixFinal":4.0},{"pno":2,"nom":"Chèvre Miel","prix":4.0,"finalPrice":4.0,"compo":[],"prixFinal":4.0},{"pno":1,"nom":"Norvégienne","prix":4.0,"finalPrice":1206.0,"compo":[{"id":6,"name":"Frite","price":0.0},{"id":7,"name":"capres","price":1200.0},{"id":8,"name":"Creme fraiche","price":2.0}],"prixFinal":1206.0},{"pno":3,"nom":"Margherita","prix":4.0,"finalPrice":4.0,"compo":[],"prixFinal":4.0}]


---
- Obtenir une pizza en particulier 

        curl -X GET http://localhost:8080/sae-boumansour-moyon/pizza/1

    Et retourne un JSON de l'objet pizza demandé : 

        {"pno":1,"nom":"Norvégienne","prix":4.0,"finalPrice":1206.0,"compo":[{"id":6,"name":"Frite","price":0.0},{"id":7,"name":"capres","price":1200.0},{"id":8,"name":"Creme fraiche","price":2.0}],"prixFinal":1206.0}


- Pour obtenir le prix final de la pizza

        curl -i -H 'Content-Type:application/json' -X GET http://localhost:8080/sae-boumansour-moyon/pizza/1/prixfinal
    Et retourne ceci :
    
        8.0


### POST
---
- Pour ajouter un ingrédient à une pizza 


        curl -i  -H 'Content-Type:application/json' -H 'Authorization: Basic WGF2aWVyOnBhc3NYYXZpZXI=' -X POST http://localhost:8080/sae-boumansour-moyon/pizza/6/5

Et retourne la pizza modifié :

        {"pno":6,"nom":"Etrange","prix":4.0,"finalPrice":5.0,"compo":[{"id":5,"name":"Mozzarella","price":1.5},{"id":6,"name":"Frite","price":0.0}],"prixFinal":5.0}
---
- Ajout d’une nouvelle pizza avec ses ingrédients

        curl -i -d '{"pno":244455577,"nom":"Etrange","prix":4,"compo":[1,2,3,4]}' -H 'Content-Type:application/json' -H 'Authorization: Basic WGF2aWVyOnBhc3NYYXZpZXI=' -X POST http://localhost:8080/sae-boumansour-moyon/pizza/
    
    Et retourne la pizza créé :
            
        {"pno":244455577,"nom":"Etrange","prix":4.0,"finalPrice":10.0,"compo":[{"id":2,"name":"Chèvre","price":3.0},{"id":3,"name":"Miel","price":2.0},{"id":4,"name":"Poivrons","price":1.5}],"prixFinal":10.0}
--- 
### PUT 
- Modifie toute la pizza en ne spécifiant que le nom.
  
        curl -i -d '{"nom":"Pizza Illies"}' -H 'Content-Type:application/json' -H 'Authorization: Basic WGF2aWVyOnBhc3NYYXZpZXI=' -X PUT http://localhost:8080/sae-boumansour-moyon/pizza/6

    Et retourne la pizza modifié :

        {"pno":6,"nom":"Pizza Illies","prix":4.0,"finalPrice":4.0,"compo":[],"prixFinal":4.0}
---
- On peut spécifié plusieurs element dans le PUT dont des ingrédients qui n'existent pas, cela n'aura pas de conséquence, l'ingrédient ne sera pas ajouté


        curl -i -d '{"nom":"Pizza Illies","compo":[-11111,1,2,3,4,1111]}' -H 'Content-Type:application/json' -H 'Authorization: Basic WGF2aWVyOnBhc3NYYXZpZXI=' -X PUT http://localhost:8080/sae-boumansour-moyon/pizza/6
    Et retourne
    
        {"pno":6,"nom":"Pizza Illies","prix":4.0,"finalPrice":10.0,"compo":[{"id":2,"name":"Chèvre","price":3.0},{"id":3,"name":"Miel","price":2.0},{"id":4,"name":"Poivrons","price":1.5}],"prixFinal":10.0}



### PATCH 


---
- Modification d’un attribut d’une pizza (le prix augmente)
    
        curl -i -H 'Content-Type:application/json' -d '{"compo":[5,444]}' -H 'Authorization: Basic WGF2aWVyOnBhc3NYYXZpZXI=' -X PATCH http://localhost:8080/sae-boumansour-moyon/pizza/1

    Et retourne : 

        {"pno":1,"nom":"Norvégienne","prix":4.0,"finalPrice":5.0,"compo":[{"id":5,"name":"Mozzarella","price":1.5}],"prixFinal":5.0}

### DELETE /pizzas/{id} suppression d’une pizza
DELETE /pizzas/{id}/{idIngredient} 

---
- suppression d’un ingrédient d’une pizza
        
        curl -i -H 'Content-Type:application/json' -H 'Authorization: Basic WGF2aWVyOnBhc3NYYXZpZXI=' -X DELETE http://localhost:8080/sae-boumansour-moyon/pizza/4/4

    Et retourne null si cela fonctionne :
        
        null

    HTTP Status 404 – Not Found

---
- Suppression d'une pizza :

        curl -i -H 'Content-Type:application/json' -H 'Authorization: Basic WGF2aWVyOnBhc3NYYXZpZXI=' -X DELETE http://localhost:8080/sae-boumansour-moyon/pizza/4

    Et retourne cela si l'operation fonctionne :

        null
## Base de Données 

    
### MCD

#### Premiere Version 
![Premiere Version du MCD](../ressources/img/MCD.png)

### MLD 

#### Premiere Version
Ingredient(<u>ino</u>,nom,prix)

Pizza(<u>pno</u>,nom,pâte,prix)

Commandes(<u>cno</u>,userName,date,prix)

Contient(<u>#nomPizza,#cno</u>)

Composition(<u>#nomPizza,#ino</u>)

### SCRIPTS DE CREATION DE TABLE
 Je le fais ce soir
## Architecture du projet java

