package co.com.fredymosqueralemus.pelucitas;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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
public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private SharedPreferencesSeguro sharedPreferencesSeguro;
    private EditText etxtCorreo;
    private EditText etxtContrasena;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        sharedPreferencesSeguro = SharedPreferencesSeguroSingleton.getInstance(this, Constantes.SHARED_PREFERENCES_INFOUSUARIO, Constantes.SECURE_KEY_SHARED_PREFERENCES);

        etxtCorreo = (EditText) findViewById(R.id.correo_etxt_loginlayout);
        etxtContrasena = (EditText) findViewById(R.id.contrasena_etxt_loginlayout);

    }

    public void iniciarSesion(View view){
        final String correo = etxtCorreo.getText().toString();
        final String contrasena = etxtContrasena.getText().toString();
        if(isCorreoValido(correo) && isContrasenaValida(contrasena)){
           mAuth.signInWithEmailAndPassword(correo.trim(), contrasena).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
               @Override
               public void onComplete(@NonNull Task<AuthResult> task) {
                   if(task.isSuccessful()){
                       sharedPreferencesSeguro.put(Constantes.CORREO, correo.trim());
                       sharedPreferencesSeguro.put(Constantes.CONTRASENA, contrasena);
                       sharedPreferencesSeguro.put(Constantes.ISLOGGED, Constantes.SI);
                       abrirHomeActivity();
                   }else {
                       Toast.makeText(LoginActivity.this, R.string.str_nohasiniciadosesion,
                               Toast.LENGTH_SHORT ).show();
                   }
               }
           });
        }
    }
    public void abrirHomeActivity(){
        Intent mIntent = new Intent(this, HomeActivity.class);
        mIntent.putExtra(Constantes.CALL_FROM_LOGINACTIVITY, LoginActivity.class.getName());
        startActivity(mIntent);
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
}
