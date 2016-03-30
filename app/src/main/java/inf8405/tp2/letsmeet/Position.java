package inf8405.tp2.letsmeet;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.text.DateFormat;
import java.util.UUID;

// INF8405 - Laboratoire 2
//Auteurs : Najib Arbaoui (1608366) && Youssef Zemmahi (1665843) && Zolnouri Mahdi (1593999)

// exemple d'information recupere pour une position formet json : https://developers.google.com/maps/documentation/geocoding/intro?hl=fr
public class Position {

    private String id_=new String();
    private double longitude_=0.0;
    private double latitude_=0.0;
    private double radius_=0.0;
    private String adresse_=new String();
    private Date date_;
    private UUID uidFormat_ = UUID.fromString("91c83b36-e25c-11e5-9730-9a79f06e9478");

    public Position()
    {
        id_=uidFormat_.randomUUID().toString();
        date_= Calendar.getInstance().getTime();
    }
    public Position(double latitude, double longitude) {
        longitude_=longitude;
        latitude_=latitude;
        date_= Calendar.getInstance().getTime();
    }

    public Position(String id,double lng, double lat)
    {
        id_=uidFormat_.randomUUID().toString();
        longitude_=lng;
        latitude_=lat;
        date_= Calendar.getInstance().getTime();
    }

    public String getId() {
        return id_;
    }

    public void setId(String id) {
        this.id_ = id;
    }

    public double getLongitude() {
        return longitude_;
    }

    public void setLongitude(double longitude) {
        this.longitude_ = longitude;
    }

    public double getLatitude() {
        return latitude_;
    }

    public void setLatitude(double latitude) {
        this.latitude_ = latitude;
    }


    public Date getDate()
    {
        return date_;
    }

    public void setActualDateAndTime()
    {
        date_= Calendar.getInstance().getTime();
    }
    public void setDate(String date)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");
        try {
            date_=sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    public String getDateString()
    {
        DateFormat df = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");
        return  df.format(date_);
    }

    public double getRadius() {
        return radius_;
    }

    public void setRadius(double radius) {
        this.radius_ = radius;
    }

    public String getAdresse() {
        return adresse_;
    }

    public void setAdresse(String adresse) {
        this.adresse_ = adresse;
    }
}
