<!---- INF8405 - Laboratoire 2-->
<!---- Auteurs : Najib Arbaoui (1608366) && Youssef Zemmahi (1665843) && Zolnouri Mahdi (1593999)-->
<?php
include "db.php";

$id_groupe = $_GET["id_groupe"];


///// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

$sql = "SELECT * FROM  `rencontre` WHERE `groupe_idgroupe` =  '$id_groupe'";
$result = $conn->query($sql);

// Recupperer les utilisateurs d'un groupe
$sql_utilisateur = "SELECT * FROM `utilisateur` WHERE `groupe_idgroupe` =  '$id_groupe'";
$result_utilisateur = $conn->query($sql_utilisateur);
$utilisateur_array = array();

if ($result_utilisateur->num_rows > 0) {
    // output data of each row

    while ($row = $result_utilisateur->fetch_assoc()) {
        $utilisateur_array[] = $row;
    }

}


// Recupperer les preferences des utilisateurs
$preferences_array = array();

for ($i = 0; $i < sizeof($utilisateur_array); $i++) {
    $id_utilisateur = $utilisateur_array[$i]["idutilisateur"];

    $sql_preferences = "SELECT DISTINCT * FROM `preferences` WHERE `utilisateur_idutilisateur` ='$id_utilisateur' ";
    $result_preferences = $conn->query($sql_preferences);

    if ($result_preferences->num_rows > 0) {
        // output data of each row

        while ($row = $result_preferences->fetch_assoc()) {
            $preferences_array[] = $row;
        }

    }

}

// Recupperer les evenements des utilisateurs deja enregistres dans Google Calendar
$calendrier_array = array();

for ($i = 0; $i < sizeof($utilisateur_array); $i++) {
    $id_utilisateur = $utilisateur_array[$i]["idutilisateur"];
    $sql_calendrier = "SELECT DISTINCT `heure_debut`, `heure_fin`,`activite` FROM `calendrier`, `calendrier_has_utilisateur` WHERE `utilisateur_idutilisateur` = '$id_utilisateur' ";
    $result_calendrier = $conn->query($sql_calendrier);

    if ($result_calendrier->num_rows > 0) {
        // output data of each row

        while ($row = $result_calendrier->fetch_assoc()) {
            $calendrier_array[] = $row;
        }

    }

}

// un algorithme Vorace pour definir le lieu et la date de rencontre
$date = "";
$jour_rencontre = "";
$heure_rencontre = "";
$lieu = "";
$nombre_de_choix_p1 = 0;
$nombre_de_choix_p2 = 0;
$nombre_de_choix_p3 = 0;
$nombre_utilisateurs = sizeof($utilisateur_array);


// Priorite 1
for ($i = 0; $i < sizeof($preferences_array) - 1; $i++) {
    for ($j = $i + 1; $j < sizeof($preferences_array); $j++) {

        if (strcmp($preferences_array[$i]["priorite"], "1") == 0 and strcmp($preferences_array[$j]["priorite"], "1") == 0) {

            if (strncmp($preferences_array[$i]["endroit"], $preferences_array[$j]["endroit"], 10) == 0) {
                $nombre_de_choix_p1 = $nombre_de_choix_p1 + 1;
                $lieu = $preferences_array[$i]["endroit"];
            }

        }

    }

}

// Priorite 2
for ($i = 0; $i < sizeof($preferences_array) - 1; $i++) {
    for ($j = $i + 1; $j < sizeof($preferences_array); $j++) {

        if (strcmp($preferences_array[$i]["priorite"], "2") == 0 and strcmp($preferences_array[$j]["priorite"], "2") == 0) {

            if (strncmp($preferences_array[$i]["endroit"], $preferences_array[$j]["endroit"], 10) == 0) {
                $nombre_de_choix_p2 = $nombre_de_choix_p2 + 1;
                if ($nombre_de_choix_p2 > $nombre_de_choix_p1)
                    $lieu = $preferences_array[$i]["endroit"];
            }

        }

    }

}

// Priorite 3
for ($i = 0; $i < sizeof($preferences_array) - 1; $i++) {
    for ($j = $i + 1; $j < sizeof($preferences_array); $j++) {

        if (strcmp($preferences_array[$i]["priorite"], "3") == 0 and strcmp($preferences_array[$j]["priorite"], "3") == 0) {

            if (strncmp($preferences_array[$i]["endroit"], $preferences_array[$j]["endroit"], 10) == 0) {
                $nombre_de_choix_p3 = $nombre_de_choix_p3 + 1;
                if ($nombre_de_choix_p3 > $nombre_de_choix_p2 and $nombre_de_choix_p3 > $nombre_de_choix_p1)
                    $lieu = $preferences_array[$i]["endroit"];
            }

        }

    }

}

$activity_array = array();

for ($i = 0; $i < sizeof($calendrier_array); $i++) {
    $jour = substr($calendrier_array[$i]["heure_debut"], 0, 10);
    $activity_array["jour"][$i] = $jour;
    for ($j = 8; $j < 23; $j++) {
        $activity_array["heure"][$j - 8] = $j;
        $activity_array["disponible"][$j - 8] = "oui";

    }
}


for ($i = 0; $i < sizeof($calendrier_array); $i++) {
    $jour = substr($calendrier_array[$i]["heure_debut"], 0, 10);
    $heure_debut = substr($calendrier_array[$i]["heure_debut"], 11, 2);
    $heure_fin = substr($calendrier_array[$i]["heure_fin"], 11, 2);
    if (strncmp(strval($activity_array["jour"][$i]), strval($jour), 10) == 0) {
        for ($j = intval($heure_debut); $j <= intval($heure_fin); $j++) {
            $activity_array["disponible"][$j - 8] = "non";
        }
    }

}


for ($i = 0; $i < sizeof($activity_array); $i++) {

    for ($j = 8; $j < 23; $j++) {

        if (strcmp($activity_array["disponible"][$j - 8], "oui") == 0) {
            if (empty($jour_rencontre))
                $jour_rencontre = $activity_array["jour"][$i];
            $heure_rencontre = $activity_array["heure"][$j - 8];
            break;
        }

    }
}
$heure_rencontre = $heure_rencontre . ":00";
if (sizeof($utilisateur_array) <= sizeof($preferences_array) * 3) {
    if (empty($jour_rencontre) or empty($lieu)) {
        echo 0;
    } else {
        echo "{
	\"lieu\": \"$lieu\",
	\"jour\": \"$jour_rencontre\",
	\"heure\": \"$heure_rencontre\"

}";
    }
} else {
    echo 0;
}


$conn->close();
?>
