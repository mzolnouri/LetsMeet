package zemmahi.youssef.letsmeet;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by mahdi on 16-02-29.
 */
public class Location_Fragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater .inflate(R.layout.location_fragment, container, false);
        return rootView;
    }
}
