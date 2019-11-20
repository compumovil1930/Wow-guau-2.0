package co.edu.javeriana.wow_guau.views;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;
import java.util.Map;

import co.edu.javeriana.wow_guau.R;

public class MenuActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ConstraintLayout cl_logout;
    private ImageView imageViewUsuario;

    private String userId;
    private String nombreUsuario;
    double latitudUsuario, longitudUsuario;
    private String direccionFoto;

    private String TAG = "MenuActivity";

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
            .build();
    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();

    private TextView tv_h_nombre;
    private ImageView iv_perfil;
    private TextView tv_estado;
    private TextView tv_saldo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitNetwork().build();
        StrictMode.setThreadPolicy(policy);

        //Recuperar datos del usuario
        userId = getIntent().getStringExtra("uid");
        nombreUsuario = getIntent().getStringExtra("nombre");
        latitudUsuario = getIntent().getDoubleExtra("Latitud", 0.0);
        longitudUsuario = getIntent().getDoubleExtra("Longitud", 0.0);
        direccionFoto = getIntent().getStringExtra("PathPhoto");

        Log.i(TAG, "userId = "+userId);
        Log.i(TAG, "nombreUsuario = "+nombreUsuario);
        Log.i(TAG, "latitudUsuario = "+latitudUsuario);
        Log.i(TAG, "longitudUsuario = "+longitudUsuario);
        Log.i(TAG, "PathPhoto = "+direccionFoto);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        //imageViewUsuario = findViewById(R.id.ivFotoUsuarioMenu);
        //descargarFotoImageView(direccionFoto);
        //no supe cómo montar la imagen en el menú de esta actividad

        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerview = navigationView.getHeaderView(0);
        tv_h_nombre = headerview.findViewById(R.id.tv_h_nombre);
        //iv_perfil = headerview.findViewById(R.id.iv_perfil);
        tv_estado = headerview.findViewById(R.id.estado);
        tv_saldo = headerview.findViewById(R.id.saldo);

        imageViewUsuario = headerview.findViewById(R.id.ivFotoUsuarioMenu);
        if(latitudUsuario != 0 && longitudUsuario!= 0){
            descargarFotoImageView(direccionFoto);
        }else{ //autenticado desde proveedores
            descargarImagenDesdeProveedor();
        }

        cl_logout = findViewById(R.id.cl_logout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_inicio, R.id.nav_actualizar, R.id.nav_historial) //acá se agregan las otras opciones del menu
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        headerview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), PerfilActivity.class);
                startActivity(i);
            }
        });
        cl_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth .signOut();
                Intent i = new Intent(getBaseContext(), LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

        db.collection("Clientes").document(mAuth.getCurrentUser().getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                Map<String,Object> vals = documentSnapshot.getData();

    /*
                paseador.setNombre( );
                paseador.setSaldo( );
                paseador.setEstado((boolean) vals.get("estado"));


     */
                tv_h_nombre.setText((String) vals.get("nombre"));
                //tv_estado.setText(" "+(paseador.isEstado()? "Disponible": "No disponible"));
                tv_saldo.setText((long) vals.get("saldo") + " petCoins");
                //fragmentRefreshListener.onRefresh();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void descargarFotoImageView(String ruta){
        db.setFirestoreSettings(settings);
        StorageReference photoRef = mStorageRef.child("images").child(ruta);
        final long ONE_MEGABYTE = 1024 * 1024;
        photoRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                imageViewUsuario.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Log.i(TAG, "No se pudo cargar la foto del usuario");
            }
        });
    }

    public void descargarImagenDesdeProveedor(){

        new Thread(new Runnable() {
            public void run() {
                // a potentially time consuming task

                try {
                    String imageUrl = direccionFoto;
                    InputStream URLcontent = (InputStream) new URL(imageUrl).getContent();
                    final Drawable image = Drawable.createFromStream(URLcontent, "your source link");

                    imageViewUsuario.post(new Runnable() {
                        public void run() {
                            imageViewUsuario.setImageDrawable(image);
                        }
                    });

                    //holder.imageViewFoto.setImageDrawable(image);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

        }).start();

    }

}
