package inf8405.tp2.letsmeet;

import android.util.ArrayMap;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.transform.sax.TemplatesHandler;

/**
 * Created by youssef on 05/03/2016.
 */
public class DBContent {

    // la map contient les utilisateurs qui appartiennent au groupe auquel appartient l'utilisateur
    private Map<String,Utilisateur> userMap_ = new HashMap<String,Utilisateur>();
    private Map<String,Preference> preferencesMap_ = new HashMap<String,Preference>();
    private Map<String,Groupe> groupsMap_ = new HashMap<String,Groupe>();
    private Map<String,Position> positionsMap_ = new HashMap<String,Position>();
    private String actualGroupId_= new String();
    private String actualUserId_=new String();
    // seul attribut commun permettant de verifier la reponse a la requette http a l'interieur d'un thread
    private boolean flagForResponses = false;
    private String responseStr = new String();

    // instance du singleton
    private static DBContent instance_ = null;

    private DBContent ()
    {

    }

    public static DBContent getInstance()
    {
        if(instance_==null)
        {
            instance_ = new DBContent();
        }
        return instance_;
    }
    public static void destroyInstance()
    {
        instance_=null;
    }

    public void addPreference(String priorite, String adresse)
    {
        Preference preference = new Preference(adresse,priorite,actualUserId_);
        userMap_.get(actualUserId_).addPreferences(preference);

    }

    // echanger les priorite de deux preferences
    public void switchUserPreferences(int x,int y)
    {
        userMap_.get(actualUserId_).switchPreferencesPriorities(x, y);
    }

    // cree une nouvelle rencontre dans le groupe actuel par l'utilisateur actuel avec les infos en paramettre
    public boolean creerRencontre(String lieu, String description, String date)
    {
        return groupsMap_.get(actualGroupId_).setRencontre(new Rencontre(lieu,description,actualUserId_,actualGroupId_,date));
    }

    // dans le cas ou il n'est pas creer il instancie un nouveau et offre la possibilite de le modifier
    // si deja creer peut etre recuperer pour modifier ses attrinuts et infos necessaires
    public Rencontre recupererRencontre()
    {
        if(groupsMap_.get(actualGroupId_).getRencontre()==null)
        {
            groupsMap_.get(actualGroupId_).setRencontre(new Rencontre());
        }
        return groupsMap_.get(actualGroupId_).getRencontre();
    }
    // recupere l'utilisateur actuel
    public Utilisateur getActualUser()
    {
        return userMap_.get(actualUserId_);
    }
    // recupere le groupe actuel
    public Groupe getActualGroup()
    {
        return groupsMap_.get(actualGroupId_);
    }
    // todo fonction a revoir si utile ou pas
    private void InitialSyncOfElements()
    {
        for (Map.Entry<String, Preference> preference : preferencesMap_.entrySet())
        {
            if(userMap_.containsKey(preference.getValue().getUserId()))
            {
                userMap_.get(preference.getValue().getUserId()).addPreferences(preference.getValue());
            }
            else
            {
                Log.d("problem in initial sort","user not found");
            }
        }

        for (Map.Entry<String, Utilisateur> user : userMap_.entrySet())
        {
            if(positionsMap_.containsKey(user.getValue().getPositionId()))
            {
                user.getValue().setPosition(positionsMap_.get(user.getValue().getPositionId()));
            }
            else
            {
                Log.d("problem in initial sort","Position not found in the map");
            }
            if(groupsMap_.containsKey(user.getValue().getGroupeId()))
            {
                groupsMap_.get(user.getValue().getGroupeId()).addUser(user.getValue());
            }
        }

    }
    // creation d'utilisateur gere , retourn si added ou pas
    public String CreerNouvelUtilisateur(final String email, final String password)
    {
        responseStr=Constants.UserNotAdded;
        Thread thread = new Thread(new Runnable() {
            public void run() {
                // todo password enregistre localement est dangereux, voir solution alternative
                Utilisateur NUtilisateur = new Utilisateur(email, password, actualGroupId_);
                try {
                    Log.d("CreerNouvelUtilisateur", "cest mon test a moi");
                    // reponse true ou false du cote serveur
                    String reponsePost = DBConnexion.postRequest("http://najibarbaoui.com/najib/insert_utilisateur.php", Parseur.ParseUserToJsonFormat(NUtilisateur));
                    if(reponsePost.contentEquals("1"))
                    {
                        responseStr=Constants.UserAdded;
                        userMap_.put(NUtilisateur.getId(),NUtilisateur);
                        actualGroupId_=NUtilisateur.getId();
                        actualGroupId_=NUtilisateur.getGroupeId();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }});
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return responseStr;

    }

    // authentification envoit une requette au serveur le serveur renvoit une reponse positive ou negative
    // si positive, renvoit les infos de l<utilisateur sinon renvoit quelle est le probleme
    public String authentification(final String courriel, final String password)
    {;
        responseStr=Constants.WrongEmail;
        Thread thread = new Thread(new Runnable() {
            public void run() {
                try {
                    Log.d("authentification", "c<est mon test a moi");
                    String reponsePost= DBConnexion.postRequest("http://najibarbaoui.com/najib/ouvrirsession.php",Parseur.ParseAuthentificationInfoToJsonFormat(courriel, password));

                    if(reponsePost.contentEquals("0"))
                    {
                        responseStr=Constants.WrongPassword;
                    }
                    else if(!reponsePost.contentEquals("1"))
                    {
                        responseStr=Constants.AccessGranted;
                        Utilisateur utilisateurActuel;
                        utilisateurActuel = Parseur.ParseJsonToUser(reponsePost);
                        actualUserId_=utilisateurActuel.getId();
                        actualGroupId_=utilisateurActuel.getGroupeId();
                        userMap_.put(actualUserId_, utilisateurActuel);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return responseStr;
    }

    // fonction de mise a jour de la position de l'utilisateur actuel dans la BD
    public void UpdateRemotePosition()
    {
        Thread thread = new Thread(new Runnable() {
            public void run() {
                Log.d("UpdatePosition", "c mon test a moi");
                try {
                    DBConnexion.postRequest("http://najibarbaoui.com/najib/update_position.php",
                            Parseur.ParsePositionToJsonFormat(userMap_.get(actualUserId_).getPosition()));

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // recuperation de tous les utilisateurs
    public  void getAllUsers()
    {
        // le clear au cas ou la map contient deja klke chose
        userMap_.clear();
        Thread UsersThread = new Thread(new Runnable() {
            public void run() {
                Log.d("Users test", "c mon test a moi");
                try{
                    // TODO set the right url
                    userMap_ = Parseur.ParseToUsersMap(DBConnexion.getRequest("http://najibarbaoui.com/najib/utilisateurs.php"));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        UsersThread.start();
        try {
            UsersThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // recuperation d'un utilisateur selon son id
    public Utilisateur getUserById(final String idUser)
    {
        if( userMap_.containsKey(idUser))
        {
            return userMap_.get(idUser);
        }
        else
        {
            final Utilisateur[] user = new Utilisateur[1];
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {

                    try {
                        user[0] = Parseur.ParseJsonToUser(DBConnexion.getRequest("http://najibarbaoui.com/najib/utilisateur.php?id_utilisateur="
                        + idUser));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return user[0];
        }
    }

    // recuperation des informations d'un groupe a partir de l'id d'un utilisateur de ce groupe
    public Groupe getGroupdInformationFromUserId(final String userId)
    {
        final Groupe[] groupe = new Groupe[1];
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String reponse = DBConnexion.getRequest("http://najibarbaoui.com/najib/groupebyidutilisateur.php?id_utilisateur="+
                            userId);
                    groupe[0] =Parseur.ParseJsonToGroup(reponse);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return groupe[0];
    }
    public  void getAllGroupsInformations()
    {
       // au cas ou
        groupsMap_.clear();
        Thread GroupsThread = new Thread(new Runnable() {
            public void run() {
                Log.d("Groups test", "c mon test a moi");
                DBConnexion con=new DBConnexion();
                try{
                    // TODO set the right url
                    groupsMap_ = Parseur.ParseToGroupeMap(con.getRequest("http://najibarbaoui.com/najib/groupe.php"));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
        GroupsThread.start();
        try {
            GroupsThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    // ajouter nouveau groupe
    public String AddNewGroupToRemoteContent(String nom)
    {
        responseStr=Constants.GroupNotAdded;
        final Groupe group = new Groupe(nom);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String reponse = DBConnexion.postRequest("http://najibarbaoui.com/najib/insert_groupe.php",
                            Parseur.ParseGroupToJsonFormat(group));
                    if(reponse.contentEquals("0"))
                    {
                        responseStr=Constants.GroupAdded;
                        actualGroupId_=group.getId();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return responseStr;
    }
    // ajout des preferences a la db
    public void addPreferencesToRemoteContent(final Utilisateur user)
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String preferencesStr = Parseur.ParsePreferencesToJsonFormat(user);
                    DBConnexion.postRequest("http://najibarbaoui.com/najib/insert_preferences.php",preferencesStr);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    // recuperation de la liste des utilisateurs selon l'id du groupe
    public  void GetUsersFromGroup(final String idGroupe)
    {
        // le clear au cas ou
        userMap_.clear();
        Thread UsersThread = new Thread(new Runnable() {
            public void run() {
                Log.d("Users test", "c mon test a moi");
                try{
                    userMap_ = Parseur.ParseToUsersMap(DBConnexion.getRequest(" http://najibarbaoui.com/najib/utilisateurbygroupe.php?id_groupe="
                    + URLEncoder.encode(idGroupe,"UTF-8")));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
        UsersThread.start();
        try {
            UsersThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void mettreAjourPositionsMembresDuGroupe()
    {
        Thread thread = new Thread(new Runnable() {
            public void run() {
                // todo right url
                String url = "";
                try {
                    url+="?groupe_idgroupe=" + URLEncoder.encode(actualGroupId_,"UTF-8");
                    Map<String, Position> newPositions= new HashMap<String, Position>();

                    try {
                        newPositions = Parseur.ParseToPositionsMap(DBConnexion.getRequest(url));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    for(Map.Entry<String, Utilisateur> entry : userMap_.entrySet())
                    {
                        if(newPositions.containsKey(entry.getValue().getPositionId()))
                        {
                            entry.getValue().setPosition(newPositions.get(entry.getValue().getPositionId()));
                        }
                        else
                        {
                            Log.d("Probelem","when updating users position");
                        }
                    }

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            } });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Rencontre recupererLaRencontreGroupeBD(final String idGroupe)
    {
        final Rencontre[] rencontre = new Rencontre[1];
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String reponse = DBConnexion.getRequest("http://najibarbaoui.com/najib/rencontrebygroupe.php?id_groupe="
                    + URLEncoder.encode(idGroupe,"UTF-8"));
                    rencontre[0] =Parseur.ParseJsonToRencontre(reponse);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return rencontre[0];
    }

    public void SynchronizeLocalPreferencesFromRemoteContent()
    {
        Thread PreferencesThread = new Thread(new Runnable() {
            public void run() {
                Log.d("Preferences test ", "c mon test a moi");
                DBConnexion con=new DBConnexion();
                try{
                    // TODO set the right url
                    preferencesMap_ = Parseur.ParseToPreferencesMap(con.getRequest("http://najibarbaoui.com/najib"));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
        PreferencesThread.start();
        try {
            PreferencesThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public  void GetUsersPositions(String IdGroupe)
    {
        Thread PositionsThread= new Thread(new Runnable() {
            public void run() {
                Log.d("Positions test", "c mon test a moi");
                DBConnexion con=new DBConnexion();
                try{
                    // TODO set the right url
                    positionsMap_ = Parseur.ParseToPositionsMap(con.getRequest("http://najibarbaoui.com/najib"));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
        PositionsThread.start();
        try {
            PositionsThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public Map<String, Utilisateur> getUserMap() {
        return userMap_;
    }

    public void setUserMap_(Map<String, Utilisateur> userMap) {
        this.userMap_ = userMap;
    }

    public Map<String, Preference> getPreferencesMap() {
        return preferencesMap_;
    }

    public void setPreferencesMap(Map<String, Preference> preferencesMap) {
        this.preferencesMap_ = preferencesMap;
    }

    public Map<String, Groupe> getGroupsMap() {
        return groupsMap_;
    }

    public void setGroupsMap(Map<String, Groupe> groupsMap) {
        this.groupsMap_ = groupsMap;
    }

    public Map<String, Position> getPositionsMap() {
        return positionsMap_;
    }

    public void setPositionsMap(Map<String, Position> positionsMap) {
        this.positionsMap_ = positionsMap;
    }

    public String getActualGroupId() {
        return actualGroupId_;
    }

    public void setActualGroupId(String actualGroupId) {
        this.actualGroupId_ = actualGroupId;
    }

    public String getActualUserId() {
        return actualUserId_;
    }

    public void setActualUserId(String actualUserId_) {
        this.actualUserId_ = actualUserId_;
    }
}
