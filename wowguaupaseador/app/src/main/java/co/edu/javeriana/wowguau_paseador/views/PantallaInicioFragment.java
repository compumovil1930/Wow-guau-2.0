package co.edu.javeriana.wowguau_paseador.views;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import co.edu.javeriana.wowguau_paseador.R;
import co.edu.javeriana.wowguau_paseador.adapters.PaseoAdapter;
import co.edu.javeriana.wowguau_paseador.model.Paseador;
import co.edu.javeriana.wowguau_paseador.model.Paseo;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class PantallaInicioFragment extends Fragment {

    private final int MY_PERMISSIONS_REQUEST_LOCATION = 0;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;

    ListView mList;
    Cursor mCursor;
    String[] mProjection;
    PaseoAdapter mPaseoAdapter;


    TextView tv_bienvenido;
    Button btn_estado;

    Paseador paseador;

    private FirebaseAuth mAuth;

    FirebaseUser mFireUser = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    protected LocationRequest createLocationRequest() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(3000); //tasa de refresco en milisegundos
        mLocationRequest.setFastestInterval(3000); //máxima tasa de refresco
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return mLocationRequest;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(this.paseador!=null && this.paseador.isEstado())
            startLocationUpdates();
    }

    @Override
    public void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    private void stopLocationUpdates(){
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    private void startLocationUpdates() {
        //Verificación de permiso!!
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
        }
    }

    public PantallaInicioFragment() {
    }



    private void pedirPermiso(){
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)){
                Toast.makeText(getContext(),"La aplicación necesita permisos", Toast.LENGTH_LONG).show();
            }
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
        } else {
            startLocationUpdates();
            // permission granted, so...
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance();
        paseador = (Paseador) getActivity().getIntent().getSerializableExtra("user");

        final View view = inflater.inflate(R.layout.fragment_pantalla_inicio, container, false);
        ((MenuActivity) getActivity()).getSupportActionBar().setTitle("Paseador");



        tv_bienvenido = view.findViewById(R.id.tv_bienvenido);
        btn_estado = view.findViewById(R.id.btn_estado);

        if(paseador.isEstado()){
            btn_estado.setBackgroundColor(Color.parseColor("#DC143C"));
            btn_estado.setText("Dejar de Trabajar");
            btn_estado.setTextColor(Color.WHITE);
        } else {
            btn_estado.setBackgroundColor(Color.parseColor("#14C967"));
            btn_estado.setText("Comenzar a Trabajar");
        }

        mList = (ListView) view.findViewById(R.id.listaPaseos);

        db.collection("Paseos")
                .whereEqualTo("uidPaseador",mAuth.getUid()).whereEqualTo("estado",true).addSnapshotListener(new EventListener<QuerySnapshot>() {
            ArrayList<Paseo> lPaseos = new ArrayList<>();

            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("error", "listen:error", e);
                    return;
                }

                for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                    switch (dc.getType()) {
                        case ADDED:
                            Map<String,Object> vals = dc.getDocument().getData();
                            Paseo my_paseo = new Paseo((String) vals.get("uidPerro"),
                                    (String) vals.get("uidPaseador"),(long) vals.get("duracion"), (long) vals.get("costo"),
                                    (String)((Map<String, Object>) vals.get("direccion")).get("direccion"),
                                    (Double)((Map<String, Object>) vals.get("direccion")).get("latitud"),
                                    (Double) ((Map<String, Object>) vals.get("direccion")).get("longitud"),
                                    (Boolean) vals.get("estado"),
                                    (String) vals.get("nomPerro"),
                                    (String) vals.get("uriPerro"));
                            lPaseos.add(my_paseo);
                            Log.d(TAG, "Add city: " + dc.getDocument().getData());
                            break;
                        case MODIFIED:
                            Log.d(TAG, "Modified city: " + dc.getDocument().getData());
                            break;
                        case REMOVED:
                            Log.d(TAG, "Removed city: " + dc.getDocument().getData());
                            break;
                    }
                }

                PaseoAdapter paseoAdapter  = new PaseoAdapter(getContext(),lPaseos);
                mList.setAdapter(paseoAdapter);
            }
        })
            ;


        tv_bienvenido.append(" "+paseador.getNombre());

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        mLocationRequest = createLocationRequest();

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                Location location = locationResult.getLastLocation();
                Log.i("LOCATION", "Location update in the callback: " + location);
                if (location != null) {
                    Map<String, Object> up = new HashMap<>();
                    Map<String, Object> myloc = new HashMap<>();
                    myloc.put("latitud",location.getLatitude());
                    myloc.put("longitud",location.getLongitude());
                    up.put("localizacion",  myloc);
                    db.collection("Paseadores").document(mFireUser.getUid()).update(up);
                }
            }
        };

        btn_estado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paseador.setEstado(!paseador.isEstado());

                if(paseador.isEstado()){
                    pedirPermiso();
                        btn_estado.setBackgroundColor(Color.parseColor("#DC143C"));
                        btn_estado.setText("Dejar de Trabajar");
                        btn_estado.setTextColor(Color.WHITE);
                } else {
                    stopLocationUpdates();
                    btn_estado.setBackgroundColor(Color.parseColor("#14C967"));
                    btn_estado.setText("Comenzar a Trabajar");
                }
                Map<String, Object> up = new HashMap<>();
                up.put("estado",paseador.isEstado() );
                db.collection("Paseadores").document(mFireUser.getUid()).update(up);

                ((MenuActivity) getActivity()).setPaseador(paseador);
                //Log.i("INFO", "OPRIMI");
            }
        });

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    startLocationUpdates();
                } else {
                    Toast.makeText(getContext(),"La aplicación necesita permisos", Toast.LENGTH_LONG).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }
}
