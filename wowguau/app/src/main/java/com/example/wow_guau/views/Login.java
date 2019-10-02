package com.example.wow_guau.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.wow_guau.R;

public class Login extends AppCompatActivity {
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
                Intent i;
                if(email.getText().toString().equals("paseador")){
                    i = new Intent(getApplicationContext(), ActividadPantallaInicioPaseador.class);
                }
                else {
                    i = new Intent(getApplicationContext(), ActividadPantallaInicioDueno.class);
                }
                startActivity(i);
            }
        });

        btn_crear_cuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent i = new Intent(getApplicationContext() , Signup_walker.class);
                Intent i = new Intent(getApplicationContext() , ActividadRegistroUsuario.class);
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
    }
}
