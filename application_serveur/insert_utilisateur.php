<!---- INF8405 - Laboratoire 2-->
<!---- Auteurs : Najib Arbaoui (1608366) && Youssef Zemmahi (1665843) && Zolnouri Mahdi (1593999)-->
<?php

include "db.php";

// recupperer le fichier json pour la mise a jour de la position
$data = json_decode(file_get_contents('php://input'), true);

$idposition = $data["position"]["idposition"];
$latitude = $data["position"]["latitude"];
$longitude = $data["position"]["longitude"];
$position_time = $data["position"]["position_time"];

$idutilisateur = $data["utilisateur"]["idutilisateur"];
$courriel = $data["utilisateur"]["courriel"];
$photo = $data["utilisateur"]["photo"];
$organisateur = $data["utilisateur"]["organisateur"];
$position_idposition = $data["utilisateur"]["position_idposition"];
$groupe_idgroupe = $data["utilisateur"]["groupe_idgroupe"];
$password_user = $data["utilisateur"]["password"];

$position_idposition = $idposition;

$conn = new mysqli($servername, $username, $password, $dbname);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

// Insertion position
$insertion_valide = 0;

$sql = "INSERT INTO `inf8405`.`position` (`idposition`, `latitude`, `longitude`, `radius`, `position_time`) VALUES ('$idposition', '$latitude', '$longitude', '10', '$position_time');";
if ($conn->query($sql) == true) {
    $insertion_valide = 1;
} else {
    $insertion_valide = 0;
}


// Insertion utilisateur
$sql = "INSERT INTO `utilisateur` (`idutilisateur`, `courriel`, `photo`, `organisateur`, `position_idposition`, `groupe_idgroupe`, `password`)
" . "VALUES ('$idutilisateur', '$courriel', '$photo', '$organisateur', '$position_idposition','$groupe_idgroupe','$password_user');";

if ($conn->query($sql) == true) {
    $insertion_valide = 1;
} else {
    $insertion_valide = 0;
}
echo($insertion_valide);

$conn->close();