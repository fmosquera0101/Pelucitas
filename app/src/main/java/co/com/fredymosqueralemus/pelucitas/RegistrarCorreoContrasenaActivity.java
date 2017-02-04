package co.com.fredymosqueralemus.pelucitas;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Fredy Mosquera Lemus on 2/02/17.
 */
public class RegistrarCorreoContrasenaActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    EditText etxtCorreo;
    EditText etxtContrasena;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_correo_contrasena);
        mAuth = FirebaseAuth.getInstance();

        etxtCorreo = (EditText) findViewById(R.id.correo_etxt_registrarcorreocontrasenalayout);
        etxtContrasena = (EditText) findViewById(R.id.contrasena_etxt_registrarcorreocontrasenalayout);

    }
    public void registrarCorreoContrasena(View view){
        String correo = etxtCorreo.getText().toString();
        String contrasena = etxtContrasena.getText().toString();
        crearUsuarioConCorreoContrasena(correo, contrasena);

    }
    public void crearUsuarioConCorreoContrasena(String correo, String contrasena){
        mAuth.createUserWithEmailAndPassword(correo, contrasena).addOnCompleteListener(this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(RegistrarCorreoContrasenaActivity.this, R.string.str_creacionusuario_exitosa,
                                    Toast.LENGTH_SHORT ).show();
                            abrirActivityRegistrarDatosPersonales();
                        }else{
                            Toast.makeText(RegistrarCorreoContrasenaActivity.this, R.string.str_creacionusuario_fallida,
                                    Toast.LENGTH_SHORT ).show();
                        }
                    }
                });
    }

    public void abrirActivityRegistrarDatosPersonales(){
        Intent mIntent = new Intent(this, RegistrarDatosPersonalesActivity.class);
        startActivity(mIntent);
    }
}
