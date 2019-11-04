package co.edu.javeriana.wow_guau.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
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

import co.edu.javeriana.wow_guau.R;
import co.edu.javeriana.wow_guau.adapters.PaseadorAdapter;
import co.edu.javeriana.wow_guau.adapters.PaseadorClassAdapter;
import co.edu.javeriana.wow_guau.adapters.PaseadorSerializable;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class ActividadBusquedaPaseadores extends AppCompatActivity {

    private final int MY_PERMISSIONS_REQUEST_LOCATION = 0;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;

    ListView mList;
    ProgressBar progressBarLista;

    ArrayList<PaseadorClassAdapter> mPaseadores = new ArrayList<>();
    PaseadorAdapter mPaseadorAdapter;

    FirebaseUser mFireUser = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    LatLng mLoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_busqueda_paseadores);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mLocationRequest = createLocationRequest();

        mList = findViewById(R.id.rvListaPaseadores);
        progressBarLista = findViewById(R.id.pbBusquedaPasedores);

        mPaseadorAdapter = new PaseadorAdapter(this, mPaseadores);
        mList.setAdapter(mPaseadorAdapter);
        progressBarLista.setVisibility(View.INVISIBLE);
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
                    mLoc = new LatLng(location.getLatitude(), location.getLongitude());
                    db.collection("Paseadores").document(mFireUser.getUid()).update(up);


                    for(int i=0;i<mList.getChildCount();++i){
                        PaseadorClassAdapter p = mPaseadorAdapter.getItem(i);

                        p.setUsuarioLocalizacion(mLoc);
                        p.calcDist();

                        View v = mList.getChildAt(i);
                        if(p.getDist()<=5.0 && p.getDist() != -1.0){
                            TextView tempTv = v.findViewById(R.id.tv_item_paseador_distancia);
                            tempTv.setText(p.getDist() + " km");
                            v.setVisibility(View.VISIBLE);
                        } else {
                            v.setVisibility(View.INVISIBLE);
                        }
                    }
                    mPaseadorAdapter.sort(new Comparator<PaseadorClassAdapter>() {
                        @Override
                        public int compare(PaseadorClassAdapter lhs, PaseadorClassAdapter rhs) {
                            Double l = new Double(lhs.getDist());
                            Double r = new Double(rhs.getDist());
                            return l.compareTo(r);   //or whatever your sorting algorithm
                        }
                    });

                }
            }
        };

        db.collection("Paseadores")
                .whereEqualTo("estado",true).addSnapshotListener(new EventListener<QuerySnapshot>() {

            private PaseadorClassAdapter newPaseador(Map<String, Object> vals){
                PaseadorClassAdapter my_paseador = new PaseadorClassAdapter(
                        (String) vals.get("nombre"),
                        (String) vals.get("uidPaseador"),
                        new LatLng(
                        (Double)((Map<String, Object>) vals.get("localizacion")).get("latitud"),
                        (Double) ((Map<String, Object>) vals.get("localizacion")).get("longitud")
                        ),
                        (String) vals.get("direccionFoto"));
                return my_paseador;
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
                            vals.put("uidPaseador",dc.getDocument().getId());

                            PaseadorClassAdapter my_paseador = newPaseador(vals);
                            my_paseador.setUsuarioLocalizacion(mLoc);
                            my_paseador.calcDist();
                            mPaseadorAdapter.add(my_paseador);

                            Log.d(TAG, "Add: " + dc.getDocument().getData());
                            break;
                        }

                        case REMOVED: {
                            Map<String,Object> vals = dc.getDocument().getData();
                            vals.put("uidPaseador",dc.getDocument().getId());

                            PaseadorClassAdapter my_paseador = newPaseador(vals);

                            for( int i=0; i<mPaseadorAdapter.getCount();++i){
                                PaseadorClassAdapter temp = mPaseadorAdapter.getItem(i);
                                if(temp.getUidPaseador().equals(my_paseador.getUidPaseador())){
                                    mPaseadorAdapter.remove(temp);
                                    break;
                                }
                            }
                            break;
                        }

                        case MODIFIED: {
                            Map<String,Object> vals = dc.getDocument().getData();
                            vals.put("uidPaseador",dc.getDocument().getId());

                            PaseadorClassAdapter my_paseador = newPaseador(vals);

                            for( int i=0; i<mPaseadorAdapter.getCount();++i){
                                PaseadorClassAdapter temp = mPaseadorAdapter.getItem(i);
                                if(temp.getUidPaseador().equals(my_paseador.getUidPaseador()) ){
                                    temp.setLocalizacion(my_paseador.getLocalizacion());
                                    break;
                                }
                            }
                            break;
                        }
                    }
                }

                mPaseadorAdapter.sort(new Comparator<PaseadorClassAdapter>() {
                    @Override
                    public int compare(PaseadorClassAdapter lhs, PaseadorClassAdapter rhs) {
                        Double l = new Double(lhs.getDist());
                        Double r = new Double(rhs.getDist());
                        return l.compareTo(r);   //or whatever your sorting algorithm
                    }
                });

            }
        });

        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PaseadorClassAdapter p = mPaseadorAdapter.getItem(position);
                Log.i(TAG, p.toString());
                PaseadorSerializable my_p = new PaseadorSerializable(p);
                Intent i = new Intent(getApplicationContext(), ActividadPerfilPaseador.class);
                i.putExtra("paseador",my_p);
                startActivity(i);
            }
        });

        //usar item_paseador para adiocionar al recycler view



        //al momento de mostrar resultados hacer .setVisibility(View.GONE) al progress bar
        //el progress bar podría extenderse más en altura al mostrarse

        //usar onOptionsItemSelected para manejar acción para filtrar (ya pusé algo)

        View.OnClickListener on = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ActividadPerfilPaseador.class);
                startActivity(i);
            }
        };

    }

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
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
        }
    }

    private void pedirPermiso(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)){
                Toast.makeText(this,"La aplicación necesita permisos", Toast.LENGTH_LONG).show();
            }
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
        } else {
            startLocationUpdates();
            // permission granted, so...
        }
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
                    Toast.makeText(this,"La aplicación necesita permisos", Toast.LENGTH_LONG).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_filtro, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.cerrarSesionMenu:
                cerrarSesion();
                return true;
            case R.id.mostrarOpcionesFiltroMenu:
                /*if (activacionFiltro) {
                    Intent intent = new Intent(ActividadBusquedaPaseadores.this, ActivityFiltro.class);
                    startActivityForResult(intent, FILTRO_REQUEST);
                }else{
                    Toast toast = Toast.makeText(ActivityBuscarAnimales.this, "No se puede " +
                            "filtrar porque no hay paseadores disponibles", Toast.LENGTH_LONG);
                    toast.show();
                }*/
                return true;
            default:
                return true;
        }
    }

    private void cerrarSesion(){
        FirebaseAuth.getInstance().signOut();
    }
}
