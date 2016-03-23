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
    private int mIdGroupe;
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
        mRadioGroup.check(R.id.rdBtnpGroup1);
        mIdGroupe = mRadioGroup.getCheckedRadioButtonId();


        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.rdBtnpGroup1:
                        DBContent.getInstance().getActualUser().setGroupeId("aff7a903-2d99-4bd1-b16b-69a2193d3284");
                        DBContent.getInstance().setActualGroupId("aff7a903-2d99-4bd1-b16b-69a2193d3284");
                        DBContent.getInstance().updateUserInformationInRemoteContent();
                        break;
                    case R.id.rdBtnpGroup2:
                        DBContent.getInstance().getActualUser().setGroupeId("1e215d6c-7055-41f9-8aed-d18dfd246da7");
                        DBContent.getInstance().setActualGroupId("1e215d6c-7055-41f9-8aed-d18dfd246da7");
                        DBContent.getInstance().updateUserInformationInRemoteContent();
                        break;
                    case R.id.rdBtnpGroup3:
                        DBContent.getInstance().getActualUser().setGroupeId("fc4c707a-049c-4c7f-9cb8-7bb5469db2ec");
                        DBContent.getInstance().setActualGroupId("fc4c707a-049c-4c7f-9cb8-7bb5469db2ec");
                        DBContent.getInstance().updateUserInformationInRemoteContent();
                        break;
                    case R.id.rdBtnpGroup4:
                        DBContent.getInstance().getActualUser().setGroupeId("43a31fa2-f3cb-40df-81fe-bcf3e7cbd3a3");
                        DBContent.getInstance().setActualGroupId("43a31fa2-f3cb-40df-81fe-bcf3e7cbd3a3");
                        DBContent.getInstance().updateUserInformationInRemoteContent();
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
