package co.edu.javeriana.wowguau_paseador.views;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import co.edu.javeriana.wowguau_paseador.R;

public class PasswordRecoveryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_recovery);
    }
    public void recover_password(View view){
        boolean recovered = true;
        if(recovered){
            Toast.makeText(getApplicationContext(),"Contraseña recuperada exitosamente", Toast.LENGTH_SHORT).show();
        }
    }
}