package co.edu.javeriana.wowguau_paseador.views;

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

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
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
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

import co.edu.javeriana.wowguau_paseador.R;
import co.edu.javeriana.wowguau_paseador.model.Paseador;
import co.edu.javeriana.wowguau_paseador.utils.FirebaseUtils;
import co.edu.javeriana.wowguau_paseador.utils.Utils;

public class LoginActivity extends AppCompatActivity {
    Button btn_login;
    Button btn_crear_cuenta;
    TextView tv_fpassword;
    EditText et_email;
    EditText et_password;
    private ConstraintLayout cl_Facebook;
    private ConstraintLayout cl_Google;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseFirestore db;

    private int RC_SIGN_IN_GOOGLE = 5;
    private GoogleSignInOptions gso;
    private GoogleSignInClient mGoogleSignInClient;
    //Facebook Declaration
    CallbackManager mCallbackManager;
    LoginManager loginManager;

    private String TAG ="LOGIN";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        btn_login = findViewById(R.id.btn_login);
        btn_crear_cuenta = findViewById(R.id.btn_crear_cuenta);
        tv_fpassword = findViewById(R.id.tv_fpassword);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        cl_Facebook = findViewById(R.id.clInicioSesionFacebook);
        cl_Google = findViewById(R.id.clInicioSesionGoogle);

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
                signInUser(et_email.getText().toString(), et_password.getText().toString());
            }
        });

        btn_crear_cuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext() , RegistroActivity.class);
                startActivity(i);
            }
        });

        tv_fpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext() , PasswordRecoveryActivity.class);
                startActivity(i);
            }
        });
        cl_Google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_login.setEnabled(false);
                cl_Facebook.setEnabled(false);
                cl_Google.setEnabled(false);
                btn_crear_cuenta.setEnabled(false);
                tv_fpassword.setEnabled(false);

                signInGoogle();
            }
        });
        cl_Facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_login.setEnabled(false);
                cl_Facebook.setEnabled(false);
                cl_Google.setEnabled(false);
                btn_crear_cuenta.setEnabled(false);
                tv_fpassword.setEnabled(false);

                signInfacebook();
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
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
    private void updateUI(FirebaseUser currentUser){
        if(currentUser!=null){
            FirebaseUtils.buscarUsuario(currentUser.getUid(), this);
        }
        et_email.setText("");
        et_password.setText("");
    }
    private  boolean validateForm(){
        boolean valid = true;
        String correo = et_email.getText().toString();
        String contrasena = et_password.getText().toString();
        if(TextUtils.isEmpty(correo)){
            et_email.setError(Utils.obligatorio);
            valid = false;
        }else{
            et_email.setError(null);
        }
        if(TextUtils.isEmpty(contrasena)){
            et_password.setError(Utils.obligatorio);
            valid = false;
        }else{
            et_password.setError(null);
        }
        if(!isEmailValid(correo)){
            et_email.setError("Mal escrito");
            valid = false;
        }else{
            et_email.setError(null);
        }
        return valid;
    }
    private boolean isEmailValid(String correo) {
        Matcher matcher = Utils.VALID_EMAIL_ADDRESS_REGEX.matcher(correo);
        return matcher.matches();
    }
    private void signInUser(String correo, String contrasena){
        if(validateForm()){
            mAuth.signInWithEmailAndPassword(correo, contrasena)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(LoginActivity.this, "Error al autenticar", Toast.LENGTH_LONG).show();
                                updateUI(null);
                            }
                        }
                    });
        }
    }
    private void signInGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN_GOOGLE);
    }
    private void signInfacebook(){
        loginManager = LoginManager.getInstance();
        loginManager.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                if (AccessToken.getCurrentAccessToken() != null) {
                    Log.i(TAG,"token not null");
                    GraphRequest request = GraphRequest.newMeRequest(
                            loginResult.getAccessToken(),
                            new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(
                                        JSONObject object,
                                        GraphResponse response) {

                                    if (object != null) {
                                        try {
                                            AppEventsLogger logger = AppEventsLogger.newLogger(LoginActivity.this);
                                            Log.i(TAG,"Facebook login suceess");

                                            handleFacebookAccessToken(loginResult.getAccessToken());

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }

                                }
                            });
                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "id,name,email,gender, birthday, about");
                    request.setParameters(parameters);
                    request.executeAsync();
                }
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                desbloquearOpciones();
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                desbloquearOpciones();
            }
        });
        loginManager.logInWithReadPermissions(LoginActivity.this, Arrays.asList("email", "public_profile"));

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

            desbloquearOpciones();
        }
    }
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            currentUser = mAuth.getCurrentUser();
                            //updateUI(user);
                            boolean isNewUser = task.getResult().getAdditionalUserInfo().isNewUser();

                            Toast.makeText(LoginActivity.this, "Procesando, por favor espera",
                                    Toast.LENGTH_LONG).show();

                            if (isNewUser) { //toca crearle un documento en Clientes
                                Log.d(TAG, "Is New User!");
                                crearCliente(currentUser);
                            } else { //se va a verificar si el usuario es Cliente
                                Log.d(TAG, "Is Old User!");
                                updateUI(currentUser);
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Autenticación fallida",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                            desbloquearOpciones();
                        }

                        // ...
                    }
                });
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

                            boolean isNewUser = task.getResult().getAdditionalUserInfo().isNewUser();

                            if (isNewUser) { //toca crearle un documento en paseador
                                Log.d(TAG, "Is New User!");
                                crearCliente(currentUser);
                            } else { //se va a verificar si el usuario es paseador
                                Log.d(TAG, "Is Old User!");
                                updateUI(currentUser);
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Autenticación fallida",
                                    Toast.LENGTH_SHORT).show();

                            desbloquearOpciones();
                        }

                        // ...
                    }
                });
    }
    private void crearCliente(final FirebaseUser user){

        //inicializar los datos del cliente
        Paseador paseador = new Paseador(user.getEmail(), user.getDisplayName(), 0, null, 0, "", null, "", 0);

        GeoPoint ubicacion = new GeoPoint(0.0, 0.0);
        paseador.setUbicacion(ubicacion);
        paseador.setDireccionFoto(user.getPhotoUrl().toString());

        FirebaseUtils.guardarUsuario(paseador, currentUser.getUid(), null);
        updateUI(currentUser);
        desbloquearOpciones();
    }
    private void desbloquearOpciones(){
        btn_login.setEnabled(true);
        btn_crear_cuenta.setEnabled(true);
        cl_Facebook.setEnabled(true);
        cl_Google.setEnabled(true);
        tv_fpassword.setEnabled(true);
    }
}
