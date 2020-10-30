<?php

    $id = (isset($_POST['id_us'])?$_POST['id_us']:'');
    $getList = (isset($_POST['getList'])?$_POST['getList']:'');

    $conec = new mysqli("localhost","root","","introverted");
    if($conec->connect_error)die("No se pudo conectar");
    mysqli_set_charset($conec, "utf8");
    
    $query = "";
    if($id!='' && $getList!=''){
        if($getList == 'music'){
        $consulta = "SELECT * FROM likemusic lm "
                . "INNER JOIN musica m"
                . " ON lm.musicId = m.musicId WHERE lm.id = '$id'";
        $query = $conec->query($consulta) or die($conec->error);
        $arr = array();
        foreach($query as $row){
            $arr[] = $row;
        }
        $str = "";
        for($i = 0; $i < count($arr); $i++){
            if($i == 0){
            $str = $arr[$i]['musicGenre'];
            }else{
                $str = $str."/".$arr[$i]['musicGenre'];
            }
        }
        
        echo $str;
        $conec -> close();
        }
        else if ($getList == 'hobbies'){
            $consulta = "SELECT * FROM likehobbie l "
                . "INNER JOIN hobbies h"
                . " ON l.hobbyId = h.hobbyId WHERE l.id = '$id'";
        $query = $conec->query($consulta) or die($conec->error);
        $arr = array();
        foreach($query as $row){
            $arr[] = $row;
        }
        $str = "";
        for($i = 0; $i < count($arr); $i++){
            if($i == 0){
            $str = $arr[$i]['hobbie'];
            }else{
                $str = $str."/".$arr[$i]['hobbie'];
            }
        }
        
        echo $str;
        $conec -> close();
        }
        else if ($getList == 'movies'){
            $consulta = "SELECT * FROM likemovie l "
                . "INNER JOIN peliculas p"
                . " ON l.movieId = p.movieId WHERE l.id = '$id'";
        $query = $conec->query($consulta) or die($conec->error);
        $arr = array();
        foreach($query as $row){
            $arr[] = $row;
        }
        $str = "";
        for($i = 0; $i < count($arr); $i++){
            if($i == 0){
            $str = $arr[$i]['movieGenre'];
            }else{
                $str = $str."/".$arr[$i]['movieGenre'];
            }
        }
        
        echo $str;
        $conec -> close();
        }
        else if ($getList == 'food'){
            $consulta = "SELECT * FROM likefood l "
                . "INNER JOIN comida c"
                . " ON l.foodId = c.foodId WHERE l.id = '$id'";
        $query = $conec->query($consulta) or die($conec->error);
        $arr = array();
        foreach($query as $row){
            $arr[] = $row;
        }
        $str = "";
        for($i = 0; $i < count($arr); $i++){
            if($i == 0){
            $str = $arr[$i]['foodType'];
            }else{
                $str = $str."/".$arr[$i]['foodType'];
            }
        }
        
        echo $str;
        $conec -> close();
        }
        else if ($getList == 'categoria_personal'){
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
            
            $consulta = "SELECT * FROM likeart l "
                . "INNER JOIN arte a"
                . " ON l.artId = a.artId WHERE l.id = '$id'";
            $query = $conec->query($consulta) or die($conec->error);
            $arr = array();
            foreach($query as $row){
                $arr[] = $row;
            }
            $str = "";
            for($i = 0; $i < count($arr); $i++){
                if($i == 0){
                $str = $arr[$i]['artGenre'];
                }else{
                    $str = $str."/".$arr[$i]['artGenre'];
                }
            }

            echo $str."/"."artista";
        } 
        else if ($arr[0]['tipo'] == "OTAKU"){
            $consulta = "SELECT * FROM likeanime l "
                . "INNER JOIN anime a"
                . " ON l.animeId = a.animeId WHERE l.id = '$id'";
            $query = $conec->query($consulta) or die($conec->error);
            $arr = array();
            foreach($query as $row){
                $arr[] = $row;
            }
            $str = "";
            for($i = 0; $i < count($arr); $i++){
                if($i == 0){
                $str = $arr[$i]['animeGenre'];
                }else{
                    $str = $str."/".$arr[$i]['animeGenre'];
                }
            }

            echo $str."/"."anime";
        }
        else if ($arr[0]['tipo'] == "GAMER"){
            $consulta = "SELECT * FROM likevideogame l "
                . "INNER JOIN videogame v"
                . " ON l.videogameId = v.videogameId WHERE l.id = '$id'";
            $query = $conec->query($consulta) or die($conec->error);
            $arr = array();
            foreach($query as $row){
                $arr[] = $row;
            }
            $str = "";
            for($i = 0; $i < count($arr); $i++){
                if($i == 0){
                $str = $arr[$i]['videogameType'];
                }else{
                    $str = $str."/".$arr[$i]['videogameType'];
                }
            }

            echo $str."/"."gamer";
        }
        else if ($arr[0]['tipo'] == "MELÓMANO"){
            $consulta = "SELECT * FROM like_music_activities l "
                    . "INNER JOIN music_activities m"
                    . " ON l.m_activityId = m.m_activityId WHERE l.id = '$id'";
            $query = $conec->query($consulta) or die($conec->error);
            $arr = array();
            foreach($query as $row){
                $arr[] = $row;
            }
            $str = "";
            for($i = 0; $i < count($arr); $i++){
                if($i == 0){
                $str = $arr[$i]['actividad_musical'];
                }else{
                    $str = $str."/".$arr[$i]['actividad_musical'];
                }
            }

            echo $str."/"."melómano";
        }
        else if ($arr[0]['tipo'] == "BIBLIÓFILO"){
            $consulta = "SELECT * FROM likebook l "
                . "INNER JOIN libros b"
                . " ON l.bookId = b.bookId WHERE l.id = '$id'";
            $query = $conec->query($consulta) or die($conec->error);
            $arr = array();
            foreach($query as $row){
                $arr[] = $row;
            }
            $str = "";
            for($i = 0; $i < count($arr); $i++){
                if($i == 0){
                $str = $arr[$i]['bookGenre'];
                }else{
                    $str = $str."/".$arr[$i]['bookGenre'];
                }
            }

            echo $str."/"."bibliófilo";
        }
        $conec -> close();
        }
        else {
            die("0");
        }
    }
    else{
        die("0");
    }
?>