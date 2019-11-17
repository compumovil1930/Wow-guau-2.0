package co.edu.javeriana.wowguau_paseador.views;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.model.value.IntegerValue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import co.edu.javeriana.wowguau_paseador.R;
import co.edu.javeriana.wowguau_paseador.model.Paseo;
import co.edu.javeriana.wowguau_paseador.model.Perro;
import co.edu.javeriana.wowguau_paseador.utils.Permisos;
import co.edu.javeriana.wowguau_paseador.utils.Utils;

public class WalkToDogActivity extends FragmentActivity implements OnMapReadyCallback {
    Button btn_llegue;
    Button btn_comenzar;

    Perro perro;
    Paseo paseo;
    String uidPaseo;

    Marker paseador;
    Marker dog;

    private GoogleMap mMap;
    private Geocoder mGeocoder;
    private Location myCurrentLocation;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    private Polyline mLine;

    private FirebaseAuth mAuth;
    FirebaseUser mFireUser = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walk_to_dog);
        mAuth = FirebaseAuth.getInstance();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btn_llegue = findViewById(R.id.btn_llegue);
        btn_comenzar = findViewById(R.id.btn_comenzar);
        myCurrentLocation = new Location("");
        mGeocoder = new Geocoder(getBaseContext());
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mLocationRequest = createLocationRequest();
        perro = (Perro) getIntent().getSerializableExtra("perro");
        uidPaseo = getIntent().getStringExtra("uidPaseo");

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                Location location = locationResult.getLastLocation();
                Log.i("LOCATION", "Location update in the callback: " + location);
                if (location != null && paseador!=null) {
                    if(paseo!=null) {
                        consumeRESTVolley();
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(Utils.midPoint(paseador.getPosition(), dog.getPosition())));
                        CameraUpdateFactory.zoomBy(0.5f);
                    }
                    paseador.setPosition(new LatLng(location.getLatitude(), location.getLongitude()));
                    paseador.setVisible(true);
                }
            }
        };

        db.collection("Paseos").document(uidPaseo).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        paseo = new Paseo((String) documentSnapshot.getData().get("uidPerro"),
                                (String) documentSnapshot.getData().get("uidPaseador"),
                                (long) documentSnapshot.getData().get("duracion"),
                                (long) documentSnapshot.getData().get("costo"),
                                (String)((Map<String, Object>) documentSnapshot.getData().get("direccion")).get("direccion"),
                                (Double)((Map<String, Object>) documentSnapshot.getData().get("direccion")).get("latitud"),
                                (Double) ((Map<String, Object>) documentSnapshot.getData().get("direccion")).get("longitud"),
                                (Boolean) documentSnapshot.getData().get("estado"),
                                (String) documentSnapshot.getData().get("nomPerro"),
                                (String) documentSnapshot.getData().get("uriPerro"));
                        updateUI();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("WALK", "Error getting documents");
                    }
                });

        Permisos.requestPermission(WalkToDogActivity.this, Manifest.permission.ACCESS_FINE_LOCATION, "I need to read the location because I want to spy you", Permisos.MY_PERMISSIONS_REQUEST_LOCATION);
        if (ContextCompat.checkSelfPermission(WalkToDogActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            setLocationOn();
            startLocationUpdates();
        }else{
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)){
                Toast.makeText(this,"La aplicación necesita permisos", Toast.LENGTH_LONG).show();
            }
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    Permisos.MY_PERMISSIONS_REQUEST_LOCATION);
        }

        mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    myCurrentLocation = location;
                }
            }
        });

        btn_llegue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO enviar notificación
                btn_llegue.setVisibility(View.INVISIBLE);
                btn_comenzar.setVisibility(View.VISIBLE);
            }
        });
        btn_comenzar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO enviar notificación

            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        startLocationUpdates();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }
    private void stopLocationUpdates(){
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    private void startLocationUpdates() {
        //Verificación de permiso!!
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
        }
    }
    private void updateUI() {
        LatLng dogLocation = new LatLng(paseo.getLatitude(), paseo.getLongitude());
        dog = mMap.addMarker(new MarkerOptions().position(dogLocation)
                .title(perro.getNombre())
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.dog_png)));
        consumeRESTVolley();
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
        mMap = googleMap;
        LatLng myLocation = new LatLng(myCurrentLocation.getLatitude(), myCurrentLocation.getLongitude());
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        paseador = mMap.addMarker(new MarkerOptions().position(myLocation)
                .icon(BitmapDescriptorFactory.fromBitmap(Utils.getBitmap(getDrawable(R.drawable.ic_walking_dog)))));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(17));
    }
    protected LocationRequest createLocationRequest() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000); //tasa de refresco en milisegundos
        mLocationRequest.setFastestInterval(500); //máxima tasa de refresco
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return mLocationRequest;
    }
    private void setLocationOn(){
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                int statusCode = ((ApiException) e).getStatusCode();
                switch (statusCode) {
                    case CommonStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied, but this can be fixed by showing the user a dialog.
                        try {// Show the dialog by calling startResolutionForResult(), and check the result in onActivityResult().
                            ResolvableApiException resolvable = (ResolvableApiException) e;
                            resolvable.startResolutionForResult(WalkToDogActivity.this, Permisos.REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException sendEx) {
                            // Ignore the error.
                        } break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. No way to fix the settings so we won't show the dialog.
                        break;
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Permisos.REQUEST_CHECK_SETTINGS: {
                if (resultCode == RESULT_OK) {
                    Toast.makeText(this, "Con acceso a localización", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Sin acceso a localización", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }
    public void consumeRESTVolley(){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://maps.googleapis.com/maps/api/directions/json?";
        String origin = "origin="+paseador.getPosition().latitude+","+paseador.getPosition().longitude;
        String destination = "destination="+paseo.getLatitude()+","+paseo.getLongitude();
        String mode = "mode=walking";
        String key = "key="+getResources().getString(R.string.google_api_key);
        StringRequest req = new StringRequest(Request.Method.GET, url+origin+"&"+destination+"&"+mode+"&"+key,
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        String data = (String)response;
                        parseJSON(data);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("TAG", "Error handling rest invocation"+error.getCause());
                    } }
        );
        queue.add(req);
    }

    private void parseJSON(String data) {
        ArrayList<LatLng> result = new ArrayList<>();
        String distance="";
        Double d=0.0;
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray steps = jsonObject.getJSONArray("routes");
            steps = steps.getJSONObject(0).getJSONArray("legs");
            d = steps.getJSONObject(0).getJSONObject("distance").getDouble("value");
            steps = steps.getJSONObject(0).getJSONArray("steps");

            result.add(new LatLng(((JSONObject)((JSONObject)steps.get(0)).get("start_location")).getDouble("lat"), ((JSONObject)((JSONObject)steps.get(0)).get("start_location")).getDouble("lng")));
            for(int i=0;i<steps.length();++i) {
                JSONObject punto = steps.getJSONObject(i);
                result.add(new LatLng(((JSONObject)punto.get("end_location")).getDouble("lat"), ((JSONObject)punto.get("end_location")).getDouble("lng")));
                Log.i("LATLNG", result.get(i).toString());
            }

            distance = "La distancia es: " + d/1000.0 + " Km a su objetivo";
            //mMap.moveCamera(CameraUpdateFactory.zoomTo((int)(15*d)));

            //Toast.makeText(getApplicationContext(),  distance ,Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        drawRoute(result);
    }

    private void drawRoute(ArrayList<LatLng> result) {
        if(mLine!=null)
            mLine.remove();
        PolylineOptions line = new PolylineOptions();
        line.addAll(result);
        line.width(10);
        line.color(Color.BLUE);
        line.jointType(JointType.ROUND);
        mLine = mMap.addPolyline(line);
    }

}
