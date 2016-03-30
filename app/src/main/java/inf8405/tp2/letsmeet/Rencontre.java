package inf8405.tp2.letsmeet;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

// INF8405 - Laboratoire 2
//Auteurs : Najib Arbaoui (1608366) && Youssef Zemmahi (1665843) && Zolnouri Mahdi (1593999)
public class Rencontre {
    private String id_=new String();
    private String lieu1_ = new String();
    private String lieu2_ = new String();
    private String lieu3_ = new String();
    private Date date_;
    private String description_=new String();
    private String idGroupe_="0633a385-4b36-4135-9987-f48b02891d82";
    private String idPlanner_ = new String();
    private UUID uidFormat_ = UUID.fromString("91c83b36-e25c-11e5-9730-9a79f06e9478");

    public Rencontre ()
    {
        id_=uidFormat_.randomUUID().toString();
        date_= Calendar.getInstance().getTime();
        description_="description";
    }

    public Rencontre(String lieu1, String lieu2, String lieu3, String idGroupe,String idPlanner, String description)
    {
        id_=uidFormat_.randomUUID().toString();
        lieu1_=lieu1;
        lieu2_=lieu2;
        lieu3_=lieu3;
        idGroupe_=idGroupe;
        idPlanner_=idPlanner;
        date_= Calendar.getInstance().getTime();
        description_=description;
    }

    /*public Rencontre( String description, String idPlanner, String idGroupe, String date)
    {
        id_=uidFormat_.randomUUID().toString();
        description_=description;
        idPlanner_=idPlanner;
        idGroupe_=idGroupe;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");
        try {
            date_=sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }*/
    public String getId() {
        return id_;
    }

    public void setId(String id) {
        this.id_ = id;
    }

    public Date getDate() {
        return date_;
    }

    public void setDate(Date date) {
        this.date_ = date;
    }

    public String getDateStr() {
        DateFormat df = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");
        return  df.format(date_);
    }

    public String getJour()
    {
        DateFormat df = new SimpleDateFormat("yyyy-M-dd");
        return  df.format(date_);
    }

    public String getHeure()
    {
        DateFormat df = new SimpleDateFormat("hh:mm:ss");
        return  df.format(date_);
    }

    public void setDateStr(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");
        try {
            date_=sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public String getDescription() {
        return description_;
    }

    public void setDescription(String description) {
        this.description_ = description;
    }

    public String getIdGroupe() {
        return idGroupe_;
    }

    public void setIdGroupe(String idGroupe) {
        this.idGroupe_ = idGroupe;
    }

    public String getIdPlanner() {
        return idPlanner_;
    }

    public void setIdPlanner(String idPlanner) {
        this.idPlanner_ = idPlanner;
    }
    public String getLieu1() {
        return lieu1_;
    }

    public void setLieu1(String lieu1) {
        this.lieu1_ = lieu1;
    }

    public String getLieu2() {
        return lieu2_;
    }

    public void setLieu2(String lieu2) {
        this.lieu2_ = lieu2;
    }

    public String getLieu3() {
        return lieu3_;
    }

    public void setLieu3(String lieu3) {
        this.lieu3_ = lieu3;
    }
}
