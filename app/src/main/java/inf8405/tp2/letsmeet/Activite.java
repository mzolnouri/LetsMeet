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
    private Date debut_;
    private Date fin_;
    private UUID uidFormat_ = UUID.fromString("91c83b36-e25c-11e5-9730-9a79f06e9478");

    public Activite()
    {
        id_=uidFormat_.randomUUID().toString();
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

        DateFormat df = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");
        return  df.format(debut_);
    }

    public void setDebutStr(String debut) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");
        try {
            this.debut_=sdf.parse(debut);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public String getFinStr() {
        DateFormat df = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");
        return  df.format(fin_);
    }

    public void setFinStr(String fin) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");
        try {
            this.fin_=sdf.parse(fin);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
