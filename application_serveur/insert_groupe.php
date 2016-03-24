<!---- INF8405 - Laboratoire 2-->
<!---- Auteurs : Najib Arbaoui (1608366) && Youssef Zemmahi (1665843) && Zolnouri Mahdi (1593999)-->
<?php

include "db.php";

// recupperer le fichier json pour la mise a jour de la position
$data = json_decode(file_get_contents('php://input'), true);

$idgroupe = $data["idgroupe"];
$nom = $data["nom"];
$nom = str_replace("'", " ", $nom);

$conn = new mysqli($servername, $username, $password, $dbname);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

// Insertion position

$sql = "INSERT INTO `inf8405`.`groupe` (`idgroupe`, `nom`) VALUES ('" . "$idgroupe" . "','" . "$nom" . "');";
if ($conn->query($sql)) {
    echo 1;
} else {
    echo 0;
}


$conn->close();