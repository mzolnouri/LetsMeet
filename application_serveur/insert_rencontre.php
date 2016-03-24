<!---- INF8405 - Laboratoire 2-->
<!---- Auteurs : Najib Arbaoui (1608366) && Youssef Zemmahi (1665843) && Zolnouri Mahdi (1593999)-->
<?php
include "db.php";

// recupperer le fichier json pour la mise a jour de la position
$data = json_decode(file_get_contents('php://input'), true);

$idrencontre = $data["idrencontre"];
$lieu1= $data["lieu1"];
$lieu2= $data["lieu2"];
$lieu3= $data["lieu3"];
$jour = $data["jour"];
$description= $data["description"];
$groupe_idgroupe= $data["groupe_idgroupe"];
$idorganisateur= $data["idorganisateur"];

$lieu1 = str_replace("'", " ", $lieu1);
$lieu1 = str_replace("é", "e", $lieu1);
$lieu1 = str_replace("è", "e", $lieu1);
$lieu1 = str_replace("à", "a", $lieu1);
$lieu1 = str_replace("â", "a", $lieu1);

$lieu2 = str_replace("'", " ", $lieu2);
$lieu2= str_replace("é", "e", $lieu2);
$lieu2 = str_replace("è", "e", $lieu2);
$lieu2 = str_replace("à", "a", $lieu2);
$lieu2 = str_replace("â", "a", $lieu2);

$lieu3 = str_replace("'", " ", $lieu3 );
$lieu1 = str_replace("é", "e", $lieu3 );
$lieu1 = str_replace("è", "e", $lieu3 );
$lieu1 = str_replace("à", "a", $lieu3 );
$lieu1 = str_replace("â", "a", $lieu3 );


$conn = new mysqli($servername, $username, $password, $dbname);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

// Insertion rencontre

$sql = "INSERT INTO `inf8405`.`rencontre` (`idrencontre`, `lieu1`, `lieu2`, `lieu3`, `jour`, `description`, `groupe_idgroupe`, `idorganisateur`) VALUES ('$idrencontre', '$lieu1', '$lieu2','$lieu3','$jour', '$description', '$groupe_idgroupe', '$idorganisateur');";
if ($conn->query($sql)) {
    echo 1;
} else {
    echo 0;
}


$conn->close();