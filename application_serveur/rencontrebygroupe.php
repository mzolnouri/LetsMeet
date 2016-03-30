<!---- INF8405 - Laboratoire 2-->
<!---- Auteurs : Najib Arbaoui (1608366) && Youssef Zemmahi (1665843) && Zolnouri Mahdi (1593999)-->
<?php
include "db.php";

$id_groupe= $_GET["id_groupe"];

/// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

$sql = "SELECT DISTINCT * FROM `rencontre` WHERE `groupe_idgroupe` =  '$id_groupe' LIMIT 0,1";
$result = $conn->query($sql);

if ($result->num_rows > 0) {
    // output data of each row
    $rows = array();
    while($row = $result->fetch_assoc()) {
        $rows[] = $row;
    }
print json_encode($rows,JSON_HEX_TAG | JSON_HEX_APOS | JSON_HEX_QUOT | JSON_HEX_AMP | JSON_UNESCAPED_UNICODE);
//    echo "lieu=".$rows[0]["lieu1"];
} else {
    echo 0;
}
//echo "lieu=".$rows[0]["lieu1"];
//var_dump($rows);
$conn->close();
?>
