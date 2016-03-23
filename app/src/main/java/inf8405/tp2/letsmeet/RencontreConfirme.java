package inf8405.tp2.letsmeet;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by youssef on 22/03/2016.
 */
public class RencontreConfirme {

    private String lieu_=new String();
    private Date date_;

    public RencontreConfirme()
    {

    }
    public String getLieu() {
        return lieu_;
    }

    public void setLieu(String lieu) {
        this.lieu_ = lieu;
    }

    public String getDate() {
        DateFormat df = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");
        return  df.format(date_);
    }

    public void setDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");
        try {
            date_=sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
