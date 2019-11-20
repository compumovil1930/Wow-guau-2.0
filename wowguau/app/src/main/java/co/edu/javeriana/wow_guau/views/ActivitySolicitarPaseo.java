package co.edu.javeriana.wow_guau.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.edu.javeriana.wow_guau.R;
import co.edu.javeriana.wow_guau.model.Paseo;

public class ActivitySolicitarPaseo extends AppCompatActivity {

    EditText editTextMonedas;
    EditText editTextDuracion;
    Button buttonDesdeAca;
    Button buttonDesdeCasa;
    TextView textViewDesdeDonde;
    Button buttonSolicitarPaseo;

    String uidPerro;
    String nombrePerro, fotoPerro;

    private GeoPoint ubicacionActual;
    private String direccionUbicacionActual;

    FirebaseFirestore db;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 4;
    private static final int REQUEST_CHECK_SETTINGS = 5;

    String[] PERMISSIONS = {
            android.Manifest.permission.ACCESS_FINE_LOCATION
    };

    private static final String TAG = "ActivityRegistro";

    private String direccionCliente = "";
    private double latitudCliente = 0;
    private double longitudCliente = 0;

    private String donde;

    Geocoder geocoder;
    List<Address> addresses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitar_paseo);

        editTextMonedas = findViewById(R.id.et_monedas_ofrecidas);
        editTextDuracion = findViewById(R.id.et_duracion_solicitud);
        buttonDesdeAca = findViewById(R.id.btn_desde_aca);
        buttonDesdeCasa = findViewById(R.id.btn_desde_direccion);
        textViewDesdeDonde = findViewById(R.id.tv_desde_donde);
        buttonSolicitarPaseo = findViewById(R.id.btn_solicitar_paseo);

        uidPerro = getIntent().getStringExtra("idPerro");
        nombrePerro = getIntent().getStringExtra("nombrePerro");
        fotoPerro = getIntent().getStringExtra("fotoPerro");

        geocoder = new Geocoder(getBaseContext());

        //Log.i("Nombre perro:", nombrePerro);
        //Log.i("Foto perro:", fotoPerro);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        donde = "";

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mLocationRequest = createLocationRequest();

        ubicacionActual = new GeoPoint(0,0);

        //editTextMonedas.setText( String.valueOf(45));

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                final Location location = locationResult.getLastLocation();
                Log.i("LOCATION", "Location update in the callback: " + location);
                if (location != null) {
                    ubicacionActual = new GeoPoint( location.getLatitude(), location.getLongitude() );
                    textViewDesdeDonde.setText("Encontrada ubicación actual");
                    try {
                        addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        direccionUbicacionActual = addresses.get(0).getAddressLine(0);
                    } catch (IOException e) {
                        direccionUbicacionActual = "No disponible";
                        e.printStackTrace();
                    }
                    stopLocationUpdates();
                }
            }
        };

        buttonSolicitarPaseo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( verificarFormulario() ){
                    registrarPaseo();
                }
            }
        });

        buttonDesdeAca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                donde = "aca";
                if (ContextCompat.checkSelfPermission(ActivitySolicitarPaseo.this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    setLocationOn();
                }else{
                    requestPermission(ActivitySolicitarPaseo.this, PERMISSIONS[0], "Acceso a GPS necesario",
                            MY_PERMISSIONS_REQUEST_LOCATION);
                }
            }
        });

        buttonDesdeCasa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                donde = "casa";
                buscarDireccionCliente();
            }
        });

        editTextDuracion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String duracionText = editTextDuracion.getText().toString();
                if (!duracionText.isEmpty()) {
                    calcularCostoPaseo();
                } else {
                    Toast.makeText(ActivitySolicitarPaseo.this, "No se ha ingresado " +
                            "duración", Toast.LENGTH_SHORT).show();
                    editTextMonedas.setText("");
                }
            }
        });

    }

    private void calcularCostoPaseo(){
        int costo;
        int minutos = Integer.valueOf(editTextDuracion.getText().toString());
        costo = minutos*15;
        editTextMonedas.setText( String.valueOf(costo) );
    }

    private boolean verificarFormulario(){
        boolean b = true;
        if(editTextDuracion.getText().toString().isEmpty()){
            b = false;
            editTextDuracion.setError("Campo obligatorio");
        }else{
            editTextDuracion.setError(null);
        }
        if(editTextMonedas.getText().toString().isEmpty()){
            b = false;
            Toast.makeText(this,"Debes ingresar duración para calcular precio", Toast.LENGTH_SHORT).show();
        }
        if(donde.equals("")){
            b = false;
            Toast.makeText(this,"Debes seleccionar una ubicación", Toast.LENGTH_SHORT).show();
        }else{
            if(donde.equals("aca")){
                if (ubicacionActual.getLongitude() == 0 && ubicacionActual.getLatitude() ==0){
                    b = false;
                    Toast.makeText(this,"No se ha encontrado la ubicación actual", Toast.LENGTH_SHORT).show();
                }
            }else{
                if(direccionCliente.equals("")){
                    b = false;
                    textViewDesdeDonde.setText("No tienes registrada la dirección de tu casa");
                }
            }
        }
        return b;
    }

    private void registrarPaseo(){

        Paseo paseo = new Paseo();
        Map<String, Object> direccion = new HashMap<>();

        if(donde.equals("aca")){
            direccion.put("direccion", direccionUbicacionActual);
            direccion.put("latitud", ubicacionActual.getLatitude());
            direccion.put("longitud", ubicacionActual.getLongitude());
        }else{
            direccion.put("direccion", direccionCliente);
            direccion.put("latitud", latitudCliente);
            direccion.put("longitud", longitudCliente);
        }
        paseo.setAceptado(false);
        paseo.setCalificacion(-1);
        paseo.setCalificado(false);
        paseo.setComentarioCalificacion("");

        paseo.setDireccion(direccion);
        paseo.setCosto( Long.valueOf(editTextMonedas.getText().toString()) );
        paseo.setDuracion( Long.valueOf( editTextDuracion.getText().toString() ) );
        paseo.setEstado(true);
        paseo.setUidPerro( uidPerro );
        paseo.setNomPerro( nombrePerro );
        paseo.setUriPerro( fotoPerro );
        paseo.setUidPaseador("");
        paseo.setUidDueno( currentUser.getUid() );
        Date dateObj = Calendar.getInstance().getTime();
        paseo.setFecha(dateObj);
        paseo.setDistanciaRecorrida(0);

        DocumentReference referencia = db.collection("Paseos").document();

        referencia
                .set(paseo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        Toast.makeText(ActivitySolicitarPaseo.this, "Paseo solicitado con éxito",
                                Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                        Toast.makeText(ActivitySolicitarPaseo.this, "Falló la solicitud de paseo",
                                Toast.LENGTH_SHORT).show();
                        //btn_hacer_solicitud.setEnabled(true);
                    }
                });

    }

    private void buscarDireccionCliente(){
        db.collection("Clientes").document(currentUser.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                direccionCliente = document.getString("direccion");
                                GeoPoint ubicacion = document.getGeoPoint("ubicacion");
                                latitudCliente = ubicacion.getLatitude();
                                longitudCliente = ubicacion.getLongitude();
                                if(direccionCliente.equals("")){
                                    textViewDesdeDonde.setText("No tienes registrada la dirección de tu casa");
                                }else{
                                    textViewDesdeDonde.setText("Seleccionada la dirección de tu casa");
                                }
                            }
                        }
                    }
                });
    }

    protected LocationRequest createLocationRequest() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000); //tasa de refresco en milisegundos
        mLocationRequest.setFastestInterval(1000); //máxima tasa de refresco
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return mLocationRequest;
    }

    private void setLocationOn(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            LocationSettingsRequest.Builder builder = new
                    LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
            SettingsClient client = LocationServices.getSettingsClient(this);
            Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

            task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
                @Override
                public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                    startLocationUpdates(); //Todas las condiciones para recibir localizaciones
                }
            });

            task.addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    int statusCode = ((ApiException) e).getStatusCode();
                    switch (statusCode) {
                        case CommonStatusCodes.RESOLUTION_REQUIRED:
                            // Location settings are not satisfied, but this can be fixed by showing the user a dialog.
                            try {// Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                ResolvableApiException resolvable = (ResolvableApiException) e;
                                resolvable.startResolutionForResult(ActivitySolicitarPaseo.this,
                                        REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException sendEx) {
                                // Ignore the error.
                            } break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // Location settings are not satisfied. No way to fix the settings so we won't show the dialog.
                            break;
                    }
                }
            });

        }else{
            Toast.makeText(this,
                    "Sin acceso a localización, permiso denegado!",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void startLocationUpdates() {
        //Verificación de permiso!!
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
        }
    }

    private void stopLocationUpdates(){
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS: {
                if (resultCode == RESULT_OK) {
                    startLocationUpdates();
                    Toast.makeText(this, "Con acceso a localización", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Sin acceso a localización", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        switch (requestCode)
        {

            case MY_PERMISSIONS_REQUEST_LOCATION:
            {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, continue with task related to permission
                    setLocationOn();
                    //Intent i = new Intent(MapsActivity.this, MapsActivity.class);
                    //startActivityForResult(i, Permisos.ADDRESS_PICKER);
                }else{
                    //button_register.setEnabled(false);
                    Toast.makeText(this,
                            "Sin acceso a localización, permiso denegado!",
                            Toast.LENGTH_LONG).show();
                }
            }
            break;
        }
    }

    private void requestPermission(Activity context, String permiso, String justificacion, int idCode){
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(context, permiso) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            if (ActivityCompat.shouldShowRequestPermissionRationale(context, permiso)) {
                Toast toast = Toast.makeText(context, justificacion, Toast.LENGTH_SHORT);
                toast.show();
            }
            //Request the permission
            ActivityCompat.requestPermissions(context, new String[]{permiso}, idCode);
        }
    }
}
