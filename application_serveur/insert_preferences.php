<!---- INF8405 - Laboratoire 2-->
<!---- Auteurs : Najib Arbaoui (1608366) && Youssef Zemmahi (1665843) && Zolnouri Mahdi (1593999)-->
<?php

include "db.php";

// recupperer le fichier json pour la mise a jour de la position
$data = json_decode(file_get_contents('php://input'), true);

//var_dump($data);

$conn = new mysqli($servername, $username, $password, $dbname);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

// Insertion preferences
$insert_succes = 0;
for ($i = 0; $i < 3; $i++) {
    // recuperer les informations des 3 preferences
    $idpreferences = $data[$i]["idpreferences"];
    $endroit = $data[$i]["endroit"];
    $priorite = $data[$i]["priorite"];
    $idutilisateur = $data[$i]["idutilisateur"];
    $endroit = str_replace("'", " ", $endroit);
    $sql = "INSERT INTO `inf8405`.`preferences` (`idpreferences`, `endroit`, `priorite`, `utilisateur_idutilisateur`) VALUES ('$idpreferences', '$endroit', '$priorite', '$idutilisateur');";
    if ($conn->query($sql) == true) {
        $insert_succes = 1;
    } else {
        $insert_succes = 0;
    }
}

echo $insert_succes;

$conn->close();