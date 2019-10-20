package co.edu.javeriana.wow_guau.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.OAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import co.edu.javeriana.wow_guau.R;
import co.edu.javeriana.wow_guau.model.Dueno;


public class LoginActivity extends AppCompatActivity {

    Button btn_login;
    Button btn_crear_cuenta;
    TextView tv_fpassword;
    EditText editTextEmail;
    EditText editTextContrasena;

    ConstraintLayout constraintLayoutFacebook;
    ConstraintLayout constraintLayoutTwitter;
    ConstraintLayout constraintLayoutGoogle;

    private FirebaseAuth mAuth;
    FirebaseUser currentUser;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    String tipoUsuario;

    private static final String TAG = "ActivityLogin";

    private int RC_SIGN_IN_GOOGLE = 5;
    GoogleSignInOptions gso;
    GoogleSignInClient mGoogleSignInClient;

    OAuthProvider.Builder provider = OAuthProvider.newBuilder("twitter.com");

    @Override
    protected void onStart() {
        super.onStart();
        /*
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);*/
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn_login = findViewById(R.id.btn_login);
        btn_crear_cuenta = findViewById(R.id.btn_crear_cuenta);
        tv_fpassword = findViewById(R.id.tv_fpassword);
        editTextEmail = findViewById(R.id.etEmail);
        editTextContrasena = findViewById(R.id.etPassword);

        constraintLayoutFacebook = findViewById(R.id.clInicioSesionFacebook);
        constraintLayoutTwitter = findViewById(R.id.clInicioSesionTwitter);
        constraintLayoutGoogle = findViewById(R.id.clInicioSesionGoogle);

        mAuth = FirebaseAuth.getInstance();

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                 .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if( validarFormulario() ) {

                    String email = editTextEmail.getText().toString();
                    String password = editTextContrasena.getText().toString();
                    btn_login.setEnabled(false);
                    constraintLayoutFacebook.setEnabled(false);
                    constraintLayoutTwitter.setEnabled(false);
                    constraintLayoutGoogle.setEnabled(false);
                    btn_crear_cuenta.setEnabled(false);
                    Toast.makeText(LoginActivity.this, "Procesando, por favor espera",
                            Toast.LENGTH_SHORT).show();
                    signInUser(email, password);
                }

                /*Intent i;
                if(editTextEmail.getText().toString().equals("dueno")){ // verificar que sea dueno en firebase
                    i = new Intent(getApplicationContext(), MenuActivity.class);
                    startActivity(i);
                }
                else{
                    Toast.makeText(getBaseContext(), "No eres un dueño", Toast.LENGTH_LONG).show();
                }*/
            }
        });

        btn_crear_cuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext() , Signup_owner.class);
                startActivity(i);
            }
        });

        tv_fpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext() , Password_recovery.class);
                startActivity(i);
            }
        });

        constraintLayoutFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        constraintLayoutTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        constraintLayoutGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInGoogle();
            }
        });
    }

    private boolean validarFormulario() {

        boolean valid = true;
        String email = editTextEmail.getText().toString();

        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError("Requerido");
            valid = false;
        } else {
            editTextEmail.setError(null);
        }

        if(!isEmailValid(email)){
            valid = false;
        }

        String password = editTextContrasena.getText().toString();
        if (TextUtils.isEmpty(password)) {
            editTextContrasena.setError("Requerido");
            valid = false;
        } else {
            if(password.length() < 8){
                editTextContrasena.setError("Debe tener mínimo 8 caracteres");
                valid = false;
            }else {
                editTextContrasena.setError(null);
            }
        }
        return valid;
    }

    private boolean isEmailValid(String email) {
        if (!email.contains("@") ||
                !email.contains(".") ||
                email.length() < 5) {
            editTextEmail.setError("Correo electrónico incorrecto");
            return false;
        }
        return true;
    }

    private void signInUser(String email, String password) {

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI
                            Log.d(TAG, "signInWithEmail:success");
                            currentUser = mAuth.getCurrentUser();
                            //loginExitoso = true;
                            updateUI();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            currentUser = null;
                            Toast.makeText(LoginActivity.this, "Autenticación fallida",
                                    Toast.LENGTH_SHORT).show();
                            updateUI();
                        }
                    }
                });

    }

    private void updateUI(){

        if(currentUser != null){
            editTextEmail.setText("");
            editTextContrasena.setText("");
            verificarTipoUsuario(currentUser.getUid());
        }else{
            btn_login.setEnabled(true);
            btn_crear_cuenta.setEnabled(true);
            constraintLayoutFacebook.setEnabled(true);
            constraintLayoutTwitter.setEnabled(true);
            constraintLayoutGoogle.setEnabled(true);
        }

    }

    private void verificarTipoUsuario(final String userId){

        db.collection("Clientes").document(userId).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists() ){
                            //Dueno dueno = documentSnapshot.toObject(Dueno.class);
                            Intent i = new Intent(LoginActivity.this, MenuActivity.class);
                            i.putExtra("nombre", documentSnapshot.getString("nombre") );
                            i.putExtra("uid", userId);
                            GeoPoint ubicacionUsuario = documentSnapshot.getGeoPoint("ubicacion");
                            i.putExtra("Latitud", ubicacionUsuario.getLatitude() );
                            i.putExtra("Longitud",  ubicacionUsuario.getLongitude() );
                            i.putExtra("PathPhoto",  documentSnapshot.getString("direccionFoto") );
                            startActivity(i);
                        }else{
                            Toast.makeText(LoginActivity.this, "No te encuentras registrado como dueño",
                                    Toast.LENGTH_SHORT).show();
                            btn_login.setEnabled(true);
                            btn_crear_cuenta.setEnabled(true);
                            constraintLayoutFacebook.setEnabled(true);
                            constraintLayoutTwitter.setEnabled(true);
                            constraintLayoutGoogle.setEnabled(true);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LoginActivity.this, "Problema durante autenticación",
                                Toast.LENGTH_SHORT).show();

                        btn_login.setEnabled(true);
                        btn_crear_cuenta.setEnabled(true);
                        constraintLayoutFacebook.setEnabled(true);
                        constraintLayoutTwitter.setEnabled(true);
                        constraintLayoutGoogle.setEnabled(true);
                    }
                });

    }

    private void signInGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN_GOOGLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN_GOOGLE) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResultGoogle(task);
        }
    }

    private void handleSignInResultGoogle(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            firebaseAuthWithGoogle(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(LoginActivity.this, "Autenticación fallida",
                    Toast.LENGTH_SHORT).show();

            btn_login.setEnabled(true);
            btn_crear_cuenta.setEnabled(true);
            constraintLayoutFacebook.setEnabled(true);
            constraintLayoutTwitter.setEnabled(true);
            constraintLayoutGoogle.setEnabled(true);
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            currentUser = mAuth.getCurrentUser();
                            Intent i = new Intent(LoginActivity.this, MenuActivity.class);
                            startActivity(i);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Autenticación fallida",
                                    Toast.LENGTH_SHORT).show();

                            btn_login.setEnabled(true);
                            btn_crear_cuenta.setEnabled(true);
                            constraintLayoutFacebook.setEnabled(true);
                            constraintLayoutTwitter.setEnabled(true);
                            constraintLayoutGoogle.setEnabled(true);

                        }

                        // ...
                    }
                });
    }

    @Override
    protected void onPause() {
        super.onPause();
        //finish();
    }

}
