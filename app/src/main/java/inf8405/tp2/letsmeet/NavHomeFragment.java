package inf8405.tp2.letsmeet;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

/**
 * Created by mahdi on 16-03-13.
 */
public class NavHomeFragment extends Fragment{
    View homeView;
    private RadioGroup mRadioGroup = null;
    private Button mBtnPreferences = null;
    private Button mBtnListeUtilisateurs = null;
    private Button mBtnCreerRencontre = null;
    private Button mBtnConfirmationRencontre = null;
    private RadioButton radioButtonItem;
    Activity mActivity;
    int mButtonSelected;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceBundle){
        mActivity = getActivity();

        homeView = inflater.inflate(R.layout.activity_nav_home, container, false);
        Map<String, Groupe> nameMap=DBContent.getInstance().getAllGroupsInformations();
        List<Groupe> listName= new ArrayList<Groupe>(nameMap.values());

       /* Manage radio group */
        mRadioGroup = (RadioGroup) homeView.findViewById(R.id.rdGrpListChoixDeGroup);
        int itemId;
        for(int i = 0; i < mRadioGroup.getChildCount(); i++){
            itemId = mRadioGroup.getChildAt(i).getId();
            radioButtonItem = (RadioButton) homeView.findViewById(itemId);
            radioButtonItem.setText(listName.get(i).getGroupName());

        }
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case 0:
                        DBContent.getInstance().getActualUser().setGroupeId("0633a385-4b36-4135-9987-f48b02891d82");
                        break;
                    case 1:
                        DBContent.getInstance().getActualUser().setGroupeId("28bc1b1f-fsdfdf9-bq12oo-08dsadsagfhgaa44");
                        break;
                    case 2:
                        DBContent.getInstance().getActualUser().setGroupeId("294feaa9-852d-45e6-8151-178019aa3358");
                        break;
                    case 3:
                        DBContent.getInstance().getActualUser().setGroupeId("320431b2-e673-4c9f-ba0a-2f9b6fbd02a1");
                        break;
                }
            }
        });

        /* Manage the preferences button */
        mBtnPreferences = (Button) homeView.findViewById(R.id.btnPreferencesCG);
        mBtnPreferences.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                preferencesButtonClicked(v);
            }
        });

        /* Manage the preferences button */
        mBtnListeUtilisateurs = (Button) homeView.findViewById(R.id.btnListeUtilisateursCG);
        mBtnListeUtilisateurs.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                listeUtilisateursButtonClicked(v);
            }
        });

         /* Manage the preferences button */
        mBtnCreerRencontre = (Button) homeView.findViewById(R.id.btnCreateRencontreCG);
        mBtnCreerRencontre.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                creerRencontreButtonClicked(v);
            }
        });

        /* Manage the confirmation rencontre button */
        mBtnConfirmationRencontre = (Button) homeView.findViewById(R.id.btnConfirmationRencontreCG);
        mBtnConfirmationRencontre.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                confirmationRencontreButtonClicked(v);
            }
        });
        return homeView;
    }

    public void confirmationRencontreButtonClicked (View view) {
        Intent i = new Intent(getActivity(), IConfirmationPreferences.class);
        startActivity(i);

    }

    public void preferencesButtonClicked (View view) {
        Intent i = new Intent(getActivity(), InterfacePreferences.class);
        startActivity(i);

    }

    public void listeUtilisateursButtonClicked (View view) {
        Intent i = new Intent(getActivity(), UserList.class);
        startActivity(i);

    }

    public void creerRencontreButtonClicked (View view) {
        Intent i = new Intent(getActivity(), CreerRencontre.class);
        startActivity(i);
    }
}
