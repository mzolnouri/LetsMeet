package inf8405.tp2.letsmeet;

import com.google.api.client.json.Json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by youssef on 04/03/2016.
 */

public final class Parseur {

    public static Map<String,Utilisateur> ParseToUsersMap(String message) throws JSONException {
        JSONArray json=new JSONArray(message);
        Map<String,Utilisateur> usersMap= new HashMap<String,Utilisateur>();
        for(int i=0;i<json.length();i++)
        {
            Utilisateur user= new Utilisateur();
            user.setId(json.getJSONObject(i).getString("idutilisateur"));
            user.setCourriel(json.getJSONObject(i).getString("courriel"));
            user.setPhotoEn64(json.getJSONObject(i).getString("photo"));
            user.setGroupeId(json.getJSONObject(i).getString("groupe_idgroupe"));
            user.setPositionId(json.getJSONObject(i).getString("position_idposition"));
            user.setPassword(json.getJSONObject(i).getString("password"));
            if(json.getJSONObject(i).getString("organisateur").contentEquals("1"))
            {
                user.setIsPlanner(true);
            }
            else
            {
                user.setIsPlanner(false);
            }

            usersMap.put(user.getId(), user);
        }
    return usersMap;
    }
    public static Map<String,Position> ParseToPositionsMap(String message) throws JSONException {
        JSONArray json=new JSONArray(message);
        Map<String,Position> positionsMap= new HashMap<String,Position>();
        for(int i=0;i<json.length();i++)
        {
            Position position= new Position();
            position.setId(json.getJSONObject(i).getString("idposition"));
            position.setLatitude(json.getJSONObject(i).getDouble("latitude"));
            position.setLongitude(json.getJSONObject(i).getDouble("longitude"));
            // TODO
            //position.setRadius(json.getJSONObject(i).getDouble("radius"));
            position.setDate(json.getJSONObject(i).getString("position_time"));
            positionsMap.put(position.getId(), position);
        }
        return positionsMap;
    }
    public static Map<String,Groupe> ParseToGroupeMap(String message) throws JSONException {
        JSONArray json=new JSONArray(message);
        Map<String,Groupe> groupesMap= new HashMap<String,Groupe>();
        for(int i=0;i<json.length();i++)
        {
            Groupe groupe= new Groupe();
            groupe.setId(json.getJSONObject(i).getString("idgroupe"));
            groupe.setGroupName(json.getJSONObject(i).getString("nom"));
            groupesMap.put(groupe.getId(), groupe);
        }
        return groupesMap;
    }
    public static Map<String,Preference> ParseToPreferencesMap(String message) throws JSONException {
        JSONArray json=new JSONArray(message);
        Map<String,Preference> PreferencesMap= new HashMap<String,Preference>();
        for(int i=0;i<json.length();i++)
        {
            Preference preference= new Preference();
            preference.setId(json.getJSONObject(i).getString("idpreference"));
           // TODO endroit
            int priorite = json.getJSONObject(i).getInt("priorite");
            switch (priorite)
            {
                case 1:
                    preference.setPriority(Constants.highPriority);
                    break;
                case 2:
                    preference.setPriority(Constants.mediumPriority);
                    break;
                case 3:
                    preference.setPriority(Constants.lowPriority);
                    break;
                default:
                    preference.setPriority(Constants.DefaultPriority);
            }
            PreferencesMap.put(preference.getId(), preference);
        }
        return PreferencesMap;
    }

    public static Utilisateur ParseJsonToUser(String userStr) throws JSONException {
        JSONArray temp = new JSONArray(userStr);
        // recuperation de la premiere case de l'array
        JSONObject json = temp.getJSONObject(0);
        Utilisateur user= new Utilisateur();
        user.setId(json.getString("idutilisateur"));
        user.setCourriel(json.getString("courriel"));
        user.setPhotoEn64(json.getString("photo"));
        user.setGroupeId(json.getString("groupe_idgroupe"));
        user.setPositionId(json.getString("position_idposition"));
        //user.setPassword(json.getString("password"));
        if(json.getString("organisateur").contentEquals("1"))
        {
            user.setIsPlanner(true);
        }
        else
        {
            user.setIsPlanner(false);
        }
        return user;
    }
    public static Groupe ParseJsonToGroup(String groupestr) throws JSONException {
        Groupe groupe = new Groupe();
        JSONArray Ajson = new JSONArray(groupestr);
        JSONObject json = Ajson.getJSONObject(0);

        groupe.setId(json.getString("idgroupe"));
        groupe.setGroupName(json.getString("nom"));
        return groupe;
    }
    public static Rencontre ParseJsonToRencontre(String rencontreStr) throws JSONException {
        JSONArray jsonArray = new JSONArray(rencontreStr);
        JSONObject json=jsonArray.getJSONObject(0);

        Rencontre rencontre = new Rencontre();
        rencontre.setId(json.getString("idrencontre"));
        rencontre.setLieu1(json.getString("lieu1"));
        rencontre.setLieu2(json.getString("lieu2"));
        rencontre.setLieu3(json.getString("lieu3"));
        //todo
        rencontre.setDateStr(json.getString("jour"));
        rencontre.setDescription(json.getString("description"));
        rencontre.setIdGroupe(json.getString("groupe_idgroupe"));
        rencontre.setIdPlanner(json.getString("idorganisateur"));
        return rencontre;

    }

    public static String ParseAuthentificationInfoToJsonFormat(String courriel, String password) throws JSONException {
        JSONObject json = new JSONObject();

        json.put("courriel", courriel);
        json.put("password", password);

        return  json.toString();
    }
    public static String ParseUserToJsonFormat(Utilisateur user) throws JSONException {
        JSONObject userJson = new JSONObject();
        JSONObject positionJson=new JSONObject();
        JSONObject json=new JSONObject();
        // create user
        userJson.put("idutilisateur",user.getId());
        userJson.put("courriel",user.getCourriel());
        userJson.put("photo",user.getPhotoEn64());
        if(user.isPlanner())
        {
            userJson.put("organisateur","1");
        }
        else
        {
            userJson.put("organisateur","0");
        }

        userJson.put("position_idposition",user.getPositionId());
        userJson.put("groupe_idgroupe",user.getGroupeId());
        userJson.put("password",user.getPassword());


        // creation de la partie position
        positionJson.put("idposition",user.getPositionId());
        positionJson.put("latitude",user.getPosition().getLatitude());
        positionJson.put("longitude",user.getPosition().getLongitude());
        positionJson.put("position_time",user.getPosition().getDateString());

        // JSON FINAL
        json.put("position",positionJson);
        json.put("utilisateur",userJson);
        return json.toString();

    }
    public static String ParsePositionToJsonFormat(Position pos) throws JSONException {
        JSONObject json = new JSONObject();
        JSONObject jsonPos = new JSONObject();
        json.put("idposition",pos.getId());
        json.put("latitude",pos.getLatitude());
        json.put("longitude",pos.getLongitude());
        json.put("position_time",pos.getDateString());

        jsonPos.put("position",json);

        return jsonPos.toString();
    }
    public static String ParseGroupToJsonFormat(Groupe groupe) throws JSONException {
        JSONObject res = new JSONObject();
        res.put("idgroupe",groupe.getId());
        res.put("nom",groupe.getGroupName());
        return res.toString();
    }
    public static String ParsePreferencesToJsonFormat(Utilisateur user) throws JSONException {
        JSONArray jArray = new JSONArray();

        for (int i=0;i<3;i++)
        {
            JSONObject json = parsePreferenceToJsonObject (user.getUserPreferences()[i]);
            json.put("idutilisateur",user.getId());
            jArray.put(json);
        }

        return jArray.toString();
    }

    private static JSONObject parsePreferenceToJsonObject (Preference preference) throws JSONException {
        JSONObject json=new JSONObject();
        json.put("idpreferences",preference.getId());
        json.put("endroit",preference.getAdresse());

        switch (preference.getPriority())
        {
            case Constants.highPriority:
                json.put("priorite","1");
                break;
            case Constants.mediumPriority:
                json.put("priorite","2");
                break;
            case Constants.lowPriority:
                json.put("priorite","3");
        }
        return json;
    }
}