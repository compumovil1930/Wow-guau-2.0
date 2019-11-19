package co.edu.javeriana.wow_guau.views;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import org.w3c.dom.Document;

import java.util.Map;

import co.edu.javeriana.wow_guau.R;
import co.edu.javeriana.wow_guau.adapters.PaseoClassAdapter;


public class paseoMapaActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    PaseoClassAdapter mPaseo;
    Marker paseadorMarker;
    Button btnChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paseo_mapa);
        mPaseo = (PaseoClassAdapter) this.getIntent().getSerializableExtra("paseo");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        db.collection("Paseadores").document(mPaseo.getUidPaseador()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(documentSnapshot != null){
                    Map<String,Object> data = documentSnapshot.getData();

                    Map<String,Object> locData = (Map<String,Object>) data.get("localizacion");
                    LatLng posicion = new LatLng( (double) locData.get("latitud"),(double) locData.get("longitud"));

                    if(paseadorMarker == null){
                        paseadorMarker = mMap.addMarker(new MarkerOptions().position(posicion).title("Paseador").icon(BitmapDescriptorFactory
                                .fromResource(R.drawable.walking_dog)));

                    } else {
                        paseadorMarker.setPosition(posicion);
                    }

                }

            }
        });

        btnChat = findViewById(R.id.button_chat);

        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),ChatActivity.class);
                i.putExtra("uidPaseo",mPaseo.getUidPaseo());
                startActivity(i);

            }
        });

        Log.i("Paseo",mPaseo.getUidPaseo());
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
        if(mPaseo == null){
            mPaseo = (PaseoClassAdapter) this.getIntent().getSerializableExtra("paseo");
        }

        //Pintar posicion de recogida y cómo se mueve el paseador
        db.collection("Paseos").document(mPaseo.getUidPaseo()).get().addOnSuccessListener(
                new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Map<String,Object> direccion = (Map<String,Object>) documentSnapshot.getData().get("direccion");
                        LatLng recogida = new LatLng((double) direccion.get("latitud"),(double) direccion.get("longitud"));
                        mMap.addMarker(new MarkerOptions().position(recogida).title("Punto de encuentro"));
                        mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(recogida));

                    }
                }
        );

    }
}
