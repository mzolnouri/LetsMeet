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

/// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

$sql = "UPDATE `position` SET `latitude` = '$latitude', `longitude` = '$longitude', `position_time` = '$position_time' WHERE `idposition` = '$idposition' ;";

echo($conn->query($sql));


$conn->close();