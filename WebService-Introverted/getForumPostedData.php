<?php

    $conec = new mysqli("localhost","root","","introverted");
    if($conec->connect_error)die("No se pudo conectar");
    mysqli_set_charset($conec, 'utf8');
    
    $consulta = "SELECT p.postId, p.id, p.tiempo, p.titulo, p.publicacion, "
                . "p.subject, p.saveLocation, p.likes, p.dislikes, u.username"
                . " FROM publicaciones p "
                . "JOIN users u ON (p.id = u.id)";
    $query = $conec->query($consulta) or die($conec->error);
    $arr = array();
    $count = 0;
    foreach($query as $row){
      $postid = $row['postId'];
      $usID = $row['id'];
      $consulta1 = "SELECT COUNT(comentario) AS commentCounter FROM comentarios WHERE postId = '$postid'";
      $query1 = $conec->query($consulta1) or die($conec->error);
      $commentCounter;
      foreach($query1 as $row1){
          $commentCounter = $row1['commentCounter'];
      }
      
      $consulta2 = "SELECT i.saveLocation AS profilePic FROM imagenusuario i "
                    . "JOIN fotoperfil f ON (f.photoId = i.photoId) "
                    . "WHERE i.id = '$usID'";
      $query2 = $conec->query($consulta2) or die($conec->error);
      $profilePicture = "";
      foreach($query2 as $row2){
          $profilePicture = $row2['profilePic'];
      }
      
      $splt = preg_split("/[\s,]+/", $row['tiempo']);
      $time = $splt[0]."/".$splt[1];
      $row['tiempo'] = $time;
      $arr[] = $row;
      $arr[$count]['commentCounter'] = $commentCounter;
      if($profilePicture != ""){
          $arr[$count]['profilePic'] = $profilePicture;
      }
      else{
          $arr[$count]['profilePic'] = NULL;
      }
      $count++;
    }
    echo json_encode($arr);
    $conec->close();
?>

