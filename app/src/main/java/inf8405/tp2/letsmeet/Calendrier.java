package inf8405.tp2.letsmeet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by youssef on 22/03/2016.
 */
public class Calendrier {

    public List<Activite> ListDactivites_ = new ArrayList<Activite>();

    public Calendrier()
    {

    }

    public List<Activite> getListDactivites() {
        return ListDactivites_;
    }

    public void setListDactivites(List<Activite> listDactivites) {
        ListDactivites_ = listDactivites;
    }
}
