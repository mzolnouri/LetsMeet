package inf8405.tp2.letsmeet;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by youssef on 22/03/2016.
 */
public class Activite {
    private String id_=new String();
    private String description_=new String();
    private String debut_=new String();
    private String fin_=new String();
    private UUID uidFormat_ = UUID.fromString("91c83b36-e25c-11e5-9730-9a79f06e9478");

    public Activite()
    {
        id_=uidFormat_.randomUUID().toString();
    }
    public Activite(String description, String debut,String fin)
    {
        id_=uidFormat_.randomUUID().toString();
        description_=description;
        debut_=debut;
        fin_=fin;
    }
    public String getId() {
        return id_;
    }

    public void setId(String id) {
        this.id_ = id;
    }

    public String getDescription() {
        return description_;
    }

    public void setDescription(String description) {
        this.description_ = description;
    }

    public String getDebutStr() {

        return debut_;
    }

    public void setDebutStr(String debut) {
        debut_=debut;
    }

    public String getFinStr() {
        return fin_;
    }

    public void setFinStr(String fin) {
        fin_=fin;
    }
}
