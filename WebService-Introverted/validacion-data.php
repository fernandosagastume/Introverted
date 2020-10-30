<?php

    $email = (isset($_POST['email'])?$_POST['email']:'');
    $user = (isset($_POST['user'])?$_POST['user']:'');
    
    $conec = new mysqli("localhost","root","","introverted");
    if($conec->connect_error)die("No se pudo conectar");
    $query_email = "";
    $query_user = "";
    
        $query_email = $conec->query("select * from users where email='$email'") or die($conec->error);
    
        $query_user = $conec->query("select * from users where username='$user'") or die($conec->error);
    
    
    $arr = array();
    foreach($query_email as $row){
        $arr[] = $row;
    }
    $arr1 = array();
    if($query_user != ""){
        foreach($query_user as $row1){
            $arr1[] = $row1;
        }
    }
    /*
    arr = email
    arr1 = user
          */
    if(!empty($arr) && !empty($arr1)){
        echo "11";
        mysqli_close($conec);
    }
    elseif(!empty($arr) && empty($arr1)){
        echo "10";
        mysqli_close($conec);
    }
    elseif(empty($arr) && !empty($arr1)){
        echo "01";
        mysqli_close($conec);
    }
    else{
        echo "00";
        mysqli_close($conec);
    }
    

?>