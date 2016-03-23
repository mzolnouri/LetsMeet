package inf8405.tp2.letsmeet;

/**
 * Created by mahdi on 16-03-22.
 */

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import com.google.android.gms.location.LocationListener;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InterfacePreferences extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleApiClient fGoogleApiClient;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private LocationRequest fLocationRequest;
    public static final String TAG = UserList.class.getSimpleName();
    private GoogleMap fMap;
    private String mPreference1 = "";
    private String mPreference2 = "";
    private String mPreference3 = "";
    private Button btnEnregistrer = null;
    private Button btnAnnuler = null;
    ArrayList<Preference> fPreferences;

    /* Pop up */
    ContentResolver fResolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);


        // Create the LocationRequest object
        fLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        fGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();


        fPreferences = new ArrayList<>();
        for (Map.Entry<String, Preference> entry : DBContent.getInstance().getPreferencesMap().entrySet()) {
            fPreferences.add(entry.getValue());
        }

        /* Manage des snipers */
        // Spinner element
        Spinner spinner1 = (Spinner) findViewById(R.id.spinner1P);
        Spinner spinner2 = (Spinner) findViewById(R.id.spinner2P);
        Spinner spinner3 = (Spinner) findViewById(R.id.spinner3P);

        // Spinner click listener
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // On selecting a spinner item
                //Spinner mySpinner = (Spinner)findViewById(R.id.spinner1P);
                String item = parent.getItemAtPosition(position).toString();

                // Showing selected spinner item
                Toast.makeText(parent.getContext(), "Selected p1: " + item, Toast.LENGTH_LONG).show();
                mPreference1 = item;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // On selecting a spinner item
                String item = parent.getItemAtPosition(position).toString();

                // Showing selected spinner item
                Toast.makeText(parent.getContext(), "Selected p2: " + item, Toast.LENGTH_LONG).show();
                mPreference2 = item;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // On selecting a spinner item
                String item = parent.getItemAtPosition(position).toString();

                // Showing selected spinner item
                Toast.makeText(parent.getContext(), "Selected p3: " + item, Toast.LENGTH_LONG).show();
                mPreference3 = item;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        List<String> catPreferencesList1 = new ArrayList<String>();
        final Rencontre rencontre=DBContent.getInstance().getActualGroupeRencontre();
        List<String> catPreferencesList2 = new ArrayList<String>();
        List<String> catPreferencesList3 = new ArrayList<String>();
        if(rencontre!=null)
        {
            // Spinner Drop down elements of list 1
            catPreferencesList1.add(rencontre.getLieu1());
            catPreferencesList1.add(rencontre.getLieu2());
            catPreferencesList1.add(rencontre.getLieu3());

            // Spinner Drop down elements of list 2
            catPreferencesList2.add(rencontre.getLieu1());
            catPreferencesList2.add(rencontre.getLieu2());
            catPreferencesList2.add(rencontre.getLieu3());

            // Spinner Drop down elementsof list 3
            catPreferencesList3.add(rencontre.getLieu1());
            catPreferencesList3.add(rencontre.getLieu2());
            catPreferencesList3.add(rencontre.getLieu3());
        }




        // Creating adapter for 3 spinners
        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, catPreferencesList1);
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, catPreferencesList2);
        ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, catPreferencesList3);

        // Drop down layout style - list view with radio button
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner1.setAdapter(dataAdapter1);
        spinner2.setAdapter(dataAdapter2);
        spinner3.setAdapter(dataAdapter3);


        fResolver = this.getContentResolver();

        if (fPreferences != null) {
            Log.e("count", "" + fPreferences.size());
            if (fPreferences.size() == 0) {
                Toast.makeText(InterfacePreferences.this, "No Preferences in your list.", Toast.LENGTH_LONG).show();
            }

        } else {
            Log.e("Cursor close 1", "----------------");
        }

        btnEnregistrer = (Button) findViewById(R.id.btnSavePerferencesP);
        btnEnregistrer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPreference1 == "")
                    mPreference1 = rencontre.getLieu1();
                if (mPreference2 == "")
                    mPreference2 = rencontre.getLieu2();
                if (mPreference3 == "")
                    mPreference3 = rencontre.getLieu3();

                DBContent.getInstance().addPreference(Constants.highPriority, mPreference1);
                DBContent.getInstance().addPreference(Constants.mediumPriority, mPreference2);
                DBContent.getInstance().addPreference(Constants.lowPriority, mPreference3);

                DBContent.getInstance().addPreferencesToRemoteContent(DBContent.getInstance().getActualUser());
                //Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                //startActivity(intent);
                finish();
            }
        });
        btnAnnuler = (Button) findViewById(R.id.btnAnnulerP);
        btnAnnuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                //startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //setUpMapIfNeeded();
        fGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (fGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(fGoogleApiClient, (com.google.android.gms.location.LocationListener) this);
            fGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        fMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        fMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        fMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "Location services connected.");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(fGoogleApiClient);
        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(fGoogleApiClient, fLocationRequest, (com.google.android.gms.location.LocationListener) this);
        } else {
            handleNewLocation(location);
        }
        ;
    }

    private void handleNewLocation(Location location) {
        Log.d(TAG, location.toString());
        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();
        LatLng latLng = new LatLng(currentLatitude, currentLongitude);
        MarkerOptions options = new MarkerOptions().position(latLng).title("Actual location");
        fMap.addMarker(options);
        float zoomLevel = 16; //This goes up to 21
        fMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Location services suspended. Please reconnect.");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }

}
