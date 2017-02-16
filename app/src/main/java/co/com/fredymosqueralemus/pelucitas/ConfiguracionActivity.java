package co.com.fredymosqueralemus.pelucitas;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import co.com.fredymosqueralemus.pelucitas.constantes.Constantes;
import co.com.fredymosqueralemus.pelucitas.sharedpreference.SharedPreferencesSeguro;
import co.com.fredymosqueralemus.pelucitas.sharedpreference.SharedPreferencesSeguroSingleton;

public class ConfiguracionActivity extends AppCompatActivity {

    SharedPreferencesSeguro sharedPreferencesSeguro;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);
        sharedPreferencesSeguro = SharedPreferencesSeguroSingleton.getInstance(this, Constantes.SHARED_PREFERENCES_INFOUSUARIO, Constantes.SECURE_KEY_SHARED_PREFERENCES);
        mAuth = FirebaseAuth.getInstance();

    }
    public void cerrarSesion(View view){
        sharedPreferencesSeguro.clear();
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(ConfiguracionActivity.this, R.string.str_hascerradosesion,
                Toast.LENGTH_SHORT ).show();
        finish();

    }
}
