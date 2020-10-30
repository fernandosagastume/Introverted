<?php
    $id = (isset($_POST['id_us'])?$_POST['id_us']:'');
    $subject = (isset($_POST['subject'])?$_POST['subject']:'');
    $title = (isset($_POST['title'])?$_POST['title']:'');
    $post_body = (isset($_POST['post_body'])?$_POST['post_body']:'');
    $post_image = (isset($_POST['post_image'])?$_POST['post_image']:'');
    
    $conec = new mysqli("localhost","root","","introverted");
    if($conec->connect_error)die("No se pudo conectar");
    mysqli_set_charset($conec, 'utf8');
    
    if($id != '' && $subject != ''&& $title != '' && $post_body!= ''){
        if($post_image != ''){
            $dir = "/WebService-Introverted/Uploads/ForumUploads";
            if(!file_exists("/wamp64/www".$dir)){
                mkdir($dir, 0777, TRUE);
            }
            $like_dislike = 0;
            $query_p = $conec->query("INSERT INTO publicaciones(id, tiempo, titulo, publicacion, "
                       . "subject, likes, dislikes) VALUES('$id', CURRENT_TIME(), '$title', "
                       . "'$post_body', '$subject', '$like_dislike', '$like_dislike')") or die($conec->error);
            $postID = $conec->insert_id;
            
            $dir1 = "/wamp64/www".$dir."/"."postPicture_POSTID".$postID.".jpeg";

            if(file_put_contents($dir1, base64_decode($post_image))){
              
               $query = $conec->query("UPDATE publicaciones SET saveLocation = '$dir1'"
                . " WHERE postId = '$postID'") or die($conec->error);
               echo "1";
               $conec->close();
            }
            else{
                die("0");
            }
        }
        else{
            $like_dislike = 0;
               $query_post = $conec->query("INSERT INTO publicaciones(id, tiempo, titulo, publicacion, "
                       . "subject, likes, dislikes) VALUES('$id', CURRENT_TIME(), '$title', "
                       . "'$post_body', '$subject', '$like_dislike', '$like_dislike')") or die($conec->error);
               echo "1";
               $conec->close();
        }
    }
    else{
        die("0");
    }
?>
