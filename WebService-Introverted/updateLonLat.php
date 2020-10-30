<?php

    $id = (isset($_POST['id_us'])?$_POST['id_us']:'');
    $lon = (isset($_POST['lon'])?$_POST['lon']:'');
    $lat = (isset($_POST['lat'])?$_POST['lat']:'');

    $conec = new mysqli("localhost","root","","introverted");
    if($conec->connect_error)die("No se pudo conectar");
    mysqli_set_charset($conec, "utf8");
    
    if($id!='' && $lon!='' && $lat !=''){
        $query = $conec->query("UPDATE users SET longitude = '$lon', latitude = '$lat' "
                           . "WHERE id = '$id'") or die($conec->error);
       echo "1";
       $conec->close();
    }else{
        echo '0';
    }

?>

