package inf8405.tp2.letsmeet;


// INF8405 - Laboratoire 2
//Auteurs : Najib Arbaoui (1608366) && Youssef Zemmahi (1665843) && Zolnouri Mahdi (1593999)

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import com.google.android.gms.location.LocationListener;

import android.os.BatteryManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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

import java.io.IOException;
import java.util.ArrayList;
import android.os.Handler;
import android.os.Message;

public class IConfirmationPreferences extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, AdapterView.OnItemSelectedListener {

    private GoogleApiClient fGoogleApiClient;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private LocationRequest fLocationRequest;
    public static final String TAG = UserList.class.getSimpleName();
    private GoogleMap fMap;
    TextView lieuRencontreMsg = null;
    TextView lieuRencontre = null;
    TextView DateRencontreMsg = null;
    TextView DateRencontre = null;
    private Button btnRevenirMP = null;
    private double addreToLatitude;
    private double addreToLongitude;
    private String lieu = "2500, chemin de Polytechnique, Montreal, Canada";
    private String latLongFromAdd = "";
    double currentLatitude = 0.0;
    double currentLongitude = 0.0;


    /* Pop up */
    ContentResolver fResolver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation_rencontre);


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

        fResolver = this.getContentResolver();
        lieuRencontreMsg = (TextView) findViewById(R.id.txtVwLieuMsgCR);
        lieuRencontre = (TextView) findViewById(R.id.txtVwLieuCR);
        DateRencontreMsg = (TextView) findViewById(R.id.txtVwDateRencontreMsgCR);
        DateRencontre = (TextView) findViewById(R.id.txtVwDateRencontreCR);
        RencontreConfirme rencontreConfirme = DBContent.getInstance().recupereResultatVoteRencontre(DBContent.getInstance().getActualGroupId());
        if(rencontreConfirme!=null)
        {
            lieu = rencontreConfirme.getLieu();
            String d = rencontreConfirme.getDate();
            lieuRencontre.setText(lieu);
            DateRencontre.setText(d);
        }else{
            lieuRencontre.setText("Pas encore confirmée pour ce groupe!");
            DateRencontre.setText("Pas encore confirmée pour ce groupe!");
        }
        btnRevenirMP = (Button) findViewById(R.id.btnAnnulerP);

        btnRevenirMP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                //startActivity(intent);
                finish();
            }
        });
        TextView niveauBattrie = (TextView)findViewById(R.id.txtNiveauBattery);
        niveauBattrie.setText(String.valueOf(getBatteryLevel()) + "%");
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
// Method pour Calculer le niveau de batterie
    public float getBatteryLevel() {
        Intent batteryIntent = registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        // Error checking that probably isn't needed but I added just in case.
        if(level == -1 || scale == -1) {
            return 50.0f;
        }

        return ((float)level / (float)scale) * 100.0f;
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
        LatLng sydney = new LatLng(-73.628182,45.502481);
        fMap.addMarker(new MarkerOptions().position(sydney).title("Location"));
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
        //currentLatitude = location.getLatitude();
        //currentLongitude = location.getLongitude();


        /* get lon et lat */
        GeocodingLocation locationAddress = new GeocodingLocation();
        locationAddress.getAddressFromLocation(lieu, getApplicationContext(), new GeocoderHandler());

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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub
    }
    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    break;
                default:
                    locationAddress = null;
            }
            latLongFromAdd = locationAddress;
            String[] str = latLongFromAdd.split(";");
            if(str.length > 0) {
                currentLatitude = Double.valueOf(str[0]);
                currentLongitude = Double.valueOf(str[1]);
                LatLng latLng = new LatLng(currentLatitude, currentLongitude);
                MarkerOptions options = new MarkerOptions().position(latLng).title("Actual location");
                fMap.addMarker(options);
                float zoomLevel = 16; //This goes up to 21
                fMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
            }else{
                LatLng latLng = new LatLng(currentLatitude, currentLongitude);
                MarkerOptions options = new MarkerOptions().position(latLng).title("Actual location");
                fMap.addMarker(options);
                float zoomLevel = 16; //This goes up to 21
                fMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
            }

        }
    }


}
