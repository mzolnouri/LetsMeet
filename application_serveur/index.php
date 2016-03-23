<!---- INF8405 - Laboratoire 2-->
<!---- Auteurs : Najib Arbaoui (1608366) && Youssef Zemmahi (1665843) && Zolnouri Mahdi (1593999)--><?php
include "db.php";

$data = json_decode(file_get_contents('php://input'), true);
//print json_encode($data);
/// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

$sql = "SELECT * FROM utilisateur";
$result = $conn->query($sql);

if ($result->num_rows > 0) {
    // output data of each row
    $rows = array();
    while($row = $result->fetch_assoc()) {
//        echo "id: " . $row["idutilisateur"]. " - Name: " . $row["courriel"]. " <img src='" . $row["photo"]. " '/><br>";
        $rows[] = $row;
    }
    print json_encode($rows);
} else {
    echo "0 results";
}
$conn->close();
?>
