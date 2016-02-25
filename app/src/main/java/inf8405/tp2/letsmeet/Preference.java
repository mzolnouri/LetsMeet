package inf8405.tp2.letsmeet;

import java.util.UUID;

/**
 * Created by youssef on 24/02/2016.
 */
public class Preference {
    private String id_=new String();
    private String adresse_ = new String();
    private String priority_=Constants.DefaultPriority;
    private String userId_=new String();
    private UUID uidFormat_ = UUID.fromString("91c83b36-e25c-11e5-9730-9a79f06e9478");

    public Preference()
    {
        id_=uidFormat_.randomUUID().toString();
    }
    public Preference(String adresse,String priority,String userId)
    {
        id_=uidFormat_.randomUUID().toString();
        priority_=priority;
        adresse_=adresse;
        userId_=userId;
    }

    public String getPriority() {
        return priority_;
    }

    public void setPriority(String priority) {
        this.priority_ = priority;
    }

    public void setToHighPriority()
    {
        priority_=Constants.highPriority;
    }
    public void setToMediumPriority()
    {
        priority_=Constants.mediumPriority;
    }
    public void setToLowPriority()
    {
        priority_=Constants.lowPriority;
    }
    public void setToDefaultPriority()
    {
        priority_=Constants.DefaultPriority;
    }

    public String getId() {
        return id_;
    }

    public void setId(String id) {
        this.id_ = id;
    }

    public String getUserId() {
        return userId_;
    }

    public void setUserId(String userId) {
        this.userId_ = userId;
    }
    public String getAdresse() {
        return adresse_;
    }

    public void setAdresse(String adresse) {
        this.adresse_ = adresse;
    }
}
