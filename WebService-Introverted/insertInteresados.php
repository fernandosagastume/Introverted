<?php

    $id = (isset($_POST['id_us'])?$_POST['id_us']:'');
    $id_in = (isset($_POST['id_interested'])?$_POST['id_interested']:'');
    
    $conec = new mysqli("localhost","root","","introverted");
    if($conec->connect_error)die("No se pudo conectar");
    mysqli_set_charset($conec, 'utf8');
    
    if($id != '' && $id_in != ''){
        $consulta = "SELECT id, generoId FROM users WHERE id = '$id' or id = '$id_in'";
        $query = $conec->query($consulta) or die("0");
        
        $userGID = 0;
        $inGID = 0;
        foreach($query as $row){
            if($row['id'] == $id){
                $userGID = $row['generoId'];
            }
            else{
                $inGID = $row['generoId'];
            }
        }
        
        if($userGID == 0 || $inGID == 0){
            die("0");
        }
        
        $insert = "INSERT INTO interesado VALUES('$inGID', '$id')";
        $query1 = $conec->query($insert) or die("0");
        
        $consulta2 = "SELECT generoId FROM interesado WHERE id = '$id_in'";
        $query2 = $conec->query($consulta2) or die("0");
        
        $closeornot = 0;
        
        foreach($query2 as $row1){
            if($userGID == $row1['generoId']){
                echo "match";
                $conec->close();
                $closeornot = 1;
            }
        }
        if($closeornot == 0){
            echo "1";
            $conec->close();
        }
        
    }else{
        echo "0";
    }
    
?>
