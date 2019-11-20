package co.edu.javeriana.wowguau_paseador.views;


import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import co.edu.javeriana.wowguau_paseador.R;
import co.edu.javeriana.wowguau_paseador.adapters.PaseoAdapter;
import co.edu.javeriana.wowguau_paseador.model.Paseador;
import co.edu.javeriana.wowguau_paseador.model.Paseo;
import co.edu.javeriana.wowguau_paseador.utils.FirebaseUtils;
import co.edu.javeriana.wowguau_paseador.utils.Permisos;

import static androidx.constraintlayout.widget.Constraints.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistorialFragment extends Fragment {
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;

    ListView mList;
    Cursor mCursor;
    String[] mProjection;

    Paseador paseador;

    ArrayList<Paseo> lPaseos = new ArrayList<>();
    PaseoAdapter paseoAdapter ;

    LatLng mLoc;

    private FirebaseAuth mAuth;
    FirebaseUser mFireUser = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    protected LocationRequest createLocationRequest() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(6000); //tasa de refresco en milisegundos
        mLocationRequest.setFastestInterval(6000); //máxima tasa de refresco
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return mLocationRequest;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(this.paseador!=null){
            mList.setVisibility(View.VISIBLE);
            startLocationUpdates();
        }
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

    public HistorialFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        ((MenuActivity)getActivity()).setFragmentRefreshListener(new MenuActivity.FragmentRefreshListener() {
            @Override
            public void onRefresh() {
                paseador = ((MenuActivity)getActivity()).getPaseador();
            }
        });
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_historial, container, false);
        ((MenuActivity) getActivity()).getSupportActionBar().setTitle("Paseador");

        paseoAdapter = new PaseoAdapter(getContext(),lPaseos);
        mList = (ListView) view.findViewById(R.id.listaPaseos);
        mList.setAdapter(paseoAdapter);

        if(paseador!=null){
            mList.setVisibility(View.VISIBLE);
        }else {
            mList.setVisibility(View.INVISIBLE);
        }
        db.collection("Paseos")
                .whereEqualTo("uidPaseador", mAuth.getUid()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            private Paseo newPaseo(Map<String, Object> vals){
                Paseo my_paseo = new Paseo((String) vals.get("uidPerro"),
                        "", (String) vals.get("uidDueno"),
                        (long) vals.get("duracion"), (long) vals.get("costo"),
                        (String)((Map<String, Object>) vals.get("direccion")).get("direccion"),
                        (Double)((Map<String, Object>) vals.get("direccion")).get("latitud"),
                        (Double) ((Map<String, Object>) vals.get("direccion")).get("longitud"),
                        (Boolean) vals.get("estado"),
                        (String) vals.get("nomPerro"),
                        (String) vals.get("uriPerro"));
                return my_paseo;
            }


            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("error", "listen:error", e);
                    return;
                }

                for (  DocumentChange dc: queryDocumentSnapshots.getDocumentChanges() ) {
                    switch (dc.getType()){

                        case ADDED: {
                            Map<String,Object> vals = dc.getDocument().getData();
                            Paseo my_paseo = newPaseo(vals);
                            my_paseo.setPaseadorLoc(mLoc);
                            paseoAdapter.add(my_paseo);

                            Log.d(TAG, "Add: " + dc.getDocument().getData());
                            break;
                        }

                        case REMOVED: {
                            Map<String,Object> vals = dc.getDocument().getData();
                            Paseo my_paseo = newPaseo(vals);

                            for( int i=0; i<paseoAdapter.getCount();++i){
                                Paseo temp = paseoAdapter.getItem(i);
                                if(temp.getUidPerro().equals(my_paseo.getUidPerro())){
                                    paseoAdapter.remove(temp);
                                    break;
                                }
                            }

                            break;
                        }
                        case MODIFIED: {
                            Map<String,Object> vals = dc.getDocument().getData();
                            Paseo my_paseo = newPaseo(vals);

                            for( int i=0; i<paseoAdapter.getCount();++i){
                                Paseo temp = paseoAdapter.getItem(i);
                                if(temp.getUidPerro().equals(my_paseo.getUidPerro())){
                                    paseoAdapter.remove(temp);
                                    break;
                                }
                            }
                            paseoAdapter.add(my_paseo);
                            break;
                        }
                    }
                }
                paseoAdapter.sort(new Comparator<Paseo>() {
                    @Override
                    public int compare(Paseo lhs, Paseo rhs) {
                        Double l = new Double(lhs.getDist());
                        Double r = new Double(rhs.getDist());
                        return l.compareTo(r);   //or whatever your sorting algorithm
                    }
                });

            }
        });

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        mLocationRequest = createLocationRequest();

        mLocationCallback = new LocationCallback() {
            private boolean i=false;
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
                    mLoc = new LatLng(location.getLatitude(), location.getLongitude());
                    db.collection("Paseadores").document(mFireUser.getUid()).update(up);

                    for(int i=0;i<mList.getChildCount();++i){
                        Paseo p = paseoAdapter.getItem(i);
                        p.setPaseadorLoc(mLoc);
                        p.calcDist();
                        View v = mList.getChildAt(i);
                        TextView tempTv = v.findViewById(R.id.tv_dist_paseo);
                        tempTv.setText(p.getDist() + " km");
                        v.setVisibility(View.VISIBLE);
                    }

                    paseoAdapter.sort(new Comparator<Paseo>() {
                        @Override
                        public int compare(Paseo lhs, Paseo rhs) {
                            Double l = new Double(lhs.getDist());
                            Double r = new Double(rhs.getDist());
                            return l.compareTo(r);   //or whatever your sorting algorithm
                        }
                    });

                }
            }
        };

        // bla bla
        return view;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Permisos.MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startLocationUpdates();
                } else {
                    Toast.makeText(getContext(),"La aplicación necesita permisos", Toast.LENGTH_LONG).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }

}
