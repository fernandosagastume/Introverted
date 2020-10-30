<?php
    ini_set('display_errors', true);
    $user = (isset($_POST['user'])?$_POST['user']:'');
    $email = (isset($_POST['email'])?$_POST['email']:'');
    $pass = (isset($_POST['pass'])?$_POST['pass']:'');
    $bday = (isset($_POST['bday'])?$_POST['bday']:'');
    $gender = (isset($_POST['gender'])?$_POST['gender']:'');
    $typePerson = (isset($_POST['typePerson'])?$_POST['typePerson']:'');
    $drink = (isset($_POST['drink'])?$_POST['drink']:'');
    $smoke = (isset($_POST['smoke'])?$_POST['smoke']:'');
    $hobbies = (isset($_POST['hobbies'])?$_POST['hobbies']:'');
    $music_gender = (isset($_POST['music_gender'])?$_POST['music_gender']:'');
    $movies_gender = (isset($_POST['movies_gender'])?$_POST['movies_gender']:'');
    $food_type = (isset($_POST['food_type'])?$_POST['food_type']:'');
    $personal_list = (isset($_POST['personal_list'])?$_POST['personal_list']:'');
    
    $aficiones = preg_split("#/#", $hobbies);
        $musica = preg_split("#/#", $music_gender);
        $movies = preg_split("#/#", $movies_gender);
        $comida = preg_split("#/#", $food_type);
        $personal = preg_split("#/#", $personal_list);
    
    
    if(($user!='') && ($email!='') && ($pass!='') && ($bday!='') && ($gender!='') && 
        ($typePerson!='') && ($drink!='') && ($smoke!='') && ($hobbies!='') && ($music_gender!='')
            && ($movies_gender!='') && ($food_type!='') && ($personal_list!='')){
        $conec = new mysqli("localhost","root","","introverted");
        $conec1 = new mysqli("localhost","root","","introverted");
        $conec2 = new mysqli("localhost","root","","introverted");
        $conec3 = new mysqli("localhost","root","","introverted");
        if($conec->connect_error)die("No se pudo conectar");  
        if($conec1->connect_error)die("No se pudo conectar");  
        if($conec2->connect_error)die("No se pudo conectar");  
        if($conec3->connect_error)die("No se pudo conectar");  
        
        mysqli_set_charset($conec, "utf8");
        mysqli_set_charset($conec1, "utf8");
        mysqli_set_charset($conec2, "utf8");
        mysqli_set_charset($conec3, "utf8");
        
        $aficiones = preg_split("#/#", $hobbies);
        $musica = preg_split("#/#", $music_gender);
        $movies = preg_split("#/#", $movies_gender);
        $comida = preg_split("#/#", $food_type);
        $personal = preg_split("#/#", $personal_list);
        
        
        
        $query_gender = $conec->query("insert into genero(sexo) " . "values('$gender')") or die($conec->error);

        $genderID = $conec->insert_id;
        
        $query_userInfo = $conec1->query("INSERT INTO users(username, email, pass, bday, drink, smoke, generoId) "
                . "VALUES('$user','$email','$pass','$bday','$drink', '$smoke', '$genderID')") or die($conec1->error);
        
        $userID = $conec1->insert_id;
        $userIDCopy = $userID;
        
        $des = "Aun no esta implementado";
        $query_personType = $conec2->query("insert into persontype(tipo, descripcion) " 
                . "values('$typePerson', '$des')") or die($conec2->error);

        $persontype_ID = $conec2->insert_id;
        $pt_IDCopy = $persontype_ID ;
        $query_categoria = $conec3->query("insert into categoria(personTypeId, id) " 
               . "values('$persontype_ID', '$userID')") or die($conec3->error);
        
        
        foreach ($aficiones as $index => $hobby) {
            $query_aficiones = $conec3->query("insert into hobbies(hobbie) " 
                . "values('$hobby')") or die($conec2->error);
            $query_aficiones = $conec3->query("insert into likehobbie " 
                . "values('$conec3->insert_id', '$userIDCopy')") or die($conec2->error);
        }
        
        foreach ($musica as $index => $music_gen) {
            $query_musica = $conec3->query("insert into musica(musicGenre) " 
                . "values('$music_gen')") or die($conec2->error);
            $query_likemusica= $conec3->query("insert into likemusic " 
                . "values('$conec3->insert_id', '$userIDCopy')") or die($conec2->error);
        }
        
        foreach ($movies as $index => $peli) {
            $query_movies = $conec3->query("insert into peliculas(movieGenre) " 
                . "values('$peli')") or die($conec2->error);
            $query_likemovies = $conec3->query("insert into likemovie " 
                . "values('$conec3->insert_id', '$userIDCopy')") or die($conec2->error);
        }
        
        foreach ($comida as $index => $food) {
            $query_food = $conec3->query("insert into comida(foodType) " 
                . "values('$food')") or die($conec2->error);
            $query_likefood = $conec3->query("insert into likefood " 
                . "values('$conec3->insert_id', '$userIDCopy')") or die($conec2->error);
        }
        
        switch($typePerson){
            case "MELÓMANO":
                foreach ($personal as $index => $actividad) {
                $query_musicActivity = $conec3->query("insert into music_activities(actividad_musical) " 
                    . "values('$actividad')") or die($conec2->error);
                $query_likeMA = $conec3->query("insert into like_music_activities " 
                    . "values('$conec3->insert_id', '$userIDCopy')") or die($conec2->error);
                }
                break;
            case "BIBLIÓFILO":
                foreach ($personal as $index => $libro) {
                $query = $conec3->query("insert into libros(bookGenre) " 
                    . "values('$libro')") or die($conec2->error);
                $query = $conec3->query("insert into likebook " 
                    . "values('$conec3->insert_id', '$userIDCopy')") or die($conec2->error);
                }
                break;
            case "OTAKU":
                foreach ($personal as $index => $anime) {
                $query = $conec3->query("insert into anime(animeGenre) " 
                    . "values('$anime')") or die($conec2->error);
                $query = $conec3->query("insert into likeanime " 
                    . "values('$conec3->insert_id', '$userIDCopy')") or die($conec2->error);
                }
                break;
            case "GAMER":
                foreach ($personal as $index => $videojuego) {
                $query = $conec3->query("insert into videogame(videogameType) " 
                    . "values('$videojuego')") or die($conec2->error);
                $query = $conec3->query("insert into likevideogame " 
                    . "values('$conec3->insert_id', '$userIDCopy')") or die($conec2->error);
                }
                break;
            case "ARTISTA":
                foreach ($personal as $index => $arte) {
                $query = $conec3->query("insert into arte(artGenre) " 
                    . "values('$arte')") or die($conec2->error);
                $query = $conec3->query("insert into likeart " 
                    . "values('$conec3->insert_id', '$userIDCopy')") or die($conec2->error);
                }
                break;
        }
        
        echo $userIDCopy;
        mysqli_close($conec);

    }
    else {
        echo "0";
    }
?>