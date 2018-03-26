Divide the whole app into
static data,and dynamic data.

db_hashcode = 9002;

<!--
  * if the db hashcode is different, update the static files
  * update the dynamic files everytime we refresh.
 -->


Static: <br/>
    - Restaurant + Food + HomePage data <br/>
    - Basket <br/>
    - Favorite <br/>

* Get a json file for basket and Favorites: and store it to the database



Dynamic: <br/>
    - Commands <br/>
    - Transaction <br/>
    - Money account <br/>

<!--
 -->

 * The main activity doesnt load directly from online.
    The data is only updated if there is a broadcast


 * Restaurant / Menu / Food activities are also loaded
        from local database as they dont change that much
        often

 * COmmand/basket/favorite/addresses are loaded from online,
        otherwise from offline

Create the application apk with some basic data?
        Check if there is some basic data, if that is not
        the case, we just download a data_bundle.


# Use a data bundle for restaurant/food/food_tags/menus

These datas dont change everyday, so store them into databundle.


Home informations also will have their hown databundle that can
    be updated when a new bundle comes in.

// Verifier si la base de donnees sqlite est aussi maleable que
   ce que je pense, avec possibilite de mettre les choses comme
   nous le voulons.