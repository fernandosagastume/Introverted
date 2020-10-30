<?php
    
    $id = (isset($_POST['id_us'])?$_POST['id_us']:'');
    $type = (isset($_POST['type'])?$_POST['type']:'');
    
    $conec = new mysqli("localhost","root","","introverted");
    if($conec->connect_error)die("No se pudo conectar");
    mysqli_set_charset($conec, 'utf8');
    
    if($id != '' && $type != ''){
        
        $consulta = "SELECT p.tipo, u.generoId from users u "
                            . "JOIN categoria c ON (u.id = c.id) "
                            . "JOIN persontype p ON (c.personTypeId = p.personTypeId) "
                            . "WHERE u.id = '$id'";
        $query = $conec->query($consulta) or die($conec->error);
        $arr = array();
        $tipo;
        $generoId;
        foreach($query as $row){
            $tipo = $row['tipo'];
            $generoId = $row['generoId'];
        }
        
        $consulta2 = "SELECT u.id, u.nombre, u.apellido, u.username, u.bday, u.longitude, u.latitude, u.generoId, g.sexo "
                            . "from users u "
                            . "JOIN genero g ON (u.generoId = g.generoId) "
                            . "JOIN categoria c ON (u.id = c.id) "
                            . "JOIN persontype p ON (c.personTypeId = p.personTypeId) "
                            . "WHERE p.tipo = '$tipo'";
        $query1 = $conec->query($consulta2) or die($conec->error);
        foreach($query1 as $row1){
            $gid = $row1['generoId'];
            $consulta3 = "SELECT * FROM interesado WHERE id = $id && generoId = '$gid'";
            $query2 = $conec->query($consulta3) or die($conec->error);
            $arr2 = array();
            foreach($query2 as $row2){
                $arr2[] = $row2;
            }
            
            if(count($arr2) == 0){
                $arr[] = $row1;
            }
            
        }
        echo json_encode($arr);
    }
    else if($id != '' && $type == ''){
        $consulta = "SELECT u.id, i.saveLocation from users u "
                            . "JOIN imagenusuario i ON (u.id = i.id) "
                            . "JOIN fotoperfil f ON (f.photoId = i.photoId) "
                            . "WHERE u.id = '$id'";
        $query = $conec->query($consulta) or die($conec->error);
        $arr = array();
        foreach($query as $row){
            $arr[] = $row;
        }
        
        echo json_encode($arr);
    }
    else{
        die($conec->error);
    }
    
?>

