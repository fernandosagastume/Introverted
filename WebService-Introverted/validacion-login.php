<?php

    $emailuser = (isset($_POST['emailuser'])?$_POST['emailuser']:'');
    $pwrd = (isset($_POST['pass'])?$_POST['pass']:'');
    
    if(($emailuser!='') && ($pwrd!='')){
    $conec = new mysqli("localhost","root","","introverted");
    if($conec->connect_error)die("No se pudo conectar");
    mysqli_set_charset($conec, 'utf8');
    
    $res = "";
    if(strpos($emailuser, "@") !== false){
        $res = $conec->query("select u.id, p.tipo from users u "
                            . "JOIN categoria c ON (u.id = c.id) "
                            . "JOIN persontype p ON (c.personTypeId = p.personTypeId) "
                            ."where u.email='$emailuser' and u.pass = '$pwrd'") or die($conec->error);
    }
    else {
        $res = $conec->query("select u.id, p.tipo from users u "
                            . "JOIN categoria c ON (u.id = c.id) "
                            . "JOIN persontype p ON (c.personTypeId = p.personTypeId) "
                            ."where u.username='$emailuser' and u.pass = '$pwrd'") or die($conec->error);
    }
    $arr = array();
    foreach($res as $row){
        $arr[] = $row;
    }
    if(empty($arr)){
        echo "0";
        mysqli_close($conec);
    }
    else{
        echo $arr[0]['id']."/".$arr[0]['tipo'];
        mysqli_close($conec);
    }
    }
?>
