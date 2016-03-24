<!---- INF8405 - Laboratoire 2-->
<!---- Auteurs : Najib Arbaoui (1608366) && Youssef Zemmahi (1665843) && Zolnouri Mahdi (1593999)-->
<?php
include "db.php";

// recupperer le fichier json pour la mise a jour de la position
$data = json_decode(file_get_contents('php://input'), true);

$courriel = $data["courriel"];
$password_user = $data["password"];

/// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

// vérifier si l'utilisateur existe
$courriel_is_not_exist = 1;

$sql = "SELECT * FROM `utilisateur` WHERE `courriel` = '$courriel'";

$result = $conn->query($sql);

if ($result->num_rows > 0) {
    $courriel_is_not_exist = 0;
} else {
    $courriel_is_not_exist = 1;

}
// vérifier le mot de passe pour cet utilisateur

$sql = "SELECT * FROM `utilisateur` WHERE `courriel` = '$courriel' and `password`='$password_user'";

$result = $conn->query($sql);

if ($result->num_rows > 0) {
    // output data of each row
    $rows = array();
    while ($row = $result->fetch_assoc()) {
        $rows[] = $row;
    }
    print json_encode($rows);
} else {
    echo $courriel_is_not_exist;
}

$conn->close();