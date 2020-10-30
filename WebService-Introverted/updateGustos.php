<?php

    $id = (isset($_POST['id_us'])?$_POST['id_us']:'');
    $gusto = (isset($_POST['gusto'])?$_POST['gusto']:'');
    $changeList = (isset($_POST['changeList'])?$_POST['changeList']:'');
    
    $conec = new mysqli("localhost","root","","introverted");
    if($conec->connect_error)die("No se pudo conectar");
    mysqli_set_charset($conec, "utf8");
    
    $query = "";
    if($id!='' && $changeList!='' && $gusto!=''){
        if($gusto == 'music'){
            $musicG = preg_split("#/#", $changeList);
            
            $consulta = "SELECT * FROM likemusic lm "
                    . "INNER JOIN musica m"
                    . " ON lm.musicId = m.musicId WHERE lm.id = '$id'";
            $query = $conec->query($consulta) or die($conec->error);
            
            $queryResult = array();
            foreach($query as $row){
                $queryResult[] = $row;
            }
            
            //Se actualiza en base de datos los generos
            if(count($queryResult) == count($musicG)){
                $cont = 0;
                foreach ($musicG as $index => $music_gen) {
                    $mG = $queryResult[$cont]['musicId'];
                    $query_musica = $conec->query("update musica set musicGenre = '$music_gen' "
                            . "where musicId = '$mG'") or die($conec->error);
                    $cont++;
                }
            } else if (count($queryResult) > count($musicG)){
                $cont = 0;
                foreach ($musicG as $index => $music_gen) {
                    $mG = $queryResult[$cont]['musicId'];
                    $query_musica = $conec->query("update musica set musicGenre = '$music_gen' "
                            . "where musicId = '$mG'") or die($conec->error);
                    $cont++;
                }
                for($i=$cont; $i < count($queryResult); $i++){
                    $mG = $queryResult[$i]['musicId'];
                    $query_musica = $conec->query("delete from musica "
                            . "where musicId = '$mG'") or die($conec->error);
                    $query_musica1 = $conec->query("delete from likemusic "
                            . "where musicId = '$mG' and id = '$id'") or die($conec->error);
                }
            }
            else {
                $cont = 0;
                foreach ($musicG as $index => $music_gen) {
                    if($cont < count($queryResult)){
                        $mG = $queryResult[$cont]['musicId'];
                        $query_musica = $conec->query("update musica set musicGenre = '$music_gen' "
                                . "where musicId = '$mG'") or die($conec->error);
                    }
                    else {
                        $query_musica = $conec->query("insert into musica(musicGenre) " 
                        . "values('$music_gen')") or die($conec->error);
                        $query_likemusica= $conec->query("insert into likemusic " 
                            . "values('$conec->insert_id', '$id')") or die($conec->error);
                    }
                    $cont++;
                }
            }
            echo "1";
        }
        else if($gusto == 'movies'){
            $movieG = preg_split("#/#", $changeList);
            
            $consulta = "SELECT * FROM likemovie lm "
                    . "INNER JOIN peliculas p"
                    . " ON lm.movieId = p.movieId WHERE lm.id = '$id'";
            $query = $conec->query($consulta) or die($conec->error);
            
            $queryResult = array();
            foreach($query as $row){
                $queryResult[] = $row;
            }
            
            //Se actualiza en base de datos los generos
            if(count($queryResult) == count($movieG)){
                $cont = 0;
                foreach ($movieG as $index => $movie_gen) {
                    $mG = $queryResult[$cont]['movieId'];
                    $query_movies = $conec->query("update peliculas set movieGenre = '$movie_gen' "
                            . "where movieId = '$mG'") or die($conec->error);
                    $cont++;
                }
            } else if (count($queryResult) > count($movieG)){
                $cont = 0;
                foreach ($movieG as $index => $movie_gen) {
                    $mG = $queryResult[$cont]['movieId'];
                    $query_movies = $conec->query("update peliculas set movieGenre = '$movie_gen' "
                            . "where movieId = '$mG'") or die($conec->error);
                    $cont++;
                }
                for($i=$cont; $i < count($queryResult); $i++){
                    $mG = $queryResult[$i]['movieId'];
                    $query_movies = $conec->query("delete from peliculas "
                            . "where movieId = '$mG'") or die($conec->error);
                    $query_movies1 = $conec->query("delete from likemovie "
                            . "where movieId = '$mG' and id = '$id'") or die($conec->error);
                }
            }
            else {
                $cont = 0;
                foreach ($movieG as $index => $movie_gen) {
                    if($cont < count($queryResult)){
                        $mG = $queryResult[$cont]['movieId'];
                        $query_movies = $conec->query("update peliculas set movieGenre = '$movie_gen' "
                                . "where movieId = '$mG'") or die($conec->error);
                    }
                    else {
                        $query_movies = $conec->query("insert into peliculas(movieGenre) " 
                        . "values('$movie_gen')") or die($conec->error);
                        $query_likemovies= $conec->query("insert into likemovie " 
                            . "values('$conec->insert_id', '$id')") or die($conec->error);
                    }
                    $cont++;
                }
            }
            echo "1";
        }
        else if($gusto == 'hobbies'){
            $hobbieList = preg_split("#/#", $changeList);
            
            $consulta = "SELECT * FROM likehobbie lh "
                    . "INNER JOIN hobbies h"
                    . " ON lh.hobbyId = h.hobbyId WHERE lh.id = '$id'";
            $query = $conec->query($consulta) or die($conec->error);
            
            $queryResult = array();
            foreach($query as $row){
                $queryResult[] = $row;
            }
            
            //Se actualiza en base de datos los hobbies
            if(count($queryResult) == count($hobbieList)){
                $cont = 0;
                foreach ($hobbieList as $index => $hobbie) {
                    $hobby = $queryResult[$cont]['hobbyId'];
                    $query_hobbies = $conec->query("update hobbies set hobbie = '$hobbie' "
                            . "where hobbyId = '$hobby'") or die($conec->error);
                    $cont++;
                }
            } else if (count($queryResult) > count($hobbieList)){
                $cont = 0;
                foreach ($hobbieList as $index => $hobbie) {
                    $hobby = $queryResult[$cont]['hobbyId'];
                    $query_hobbies = $conec->query("update hobbies set hobbie = '$hobbie' "
                            . "where hobbyId = '$hobby'") or die($conec->error);
                    $cont++;
                }
                for($i=$cont; $i < count($queryResult); $i++){
                    $hobby = $queryResult[$i]['hobbyId'];
                    $query_hobbies = $conec->query("delete from hobbies "
                            . "where hobbyId = '$hobby'") or die($conec->error);
                    $query_hobbies1 = $conec->query("delete from likehobbie "
                            . "where hobbyId = '$hobby' and id = '$id'") or die($conec->error);
                }
            }
            else {
                $cont = 0;
                foreach ($hobbieList as $index => $hobbie) {
                    if($cont < count($queryResult)){
                        $hobby = $queryResult[$cont]['hobbyId'];
                        $query_hobbies = $conec->query("update hobbies set hobbie = '$hobbie' "
                                . "where hobbyId = '$hobby'") or die($conec->error);
                    }
                    else {
                        $query_hobbies = $conec->query("insert into hobbies(hobbie) " 
                        . "values('$hobbie')") or die($conec->error);
                        $query_likehobbie= $conec->query("insert into likehobbie " 
                            . "values('$conec->insert_id', '$id')") or die($conec->error);
                    }
                    $cont++;
                }
            }
            echo "1";
        }
        else if($gusto == 'food'){
            $foodT = preg_split("#/#", $changeList);
            
            $consulta = "SELECT * FROM likefood lf "
                    . "INNER JOIN comida c"
                    . " ON lf.foodId = c.foodId WHERE lf.id = '$id'";
            $query = $conec->query($consulta) or die($conec->error);
            
            $queryResult = array();
            foreach($query as $row){
                $queryResult[] = $row;
            }
            
            //Se actualiza en base de datos los hobbies
            if(count($queryResult) == count($foodT)){
                $cont = 0;
                foreach ($foodT as $index => $food_type) {
                    $foody = $queryResult[$cont]['foodId'];
                    $query_food = $conec->query("update comida set foodType = '$food_type' "
                            . "where foodId = '$foody'") or die($conec->error);
                    $cont++;
                }
            } else if (count($queryResult) > count($foodT)){
                $cont = 0;
                foreach ($foodT as $index => $food_type) {
                    $foody = $queryResult[$cont]['foodId'];
                    $query_food = $conec->query("update comida set foodType = '$food_type' "
                            . "where foodId = '$foody'") or die($conec->error);
                    $cont++;
                }
                for($i=$cont; $i < count($queryResult); $i++){
                    $foody = $queryResult[$i]['foodId'];
                    $query_food = $conec->query("delete from comida "
                            . "where foodId = '$foody'") or die($conec->error);
                    $query_food1 = $conec->query("delete from likefood "
                            . "where foodId = '$foody' and id = '$id'") or die($conec->error);
                }
            }
            else {
                $cont = 0;
                foreach ($foodT as $index => $food_type) {
                    if($cont < count($queryResult)){
                        $foody = $queryResult[$cont]['foodId'];
                        $query_food = $conec->query("update comida set foodType = '$food_type' "
                                . "where foodId = '$foody'") or die($conec->error);
                    }
                    else {
                        $query_food = $conec->query("insert into comida(foodType) " 
                        . "values('$food_type')") or die($conec->error);
                        $query_likefood= $conec->query("insert into likefood " 
                            . "values('$conec->insert_id', '$id')") or die($conec->error);
                    }
                    $cont++;
                }
            }
            echo "1";
        }
        else if($gusto == 'categoria_personal'){
            $consulta = "SELECT tipo FROM users u 
                         INNER JOIN categoria c
                         INNER JOIN persontype p
                         ON u.id = c.id && c.personTypeId = p.personTypeId
                         WHERE u.id = '$id'";
            
        $query = $conec->query($consulta) or die($conec->error);
        $arr = array();
        foreach($query as $row){
            $arr[] = $row;
        }
        
        if($arr[0]['tipo'] == "ARTISTA"){
            
            $personalCat = preg_split("#/#", $changeList);
            
            $consulta = "SELECT * FROM likeart la "
                    . "INNER JOIN arte a"
                    . " ON la.artId = a.artId WHERE la.id = '$id'";
            $query = $conec->query($consulta) or die($conec->error);
            
            $queryResult = array();
            foreach($query as $row){
                $queryResult[] = $row;
            }
            
            //Se actualiza en base de datos los generos de arte
            if(count($queryResult) == count($personalCat)){
                $cont = 0;
                foreach ($personalCat as $index => $personal_GenOrType) {
                    $aID = $queryResult[$cont]['artId'];
                    $query_personalCat = $conec->query("update arte set artGenre = '$personal_GenOrType' "
                            . "where artId = '$aID'") or die($conec->error);
                    $cont++;
                }
            } else if (count($queryResult) > count($personalCat)){
                $cont = 0;
                foreach ($personalCat as $index => $personal_GenOrType) {
                    $aID = $queryResult[$cont]['artId'];
                    $query_personalCat = $conec->query("update arte set artGenre = '$personal_GenOrType' "
                            . "where artId = '$aID'") or die($conec->error);
                    $cont++;
                }
                for($i=$cont; $i < count($queryResult); $i++){
                    $aID = $queryResult[$i]['artId'];
                    $query_personalCat = $conec->query("delete from arte "
                            . "where artId = '$aID'") or die($conec->error);
                    $query_personalCat1 = $conec->query("delete from likeart "
                            . "where artId = '$aID' and id = '$id'") or die($conec->error);
                }
            }
            else {
                $cont = 0;
                foreach ($personalCat as $index => $personal_GenOrType) {
                    if($cont < count($queryResult)){
                        $aID = $queryResult[$cont]['artId'];
                        $query_personalCat = $conec->query("update arte set artGenre = '$personal_GenOrType' "
                                . "where artId = '$aID'") or die($conec->error);
                    }
                    else {
                        $query_personalCat = $conec->query("insert into arte(artGenre) " 
                        . "values('$personal_GenOrType')") or die($conec->error);
                        $query_likeart = $conec->query("insert into likeart " 
                            . "values('$conec->insert_id', '$id')") or die($conec->error);
                    }
                    $cont++;
                }
            }
            echo "1";
        } 
        else if ($arr[0]['tipo'] == "OTAKU"){
            $personalCat = preg_split("#/#", $changeList);
            
            $consulta = "SELECT * FROM likeanime la "
                    . "INNER JOIN anime a"
                    . " ON la.animeId = a.animeId WHERE la.id = '$id'";
            $query = $conec->query($consulta) or die($conec->error);
            
            $queryResult = array();
            foreach($query as $row){
                $queryResult[] = $row;
            }
            
            //Se actualiza en base de datos los generos de anime
            if(count($queryResult) == count($personalCat)){
                $cont = 0;
                foreach ($personalCat as $index => $personal_GenOrType) {
                    $aID = $queryResult[$cont]['animeId'];
                    $query_personalCat = $conec->query("update anime set animeGenre = '$personal_GenOrType' "
                            . "where animeId = '$aID'") or die($conec->error);
                    $cont++;
                }
            } else if (count($queryResult) > count($personalCat)){
                $cont = 0;
                foreach ($personalCat as $index => $personal_GenOrType) {
                    $aID = $queryResult[$cont]['animeId'];
                    $query_personalCat = $conec->query("update anime set animeGenre = '$personal_GenOrType' "
                            . "where animeId = '$aID'") or die($conec->error);
                    $cont++;
                }
                for($i=$cont; $i < count($queryResult); $i++){
                    $aID = $queryResult[$i]['animeId'];
                    $query_personalCat = $conec->query("delete from anime "
                            . "where animeId = '$aID'") or die($conec->error);
                    $query_personalCat1 = $conec->query("delete from likeanime "
                            . "where animeId = '$aID' and id = '$id'") or die($conec->error);
                }
            }
            else {
                $cont = 0;
                foreach ($personalCat as $index => $personal_GenOrType) {
                    if($cont < count($queryResult)){
                        $aID = $queryResult[$cont]['animeId'];
                        $query_personalCat = $conec->query("update anime set animeGenre = '$personal_GenOrType' "
                                . "where animeId = '$aID'") or die($conec->error);
                    }
                    else {
                        $query_personalCat = $conec->query("insert into anime(animeGenre) " 
                        . "values('$personal_GenOrType')") or die($conec->error);
                        $query_likeanime = $conec->query("insert into likeanime " 
                            . "values('$conec->insert_id', '$id')") or die($conec->error);
                    }
                    $cont++;
                }
            }
            echo "1";
        }
        else if ($arr[0]['tipo'] == "GAMER"){
            $personalCat = preg_split("#/#", $changeList);
            
            $consulta = "SELECT * FROM likevideogame lvg "
                    . "INNER JOIN videogame vg"
                    . " ON lvg.videogameId = vg.videogameId WHERE lvg.id = '$id'";
            $query = $conec->query($consulta) or die($conec->error);
            
            $queryResult = array();
            foreach($query as $row){
                $queryResult[] = $row;
            }
            
            //Se actualiza en base de datos los tipos de videogame
            if(count($queryResult) == count($personalCat)){
                $cont = 0;
                foreach ($personalCat as $index => $personal_GenOrType) {
                    $vgID = $queryResult[$cont]['videogameId'];
                    $query_personalCat = $conec->query("update videogame set videogameType = '$personal_GenOrType' "
                            . "where videogameId = '$vgID'") or die($conec->error);
                    $cont++;
                }
            } else if (count($queryResult) > count($personalCat)){
                $cont = 0;
                foreach ($personalCat as $index => $personal_GenOrType) {
                    $vgID = $queryResult[$cont]['videogameId'];
                    $query_personalCat = $conec->query("update videogame set videogameType = '$personal_GenOrType' "
                            . "where videogameId = '$vgID'") or die($conec->error);
                    $cont++;
                }
                for($i=$cont; $i < count($queryResult); $i++){
                    $vgID = $queryResult[$i]['videogameId'];
                    $query_personalCat = $conec->query("delete from videogame "
                            . "where videogameId = '$vgID'") or die($conec->error);
                    $query_personalCat1 = $conec->query("delete from likevideogame "
                            . "where videogameId = '$vgID' and id = '$id'") or die($conec->error);
                }
            }
            else {
                $cont = 0;
                foreach ($personalCat as $index => $personal_GenOrType) {
                    if($cont < count($queryResult)){
                        $vgID = $queryResult[$cont]['videogameId'];
                        $query_personalCat = $conec->query("update videogame set videogameType = '$personal_GenOrType' "
                                . "where videogameId = '$vgID'") or die($conec->error);
                    }
                    else {
                        $query_personalCat = $conec->query("insert into videogame(videogameType) " 
                        . "values('$personal_GenOrType')") or die($conec->error);
                        $query_likevideogame = $conec->query("insert into likevideogame " 
                            . "values('$conec->insert_id', '$id')") or die($conec->error);
                    }
                    $cont++;
                }
            }
            echo "1";
        }
        else if ($arr[0]['tipo'] == "MELÓMANO"){
            $personalCat = preg_split("#/#", $changeList);
            
            $consulta = "SELECT * FROM like_music_activities lma "
                    . "INNER JOIN music_activities ma"
                    . " ON lma.m_activityId = ma.m_activityId WHERE lma.id = '$id'";
            $query = $conec->query($consulta) or die($conec->error);
            
            $queryResult = array();
            foreach($query as $row){
                $queryResult[] = $row;
            }
            
            //Se actualiza en base de datos los tipos de actividades musicales
            if(count($queryResult) == count($personalCat)){
                $cont = 0;
                foreach ($personalCat as $index => $personal_GenOrType) {
                    $maID = $queryResult[$cont]['m_activityId'];
                    $query_personalCat = $conec->query("update music_activities set actividad_musical = "
                            . "'$personal_GenOrType' "
                            . "where m_activityId = '$maID'") or die($conec->error);
                    $cont++;
                }
            } else if (count($queryResult) > count($personalCat)){
                $cont = 0;
                foreach ($personalCat as $index => $personal_GenOrType) {
                    $maID = $queryResult[$cont]['m_activityId'];
                    $query_personalCat = $conec->query("update music_activities set actividad_musical = "
                            . "'$personal_GenOrType' "
                            . "where m_activityId = '$maID'") or die($conec->error);
                    $cont++;
                }
                for($i=$cont; $i < count($queryResult); $i++){
                    $maID = $queryResult[$i]['m_activityId'];
                    $query_personalCat = $conec->query("delete from music_activities "
                            . "where m_activityId = '$maID'") or die($conec->error);
                    $query_personalCat1 = $conec->query("delete from like_music_activities "
                            . "where m_activityId = '$maID' and id = '$id'") or die($conec->error);
                }
            }
            else {
                $cont = 0;
                foreach ($personalCat as $index => $personal_GenOrType) {
                    if($cont < count($queryResult)){
                        $maID = $queryResult[$cont]['m_activityId'];
                        $query_personalCat = $conec->query("update music_activities set "
                                . "actividad_musical = '$personal_GenOrType' "
                                . "where m_activityId = '$maID'") or die($conec->error);
                    }
                    else {
                        $query_personalCat = $conec->query("insert into music_activities(actividad_musical) " 
                        . "values('$personal_GenOrType')") or die($conec->error);
                        $query_like_music_activities = $conec->query("insert into like_music_activities " 
                            . "values('$conec->insert_id', '$id')") or die($conec->error);
                    }
                    $cont++;
                }
            }
            echo "1";
        }
        else if ($arr[0]['tipo'] == "BIBLIÓFILO"){
            $personalCat = preg_split("#/#", $changeList);
            
            $consulta = "SELECT * FROM likebook lb "
                    . "INNER JOIN libros b"
                    . " ON lb.bookId = b.bookId WHERE lb.id = '$id'";
            $query = $conec->query($consulta) or die($conec->error);
            
            $queryResult = array();
            foreach($query as $row){
                $queryResult[] = $row;
            }
            
            //Se actualiza en base de datos los tipos de videogame
            if(count($queryResult) == count($personalCat)){
                $cont = 0;
                foreach ($personalCat as $index => $personal_GenOrType) {
                    $bID = $queryResult[$cont]['bookId'];
                    $query_personalCat = $conec->query("update libros set "
                            . "bookGenre = '$personal_GenOrType' "
                            . "where bookId = '$bID'") or die($conec->error);
                    $cont++;
                }
            } else if (count($queryResult) > count($personalCat)){
                $cont = 0;
                foreach ($personalCat as $index => $personal_GenOrType) {
                    $bID = $queryResult[$cont]['bookId'];
                    $query_personalCat = $conec->query("update libros set bookGenre = "
                            . "'$personal_GenOrType' "
                            . "where bookId = '$bID'") or die($conec->error);
                    $cont++;
                }
                for($i=$cont; $i < count($queryResult); $i++){
                    $bID = $queryResult[$i]['bookId'];
                    $query_personalCat = $conec->query("delete from libros "
                            . "where bookId = '$bID'") or die($conec->error);
                    $query_personalCat1 = $conec->query("delete from likebook "
                            . "where bookId = '$bID' and id = '$id'") or die($conec->error);
                }
            }
            else {
                $cont = 0;
                foreach ($personalCat as $index => $personal_GenOrType) {
                    if($cont < count($queryResult)){
                        $bID = $queryResult[$cont]['bookId'];
                        $query_personalCat = $conec->query("update libros set bookGenre = "
                                . "'$personal_GenOrType' "
                                . "where bookId = '$bID'") or die($conec->error);
                    }
                    else {
                        $query_personalCat = $conec->query("insert into libros(bookGenre) " 
                        . "values('$personal_GenOrType')") or die($conec->error);
                        $query_likebook = $conec->query("insert into likebook " 
                            . "values('$conec->insert_id', '$id')") or die($conec->error);
                    }
                    $cont++;
                }
            }
            echo "1";
        }
        }
        $conec->close();
    }else{
        die("0");
     }
?>