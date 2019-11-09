package co.edu.javeriana.wow_guau.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import co.edu.javeriana.wow_guau.R;
import co.edu.javeriana.wow_guau.model.Direccion;
import co.edu.javeriana.wow_guau.model.Dueno;
import co.edu.javeriana.wow_guau.utils.CameraUtils;
import co.edu.javeriana.wow_guau.utils.Permisos;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class Signup_owner extends AppCompatActivity {

    private EditText et_email;
    private EditText et_password;
    private EditText et_nombre;
    private EditText et_cedula;
    private EditText et_fecha_nacimiento;
    private EditText et_phone;
    private EditText et_direccion;
    private RadioGroup rg_genero;
    private ImageButton ib_upload_photo;
    private Button button_register;
    private ScrollView scrollView;
    private ImageView imageViewFoto;
    private ImageButton imageButtonGaleria;

    private DatePickerDialog.OnDateSetListener date;
    private Calendar calendar;
    private Address address;

    private StorageReference storageReference;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private FirebaseAuth mAuth;

    private static final int REQUEST_IMAGE_CAPTURE = 3;
    private static final int IMAGE_PICKER_REQUEST = 2;

    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 0;
    private static final int MY_PERMISSIONS_READ_EXTERNAL_STORAGE = 1;

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 4;

    private static String[] PERMISSIONS = {
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.ACCESS_FINE_LOCATION
    };

    private static final String TAG = "ActivityRegistro";

    private Dueno cliente;
    private String idUser;

    private GeoPoint ubicacionCliente;

    private FirebaseFirestoreSettings settings;

    private boolean IngresoActividadMapas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_owner);

        IngresoActividadMapas = false;

        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        et_nombre = findViewById(R.id.et_nombre);
        et_cedula = findViewById(R.id.et_cedula);
        rg_genero = findViewById(R.id.rg_genero);
        et_fecha_nacimiento = findViewById(R.id.et_date);
        et_phone = findViewById(R.id.et_phone);
        et_direccion = findViewById(R.id.et_direccion);
        scrollView = findViewById(R.id.scrollView);
        ib_upload_photo = findViewById(R.id.ib_upload_photo);
        button_register = findViewById(R.id.button_register);
        imageViewFoto = findViewById(R.id.ivFotoUsuario);
        imageButtonGaleria = findViewById(R.id.ibGaleria);

        settings = new FirebaseFirestoreSettings.Builder()
                .build();
        storageReference = FirebaseStorage.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();

        ubicacionCliente = new GeoPoint(0,0);

        cliente = null;

        calendar = Calendar.getInstance();

        ib_upload_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //CameraUtils.startDialog(Signup_owner.this);
                if (ContextCompat.checkSelfPermission(v.getContext(), Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED) {
                    takePicture();
                }else{
                    requestPermission(Signup_owner.this, PERMISSIONS[0], "Acceso " +
                                    "a cámara necesario",
                            MY_PERMISSIONS_REQUEST_CAMERA);
                }
            }
        });

        imageButtonGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(view.getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {

                    Intent pickImage = new Intent(Intent.ACTION_PICK);
                    pickImage.setType("image/*");
                    startActivityForResult(pickImage, IMAGE_PICKER_REQUEST);

                }else{
                    requestPermission(Signup_owner.this, PERMISSIONS[1], "Leer " +
                                    "almacenamiento es necesario",
                            MY_PERMISSIONS_READ_EXTERNAL_STORAGE);
                }
            }
        });

        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cliente = registrarDueno();
                if (cliente != null && ubicacionCliente.getLatitude()!=0 && ubicacionCliente.getLongitude() != 0) {
                    cliente.setUbicacion(ubicacionCliente);
                    button_register.setEnabled(false);
                    registrarUsuario();
                }else{
                    if( ubicacionCliente.getLatitude()==0 && ubicacionCliente.getLongitude() == 0 ){
                        Toast toast = Toast.makeText(Signup_owner.this, "No se ha encontrado " +
                                "la ubicación geográfica", Toast.LENGTH_LONG);
                        toast.show();
                    }
                }

            }
        });
        date = new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDate();
            }
        };
        et_fecha_nacimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(Signup_owner.this, date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        et_direccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(Signup_owner.this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {

                    Intent i = new Intent(Signup_owner.this, MapsActivity.class);
                    startActivityForResult(i, Permisos.ADDRESS_PICKER);

                }else{
                    requestPermission(Signup_owner.this, PERMISSIONS[2], "Acceso a GPS necesario",
                            MY_PERMISSIONS_REQUEST_LOCATION);
                }

            }
        });

    }

    private void updateDate() {
        String format = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        et_fecha_nacimiento.setText(sdf.format(calendar.getTime()));
    }

    private Dueno registrarDueno() {

        boolean completo = true;
        int radioButtonID = rg_genero.getCheckedRadioButtonId();
        View radioButton = rg_genero.findViewById(radioButtonID);
        int idx = rg_genero.indexOfChild(radioButton);

        String email = et_email.getText().toString();
        String contrasena = et_password.getText().toString();
        String nombre = et_nombre.getText().toString();
        String cedula = et_cedula.getText().toString();
        String fecha = et_fecha_nacimiento.getText().toString();
        String phone = et_phone.getText().toString();
        String direccion = et_direccion.getText().toString();
        String genero;

        if (imageViewFoto.getDrawable() == null){ //no seleccionó imagen
            completo = false;
            Toast toast = Toast.makeText(Signup_owner.this, "Se debe seleccionar o " +
                    "tomar una foto", Toast.LENGTH_LONG);
            toast.show();
        }
        if(email.isEmpty()){
            et_email.setError(getString(R.string.obligatorio));
            completo = false;
        }else{
            if( isEmailValid(email) ) {
                et_email.setError(null);
            }
        }
        if(contrasena.isEmpty()){
            et_password.setError(getString(R.string.obligatorio));
            completo = false;
        }else{
            if(contrasena.length() < 6) {
                et_password.setError("Debe tener mínimo 6 caracteres");
                completo = false;
            }else{
                et_password.setError(null);
            }
        }
        if(nombre.isEmpty()){
            et_nombre.setError(getString(R.string.obligatorio));
            completo = false;
        }else{
            et_nombre.setError(null);
        }
        if(cedula.isEmpty()){
            et_cedula.setError(getString(R.string.obligatorio));
            completo = false;
        }else{
            if( isNumeric(cedula) ){
                et_cedula.setError(null);
            }else{
                et_cedula.setError("Todos los caracteres deben ser números");
                completo = false;
            }
        }
        if(fecha.isEmpty()){
            et_fecha_nacimiento.setError(getString(R.string.obligatorio));
            completo = false;
        }else{
            if(isDateValid(fecha)){
                et_fecha_nacimiento.setError(null);
            }else{
                et_fecha_nacimiento.setError("Formato incorrecto");
                completo = false;
            }
        }
        if(phone.isEmpty()){
            et_phone.setError(getString(R.string.obligatorio));
            completo = false;
        }else{
            if( isNumeric(phone) ){
                et_phone.setError(null);
            }else{
                et_phone.setError("Todos los caracteres deben ser números");
                completo = false;
            }
        }
        if(direccion.isEmpty()){
            et_direccion.setError(getString(R.string.obligatorio));
            completo = false;
        }else{
            if(IngresoActividadMapas) {
                et_direccion.setError(null);
            }else{
                completo = false;
                Toast toast = Toast.makeText(Signup_owner.this, "Debes desplegar " +
                        "la búsqueda de dirección clickeando sobre el campo dirección", Toast.LENGTH_LONG);
                toast.show();
            }
        }
        if(radioButtonID==-1) {
            completo = false;
            Toast toast = Toast.makeText(Signup_owner.this, "Se debe seleccionar un " +
                    "género", Toast.LENGTH_LONG);
            toast.show();
        }
        if(!completo)
            return null;
        genero = ((RadioButton)(rg_genero.getChildAt(idx))).getText().toString();

        Date fechaNacimiento = null;
        try {
            fechaNacimiento = new SimpleDateFormat("MM/dd/yyyy").parse(fecha);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return new Dueno(email, nombre, Long.parseLong(cedula), fechaNacimiento, Long.parseLong(phone), genero, direccion);
    }

    private boolean isDateValid(String fecha){
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
            date = sdf.parse(fecha);
            if (date != null && !fecha.equals(sdf.format(date))) {
                date = null;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        if (date == null) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean isNumeric(String strNum) {
        try {
            long d = Long.parseLong(strNum);
        } catch (NumberFormatException | NullPointerException nfe) {
            return false;
        }
        return true;
    }

    private boolean isEmailValid(String email) {
        if (!email.contains("@") ||
                !email.contains(".") ||
                email.length() < 5) {
            et_email.setError("Correo electrónico incorrecto");
            return false;
        }
        return true;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case REQUEST_IMAGE_CAPTURE:
                if (resultCode == RESULT_OK) {
                    try {
                        Bundle extras = data.getExtras();
                        Bitmap imageBitmap;
                        if (extras != null) {
                            imageBitmap = (Bitmap) extras.get("data");
                            imageViewFoto.setImageBitmap(imageBitmap);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return;

            case IMAGE_PICKER_REQUEST:
                if (resultCode == RESULT_OK) {
                    try {
                        final Uri imageUri = data.getData();
                        final InputStream imageStream;
                        if (imageUri != null) {
                            imageStream = getContentResolver().openInputStream(imageUri);
                            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                            imageViewFoto.setImageBitmap(selectedImage);
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                return;

            case Permisos.ADDRESS_PICKER:
                if (resultCode == RESULT_OK) {

                    IngresoActividadMapas = true;

                    Bundle extras = data.getExtras();
                    address = (Address) extras.get("address");
                    et_direccion.setText(extras.get("direccion").toString());

                    ubicacionCliente = new GeoPoint(address.getLatitude(), address.getLongitude());

                    Log.i(TAG, String.valueOf(address.getLatitude()));
                    Log.i(TAG, String.valueOf(address.getLongitude()));
                }
                return;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        switch (requestCode)
        {
            case MY_PERMISSIONS_REQUEST_CAMERA:
            {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takePicture();
                }
            }
            break;

            case MY_PERMISSIONS_READ_EXTERNAL_STORAGE:
            {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent pickImage = new Intent(Intent.ACTION_PICK);
                    pickImage.setType("image/*");
                    startActivityForResult(pickImage, IMAGE_PICKER_REQUEST);
                }
            }
            break;

            case MY_PERMISSIONS_REQUEST_LOCATION:
            {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, continue with task related to permission
                    //revisarActivacionGPS();
                    Intent i = new Intent(Signup_owner.this, MapsActivity.class);
                    startActivityForResult(i, Permisos.ADDRESS_PICKER);
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

    private void takePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void registrarUsuario(){

        mAuth.createUserWithEmailAndPassword(et_email.getText().toString(), et_password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                            FirebaseUser user = mAuth.getCurrentUser();
                            if(user!=null){ //Update user Info
                                UserProfileChangeRequest.Builder upcrb = new UserProfileChangeRequest.Builder();
                                upcrb.setDisplayName(et_nombre.getText().toString());
                                upcrb.setPhotoUri(Uri.parse("clientes/photo_"+user.getUid()+".jpg"));
                                user.updateProfile(upcrb.build());

                                Toast.makeText(Signup_owner.this, "Usuario creado con éxito",
                                        Toast.LENGTH_SHORT).show();

                                idUser = user.getUid();
                                crearCliente(user);
                            }
                        }
                        if (!task.isSuccessful()) {
                            if(task.getException().getMessage().equals("The email address is already in use by another account.")) {
                                Toast.makeText(Signup_owner.this, "Falló la creación de usuario: el " +
                                        "email ya está siendo usando", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(Signup_owner.this, "Falló la creación de usuario: "
                                                + Objects.requireNonNull(task.getException()).toString(),
                                        Toast.LENGTH_SHORT).show();
                                Log.e(TAG, Objects.requireNonNull(task.getException().getMessage()));
                            }
                            button_register.setEnabled(true);
                        }
                    }
                });

    }

    private void crearCliente(final FirebaseUser user){
        cliente.setDireccionFoto( "clientes/photo_"+user.getUid()+".jpg" );
        db.collection("Clientes").document(user.getUid())
                .set(cliente)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        subirFoto("clientes/photo_"+user.getUid()+".jpg",
                                ((BitmapDrawable)imageViewFoto.getDrawable()).getBitmap());

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                        Toast.makeText(Signup_owner.this, "Fallo la creación de usuario: ",
                                Toast.LENGTH_SHORT).show();
                        button_register.setEnabled(true);
                    }
                });

    }

    public void subirFoto(String ruta, Bitmap photo){
        db.setFirestoreSettings(settings);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = storageReference.child("images").child(ruta).putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.i("ERROR", "No se pudo subir la foto");
                button_register.setEnabled(true);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.i(TAG, "Foto subida");
                Intent i = new Intent(getApplicationContext() , MenuActivity.class);

                i.putExtra("nombre", cliente.getNombre() );
                i.putExtra("uid", idUser);
                i.putExtra("Latitud", cliente.getUbicacion().getLatitude() );
                i.putExtra("Longitud",   cliente.getUbicacion().getLongitude() );
                i.putExtra("PathPhoto",  cliente.getDireccionFoto() );

                startActivity(i);
                finish();
            }
        });
    }

}
