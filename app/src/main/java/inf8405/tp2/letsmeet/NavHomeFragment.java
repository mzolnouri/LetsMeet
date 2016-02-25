package inf8405.tp2.letsmeet;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import javax.annotation.Nullable;

/**
 * Created by mahdi on 16-03-13.
 */
public class NavHomeFragment extends Fragment{
    View homeView;
    private RadioGroup radioGroup = null;
    private Button btnRencontres = null;
    private Button btnListeUtilisateurs = null;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceBundle){
        homeView = inflater.inflate(R.layout.activity_nav_home, container, false);
        //btnRencontres = (Button) findViewById(R.id.btnRencontresCG);
        //btnListeUtilisateurs = (Button) findViewById(R.id.btnListeUtilisateursCG);
        //radioGroup = (RadioGroup) findViewById(R.id.rdGrpListChoixDeGroup);
        return homeView;
    }
}
