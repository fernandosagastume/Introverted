<?php

$id = (isset($_POST['id_us'])?$_POST['id_us']:'');

    $conec = mysqli_connect("localhost","root","","introverted");
    mysqli_set_charset($conec, "utf8");
   if(!$conec) {
      die('Could not connect: ' . mysqli_error());
   }
   
   $query = "SELECT tipo FROM categoria c "
                . "INNER JOIN persontype p"
                . " ON c.personTypeId = p.personTypeId WHERE c.id = '$id'";
   
   if($res = mysqli_query($conec, $query)) {
      if(mysqli_num_rows($res) > 0) {
        while($row = mysqli_fetch_array($res)){
            echo $row['tipo'];
        }
      }else {
         echo "0";
      }
   }else {
         echo "ERROR";
      }
   mysqli_close($conec);
?>

