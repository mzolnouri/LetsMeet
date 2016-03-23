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
    private String date_;

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
        return date_;
    }

    public void setDate(String date) {
            date_=date;
    }
}
