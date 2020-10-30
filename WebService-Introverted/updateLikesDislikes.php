<?php

    $postId = (isset($_POST['post_id'])?$_POST['post_id']:'');
    $likes = (isset($_POST['likes'])?$_POST['likes']:'');
    $dislikes = (isset($_POST['dislikes'])?$_POST['dislikes']:'');

    $conec = new mysqli("localhost","root","","introverted");
    if($conec->connect_error)die("No se pudo conectar");
    mysqli_set_charset($conec, "utf8");
    
    if($postId!='' && $likes!='' && $dislikes !=''){
        $query = $conec->query("UPDATE publicaciones SET likes = '$likes', dislikes = '$dislikes' "
                           . "WHERE postId = '$postId'") or die($conec->error);
       echo "1";
       $conec->close();
    }else{
        echo '0';
    }

?>