package inf8405.tp2.letsmeet;

import java.util.ArrayList;
import java.util.List;

// INF8405 - Laboratoire 2
//Auteurs : Najib Arbaoui (1608366) && Youssef Zemmahi (1665843) && Zolnouri Mahdi (1593999)

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
