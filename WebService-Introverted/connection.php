<?php
    
    $con = new mysqli("localhost", "root", "", "introverted");
    if ($con->connect_errno) {
    echo "Error al conectarse con la base de datos";
    }
?>

