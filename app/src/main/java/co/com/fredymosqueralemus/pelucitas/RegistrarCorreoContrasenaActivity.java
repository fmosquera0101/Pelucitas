package co.com.fredymosqueralemus.pelucitas;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import co.com.fredymosqueralemus.pelucitas.constantes.Constantes;
import co.com.fredymosqueralemus.pelucitas.sharedpreference.SharedPreferencesSeguro;
import co.com.fredymosqueralemus.pelucitas.sharedpreference.SharedPreferencesSeguroSingleton;

/**
 * Created by Fredy Mosquera Lemus on 2/02/17.
 */
public class RegistrarCorreoContrasenaActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText etxtCorreo;
    private EditText etxtContrasena;
    private SharedPreferencesSeguro sharedPreferencesSeguro;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_correo_contrasena);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sharedPreferencesSeguro = SharedPreferencesSeguroSingleton.getInstance(this, Constantes.SHARED_PREFERENCES_INFOUSUARIO, Constantes.SECURE_KEY_SHARED_PREFERENCES);
        mAuth = FirebaseAuth.getInstance();

        etxtCorreo = (EditText) findViewById(R.id.correo_etxt_registrarcorreocontrasenalayout);
        etxtContrasena = (EditText) findViewById(R.id.contrasena_etxt_registrarcorreocontrasenalayout);

    }
    public void registrarCorreoContrasena(View view){

        String correo = etxtCorreo.getText().toString();
        String contrasena = etxtContrasena.getText().toString();
        if(isCorreoValido(correo) && isContrasenaValida(contrasena)){
            crearUsuarioConCorreoContrasena(correo.trim().toLowerCase(), contrasena.trim());
        }

    }
    private void crearUsuarioConCorreoContrasena(final String correo, final String contrasena){
        mAuth.createUserWithEmailAndPassword(correo, contrasena).addOnCompleteListener(this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(RegistrarCorreoContrasenaActivity.this, R.string.str_creacionusuario_exitosa,
                                    Toast.LENGTH_SHORT ).show();
                            sharedPreferencesSeguro.put(Constantes.CORREO, correo);
                            sharedPreferencesSeguro.put(Constantes.CONTRASENA, contrasena);
                            sharedPreferencesSeguro.put(Constantes.ISLOGGED, Constantes.SI);
                            sharedPreferencesSeguro.put(Constantes.USERUID, mAuth.getCurrentUser().getUid());
                            abrirActivityRegistrarDatosPersonales();
                        }else{
                            Toast.makeText(RegistrarCorreoContrasenaActivity.this, R.string.str_creacionusuario_fallida,
                                    Toast.LENGTH_SHORT ).show();
                        }
                    }
                });
    }

    private void abrirActivityRegistrarDatosPersonales(){
        Intent mIntent = new Intent(this, RegistrarDatosPersonalesActivity.class);
        startActivity(mIntent);
        finish();
    }
    private boolean isCorreoValido(String correo){
        if(TextUtils.isEmpty(correo)){
            etxtCorreo.requestFocus();
            etxtCorreo.setError(getString(R.string.error_campo_requerido));
            return false;
        }else if(!isDireccionCorreoValida(correo.trim())){
            etxtCorreo.requestFocus();
            etxtCorreo.setError(getString(R.string.error_msj_correonovalido));
            return false;
        }
        return true;

    }
    private boolean isDireccionCorreoValida(String correo){
        Pattern pattern = Pattern.compile(Constantes.PETTERN_VALIDA_CORREO);
        Matcher matcher = pattern.matcher(correo);
        return matcher.matches();
    }
    private boolean isContrasenaValida(String contrasena){
        if(TextUtils.isEmpty(contrasena)){
            etxtContrasena.requestFocus();
            etxtContrasena.setError(getString(R.string.error_campo_requerido));
            return false;
        } else if(contrasena.trim().length() < 6){
            etxtContrasena.requestFocus();
            etxtContrasena.setError(getString(R.string.error_msj_contrasenadebil));
            return false;
        }

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        int item = menuItem.getItemId();
        if(item == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }

}
