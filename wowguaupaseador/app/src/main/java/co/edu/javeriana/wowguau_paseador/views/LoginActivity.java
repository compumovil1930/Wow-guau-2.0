package co.edu.javeriana.wowguau_paseador.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import co.edu.javeriana.wowguau_paseador.R;

public class LoginActivity extends AppCompatActivity {
    Button btn_login;
    Button btn_crear_cuenta;
    TextView tv_fpassword;
    EditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn_login = findViewById(R.id.button_login);
        btn_crear_cuenta = findViewById(R.id.btn_crear_cuenta);
        tv_fpassword = findViewById(R.id.tv_fpassword);
        email = findViewById(R.id.email);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i ;
                if(email.getText().toString().equals("paseador")){ // verificar que sea dueno en firebase
                    i = new Intent(getApplicationContext(), MenuActivity.class);
                    startActivity(i);
                }
                else{
                    Toast.makeText(getBaseContext(), "No eres un paseador", Toast.LENGTH_LONG).show();
                }
            }
        });

        btn_crear_cuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent i = new Intent(getApplicationContext() , Signup_walker.class);
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
}
