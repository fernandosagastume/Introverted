<?php

    $id = (isset($_POST['id_us'])?$_POST['id_us']:'');
    $action = (isset($_POST['accion'])?$_POST['accion']:'');
    $nombre = (isset($_POST['nombre'])?$_POST['nombre']:'');
    $apellido = (isset($_POST['apellido'])?$_POST['apellido']:'');
    $brday = (isset($_POST['bday'])?$_POST['bday']:'');
    $genero = (isset($_POST['genero'])?$_POST['genero']:'');
    $drinks = (isset($_POST['drink'])?$_POST['drink']:'');
    $smokes = (isset($_POST['smoke'])?$_POST['smoke']:'');
    $description = (isset($_POST['description'])?$_POST['description']:'');
    $profilePic = (isset($_POST['profpic'])?$_POST['profpic']:'');

    $conec = new mysqli("localhost","root","","introverted");
    if($conec->connect_error)die("No se pudo conectar");
    mysqli_set_charset($conec, 'utf8');
    
    if($action == "retrieve" && $id != ''){
        $consulta = "SELECT u.nombre, u.apellido, g.sexo, u.bday, u.drink, u.smoke, p.descripcion  "
                . "FROM users u "
                    . "JOIN genero g "
                    . "JOIN categoria c "
                    . "JOIN persontype p"
                    . " ON u.generoId = g.generoId "
                    . "and c.id = u.id and c.personTypeId = p.personTypeId"
                    . " WHERE u.id = '$id'";
        $query = $conec->query($consulta) or die($conec->error);
        
        $arr = array();
        foreach($query as $row){
            $arr[] = $row;
        }
        
        $consulta = "SELECT i.saveLocation  "
                . "FROM imagenusuario i "
                    . "JOIN fotoperfil f "
                    . "ON i.id = f.id "
                    . "WHERE i.id = '$id'";
        $query = $conec->query($consulta) or die($conec->error);
        $pp = array();
        foreach($query as $row){
            $arr[] = $row;
        }
        
        $json = json_encode($arr);
        echo $json;
        $conec -> close();
    }
    
    else if($action == "update" && $id != '' && $nombre != ''&& $apellido != ''
            && $brday != ''&& $genero != ''&& $drinks != ''&& $smokes != ''&& $description != ''){
        $query = $conec->query("UPDATE users SET nombre = '$nombre', apellido = '$apellido',"
                           . " bday = '$brday', drink = '$drinks', smoke = '$smokes' "
                           . "WHERE id = '$id'") or die($conec->error);
        
        $query = $conec->query("UPDATE users u JOIN genero g SET g.sexo = '$genero'"
                . " WHERE u.generoId = g.generoId and u.id = '$id'") or die($conec->error);
        
        $query = $conec->query("UPDATE users u JOIN categoria c JOIN persontype p SET p.descripcion = '$description'"
                . " WHERE u.id = c.id and c.personTypeId = p.personTypeId and u.id = '$id'") or die($conec->error);
        
        echo 1;
        $conec->close();
    }
    else if($action == "uploadPic" && $id!='' && $profilePic!=''){
        $dir = "/WebService-Introverted/Uploads";
        if(!file_exists("/wamp64/www".$dir)){
            mkdir($dir, 0777, TRUE);
        }
        $dir1 = "/wamp64/www".$dir."/"."profilePicture_ID".$id.".jpeg";
        
        if(file_exists($dir1)){
            unlink($dir1);
        }
   
        if(file_put_contents($dir1, base64_decode($profilePic))){
            $querytext = "SELECT * FROM fotoPerfil "
                . "WHERE id = '$id'";
            $query = $conec->query($querytext);
            $arreglo1 = array();
                foreach($query as $row){
                    $arreglo1[] = $row;
                }
            $dir = $dir."/"."profilePicture_ID".$id.".jpeg";
            if(count($arreglo1) == 0){
            $query_profilePic = $conec->query("INSERT INTO imagenusuario(saveLocation, id) " 
                . "VALUES('$dir', '$id')") or die($conec->error);
            $query_profilePic = $conec->query("INSERT INTO fotoPerfil(photoId, id) " 
                . "VALUES('$conec->insert_id', '$id')") or die($conec->error);
            }
            else{
                $arreglo = array();
                foreach($query as $row){
                    $arreglo[] = $row;
                }
                $photoId = $arreglo[0]['photoId'];
                $query_profilePic = $conec->query("UPDATE imagenusuario SET saveLocation = '$dir' " 
                . "WHERE id = '$id' and '$photoId'") or die($conec->error);
            }
            echo 1;
        }
        else{
            echo 0;
        }
    }
    
    else{
        die('0');
    }

?>