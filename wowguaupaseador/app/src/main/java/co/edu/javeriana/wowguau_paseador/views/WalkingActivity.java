package co.edu.javeriana.wowguau_paseador.views;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;

import co.edu.javeriana.wowguau_paseador.R;
import co.edu.javeriana.wowguau_paseador.model.Paseo;
import co.edu.javeriana.wowguau_paseador.model.Perro;
import co.edu.javeriana.wowguau_paseador.utils.Utils;

public class WalkingActivity extends FragmentActivity implements OnMapReadyCallback {
    TextView tv_tiempo;
    private GoogleMap mMap;

    Perro perro;
    Paseo paseo;
    String uidPaseo;

    private FirebaseAuth mAuth;
    FirebaseUser mFireUser = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walking);

        mAuth = FirebaseAuth.getInstance();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        tv_tiempo = findViewById(R.id.tv_tiempo);


        perro = (Perro) getIntent().getSerializableExtra("perro");
        uidPaseo = getIntent().getStringExtra("uidPaseo");

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

    }

    private void updateUI() {
        timer(paseo.getDuracionMinutos()*60000);
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

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
    public void timer(long end){
        new CountDownTimer(end, 1000) {
            public void onTick(long millisUntilFinished) {
                tv_tiempo.setText(Utils.longToString(millisUntilFinished));
            }
            public void onFinish() {
                tv_tiempo.setText("done!");
            }
        }.start();
    }
}
