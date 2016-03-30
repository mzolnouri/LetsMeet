package inf8405.tp2.letsmeet;
// INF8405 - Laboratoire 2
//Auteurs : Najib Arbaoui (1608366) && Youssef Zemmahi (1665843) && Zolnouri Mahdi (1593999)
import android.Manifest;
import android.content.ContentResolver;
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
import android.widget.ListView;
import android.widget.SearchView;
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

public class UserList extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleApiClient fGoogleApiClient;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private LocationRequest fLocationRequest;
    public static final String TAG = UserList.class.getSimpleName();
    private GoogleMap fMap;
    ArrayList<Utilisateur> fUtilisateurs;
    List<Utilisateur> fUsersTemp;
    /* Contact fList */
    ListView fListView;
    /* Cursor to load contacts list */
    String fId, fEmail;

    /* Pop up */
    ContentResolver fResolver;
    SearchView fSearchView;
    UserAdapter fUserAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_liste);

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

        /* Get the map containing all users */
        //DBContent.getInstance().GetUsersFromGroup(DBContent.getInstance().getActualGroupId());
        fUtilisateurs = new ArrayList<>();
        Map<String,Utilisateur> myMap=DBContent.getInstance().GetUsersFromGroup(DBContent.getInstance().getActualGroupId());
        fListView = (ListView) findViewById(R.id.lstVwcontacts_list);
        for(Map.Entry<String, Utilisateur> entry : myMap.entrySet())
        {
            fUtilisateurs.add(entry.getValue());

        }

        fResolver = this.getContentResolver();


        if (fUtilisateurs != null) {
            Log.e("count", "" + fUtilisateurs.size());
            if (fUtilisateurs.size() == 0) {
                Toast.makeText(UserList.this, "No contacts in your contact list.", Toast.LENGTH_LONG).show();
            }

            fUserAdapter = new UserAdapter(fUtilisateurs, UserList.this);
            fListView.setAdapter(fUserAdapter);

            // Select item on listclick
            fListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    Log.e("search", "here---------------- listener");

                    DBContent dbContent=DBContent.getInstance();
                    Utilisateur utilisateurData = fUtilisateurs.get(i);
                    Position position=DBContent.getInstance().GetUserPosition(utilisateurData.getId());
                    Toast.makeText(UserList.this, "You've selected: " + utilisateurData.getCourriel(), Toast.LENGTH_LONG).show();

                    double currentLatitude = position.getLatitude();
                    double currentLongitude = position.getLongitude();
                    LatLng latLng = new LatLng(currentLatitude, currentLongitude);
                    MarkerOptions options = new MarkerOptions().position(latLng).title("Location of " + utilisateurData.getCourriel());
                    fMap.clear();
                    fMap.addMarker(options);
                    float zoomLevel = 16; //This goes up to 21
                    fMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
                }
            });

            fListView.setFastScrollEnabled(true);

        } else {
            Log.e("Cursor close 1", "----------------");
        }
        fSearchView = (SearchView) findViewById(R.id.srchViewSearchContacts);

        fSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                fUserAdapter.filter((newText));
                return false;
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
