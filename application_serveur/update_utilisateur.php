<!---- INF8405 - Laboratoire 2-->
<!---- Auteurs : Najib Arbaoui (1608366) && Youssef Zemmahi (1665843) && Zolnouri Mahdi (1593999)-->
<?php

include "db.php";

// recupperer le fichier json pour la mise a jour de la position
$data = json_decode(file_get_contents('php://input'), true);

$idutilisateur = $data["idutilisateur"];
$courriel = $data["courriel"];
$photo = $data["photo"];
$organisateur = $data["organisateur"];
$groupe_idgroupe = $data["groupe_idgroupe"];
$password_user = $data["password"];


/// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

$sql = " UPDATE `inf8405`.`utilisateur` SET `photo` = '$photo', `organisateur` = '$organisateur', `groupe_idgroupe` = '$groupe_idgroupe', `password` = '$password_user' WHERE `utilisateur`.`idutilisateur` = '$idutilisateur';";

echo($conn->query($sql));


$conn->close();
