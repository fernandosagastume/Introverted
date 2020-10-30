<?php

$id = (isset($_POST['id_us'])?$_POST['id_us']:'');

    $conec = new mysqli("localhost","root","","introverted");
    if($conec->connect_error)die("No se pudo conectar");
    $query_email = "";
    $query_user = "";
    mysqli_set_charset($conec, 'utf8');
    
    if($id!=''){
        $consulta = "SELECT * FROM users u "
                . "INNER JOIN genero g"
                . " ON u.generoId = g.generoId WHERE u.id = '$id'";
        $query = $conec->query($consulta) or die($conec->error);
    }
    else{
        die("0");
    }
    
    $arr = array();
    foreach($query as $row){
        $arr[] = $row;
    }
    
    $consulta = "SELECT i.saveLocation FROM fotoPerfil f "
                . "INNER JOIN imagenUsuario i"
                . " ON f.photoId = i.photoId WHERE f.id = '$id'";
        $query = $conec->query($consulta) or die($conec->error);
        
    foreach($query as $row){
        $arr[] = $row;
    }
    
    $json = json_encode($arr);
    echo $json;
    $conec -> close();

?>
