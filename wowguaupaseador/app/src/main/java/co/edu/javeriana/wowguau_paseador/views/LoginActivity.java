package co.edu.javeriana.wowguau_paseador.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

import co.edu.javeriana.wowguau_paseador.R;
import co.edu.javeriana.wowguau_paseador.utils.FirebaseUtils;
import co.edu.javeriana.wowguau_paseador.utils.Utils;

public class LoginActivity extends AppCompatActivity {
    Button btn_login;
    Button btn_crear_cuenta;
    TextView tv_fpassword;
    EditText et_email;
    EditText et_password;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        btn_login = findViewById(R.id.btn_login);
        btn_crear_cuenta = findViewById(R.id.btn_crear_cuenta);
        tv_fpassword = findViewById(R.id.tv_fpassword);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);

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
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
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
                                Log.d("BIEN", "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("MAL", "signInWithEmail:failure", task.getException());
                                Toast.makeText(LoginActivity.this, "Error al autenticar", Toast.LENGTH_LONG).show();
                                updateUI(null);
                            }
                        }
                    });
        }
    }
}
