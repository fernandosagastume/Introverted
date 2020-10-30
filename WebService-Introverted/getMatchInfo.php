<?php

    $id = (isset($_POST['id_us'])?$_POST['id_us']:'');
    
    $conec = new mysqli("localhost","root","","introverted");
    if($conec->connect_error)die("No se pudo conectar");
    mysqli_set_charset($conec, 'utf8');

    if($id != ''){
        
        $consulta = "SELECT generoId FROM users WHERE id = '$id'";
        $query = $conec->query($consulta) or die($conec->error);
        $userGID;
        foreach($query as $row){
            $userGID = $row['generoId'];
        }
        
        $consulta1 = "SELECT * FROM interesado WHERE id = '$id'";
        $query1 = $conec->query($consulta1) or die($conec->error);
        $arr = array();
        //Proceso para verificar si el usuario hace match con el interesado
        foreach($query1 as $row){
            $generoId = $row['generoId'];
            $consulta2 = "SELECT id FROM users WHERE generoId = '$generoId'";
            $query2 = $conec->query($consulta2) or die($conec->error);
            $arr1 = array();
            foreach($query2 as $row1){
                $idM = $row1['id'];
                $consulta3 = "SELECT * FROM interesado WHERE id = '$idM' and generoId = '$userGID'";;
                $query3 = $conec->query($consulta3) or die($conec->error);
                foreach($query3 as $row2){
                    $arr1[] = $row2;
                }
                if(count($arr1) > 0){
                    $arr[] = $row1['id'];
                }
            }
        }
        
        $match_data = array();
        for($i = 0; $i < count($arr); $i++){
            $matchID = $arr[$i];
            $consulta4 = "SELECT u.id, u.username, u.nombre, u.apellido, i.saveLocation 
                          FROM users u
                          LEFT JOIN imagenusuario i ON (i.id = u.id)
                          LEFT JOIN fotoperfil f ON (f.photoId = i.photoId)
                          WHERE u.id = '$matchID'";
            $query4 = $conec->query($consulta4) or die($conec->error);
            foreach($query4 as $row4){
                $match_data[] = $row4;
            }
        }
        
        echo json_encode($match_data);
        $conec->close();
    }else{
        die("Ningún ID fue dado");
    }
?>
