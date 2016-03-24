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


// Insertion calendrier

$insert_succes = 0;
for ($i = 0; $i < sizeof($data); $i++) {
    // recuperer les informations des calendrier
    $idcalendrier= $data[$i]["idcalendrier"];
    $heure_debut = $data[$i]["heure_debut"];
    $heure_fin = $data[$i]["heure_fin"];
    $activite = $data[$i]["activite"];
    $idutilisateur = $data[$i]["idutilisateur"];
    $activite = str_replace("'", " ", $activite);
    echo ("- ".$idcalendrier."\n".$heure_debut."\n".$heure_fin."\n".$activite."\n"."\n" );
    $sql = "INSERT INTO `inf8405`.`calendrier` (`idcalendrier`, `heure_debut`, `heure_fin`, `activite`) VALUES ('$idcalendrier', '$heure_debut', '$heure_fin', '$activite');";
    if ($conn->query($sql) == true) {
        $insert_succes = 1;
    } else {
        $insert_succes = 0;
    }

    $sql = "INSERT INTO `inf8405`.`calendrier_has_utilisateur` (`calendrier_idcalendrier`, `utilisateur_idutilisateur`) VALUES ('$idcalendrier', '$idutilisateur');";
    if ($conn->query($sql) == true) {
        $insert_succes = 1;
    } else {
        $insert_succes = 0;
    }
}

echo $insert_succes;

$conn->close();

