package inf8405.tp2.letsmeet;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.UUID;

/**
 * Created by youssef on 24/02/2016.
 */
public class Utilisateur {
    private String id_= new String();
    private String courriel_=new String();
    private Bitmap photoEnBitMap_;// = Bitmap.createBitmap(" ");
    private boolean isPlanner_=false;
    private Position position_=new Position();
    private Boolean checkedBox_;
    private Preference[] userPreferences_=new Preference[3];
    private String photoEn64_ = new String();
    private String positionId_=new String();
    private String groupeId_=new String();
    private String password_=new String();
    private UUID uidFormat_ = UUID.fromString("91c83b36-e25c-11e5-9730-9a79f06e9478");
    private Calendrier calendrier_=new Calendrier();

    public Utilisateur()
   {
       id_=uidFormat_.randomUUID().toString();
       positionId_=uidFormat_.randomUUID().toString();
       for (int i=0;i<3;i++)
       {
           userPreferences_[i]=null;
       }
   }
    public Utilisateur(String courriel, String password, String groupid)
    {
        id_=uidFormat_.randomUUID().toString();
        positionId_=uidFormat_.randomUUID().toString();
        courriel_=courriel;
        password_=password;
        groupeId_=groupid;
        for (int i=0;i<3;i++)
        {
            userPreferences_[i]=null;
        }
    }
    public Utilisateur(String id, String name, String courriel, boolean isPlanner)
    {
        positionId_=uidFormat_.randomUUID().toString();
        id_=id;
        courriel_=courriel;
        isPlanner_=isPlanner;
        for (int i=0;i<3;i++)
        {
            userPreferences_[i]=null;
        }
    }
    public Bitmap getPhotoEnBitmap(){
        return photoEnBitMap_;
    }
    public void setPhotoEnBitmap(Bitmap photo){
        this.photoEnBitMap_ = photo;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        photoEn64_= Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
    public Boolean getCheckedBox() {
        return checkedBox_;
    }

    public void setCheckedBox(Boolean checkedBox) {
        this.checkedBox_ = checkedBox;
    }
    public String getId() {
        return id_;
    }

    public void setId(String id) {
        this.id_ = id;
    }

    public String getCourriel() {
        return courriel_;
    }

    public void setCourriel(String courriel) {
        this.courriel_ = courriel;
    }

    public boolean isPlanner() {
        return isPlanner_;
    }

    public void setIsPlanner(boolean isPlanner) {
        this.isPlanner_ = isPlanner;
    }

    public Position getPosition() {
        return position_;
    }

    public void setPosition(Position position) {
        this.position_ = position;
    }

    public Preference[] getUserPreferences() {
        return userPreferences_;
    }

    public void setUserPreferences(Preference[] userPreferences) {
        this.userPreferences_ = userPreferences;
    }

    public void addPreferences(Preference preference)
    {
        boolean addedFlag=false;
        for (int i=0;i<3;i++)
        {
            if(userPreferences_[i]==null)
            {
                userPreferences_[i]=preference;
                return;
            }
            else if(preference.getPriority().contentEquals(userPreferences_[i].getPriority()))
            {
                userPreferences_[i]=preference;
                return;
            }
        }
    }
// indice a recevoir de 0 a 2
    public boolean switchPreferencesPriorities(int x, int y )
    {
        if (userPreferences_[x]==null || userPreferences_[y]==null)
        {
            // probleme si ca entre ici
            return false;
        }

        String temp = userPreferences_[x].getPriority();
        userPreferences_[x].setPriority(userPreferences_[y].getPriority());
        userPreferences_[y].setPriority(temp);

        return true;
    }

    public String getPhotoEn64() {
        return photoEn64_;
    }

    public void setPhotoEn64(String photoEn64) {
        byte[] decodedString = Base64.decode(photoEn64, Base64.DEFAULT);
        photoEnBitMap_ = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        this.photoEn64_ = photoEn64;
    }

    public String getPositionId() {
        return positionId_;
    }

    public void setPositionId(String positionId) {
        this.positionId_ = positionId;
    }

    public String getGroupeId() {
        return groupeId_;
    }

    public void setGroupeId(String groupeId) {
        this.groupeId_ = groupeId;
    }

    public String getPassword() {
        return password_;
    }

    public void setPassword(String password) {
        this.password_ = password;
    }

    public boolean IsUserNameValide(){
        /* TO DO */
        // Vérifier si le user name est valide

        return false;
    }
    public boolean IsPasswordValide(){
        /* TO DO */
        // Vérifier si le user name est valide

        return false;
    }
    public boolean InscrireNouveauUtilisateur(){
        /* TO DO */
        // Entregistrer l'utilisateur et retourner la validité de l'opération

        return false;
    }

    public Calendrier getCalendrier() {
        return calendrier_;
    }

    public void setCalendrier(Calendrier calendrier) {
        this.calendrier_ = calendrier;
    }

    public List<Activite> getListeActivite()
    {
        return calendrier_.getListDactivites();
    }
    public void ajouterActivite(Activite activite)
    {
        
    }
}
