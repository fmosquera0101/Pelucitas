package co.com.fredymosqueralemus.pelucitas;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

import co.com.fredymosqueralemus.pelucitas.constantes.Constantes;
import co.com.fredymosqueralemus.pelucitas.modelo.usuario.Usuario;
import co.com.fredymosqueralemus.pelucitas.utilidades.UtilidadesFecha;
import co.com.fredymosqueralemus.pelucitas.utilidades.UtilidadesFirebaseBD;

/**
 * Created by Fredy Mosquera Lemus on 2/02/17.
 */

public class RegistrarDatosPersonalesActivity extends AppCompatActivity {

    private EditText etxtCedulaIdentificacion;
    private EditText etxtNombre;
    private EditText etxtApellidos;
    private EditText etxtTelefono;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_datos_personales);

        etxtCedulaIdentificacion = (EditText) findViewById(R.id.cedula_ext_registrardatospersonaleslayout);
        etxtNombre = (EditText) findViewById(R.id.nombre_ext_registrardatospersonaleslayout);
        etxtApellidos = (EditText) findViewById(R.id.apellidos_ext_registrardatospersonaleslayout);
        etxtTelefono = (EditText) findViewById(R.id.telefono_ext_registrardatospersonaleslayout);

        mAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                firebaseUser = firebaseAuth.getCurrentUser();
            }
        };
    }

    public void registrarDatosPersonales(View view){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        DatabaseReference databaseReference = firebaseDatabase.getReference(UtilidadesFirebaseBD.getUrlInserccionUsuario(firebaseUser.getUid()));
        Usuario mUsuario = new Usuario();
        mUsuario.setKeyUid(firebaseUser.getUid());
        mUsuario.setCedulaIdentificacion(etxtCedulaIdentificacion.getText().toString());
        mUsuario.setNombre(etxtNombre.getText().toString().trim());
        mUsuario.setApellidos(etxtApellidos.getText().toString().trim());
        mUsuario.setTelefono(etxtTelefono.getText() != null?etxtTelefono.getText().toString().trim():"-");
        mUsuario.setFechaNacimiento(UtilidadesFecha.convertirDateAString(new Date()));
        mUsuario.setFechaInsercion(UtilidadesFecha.convertirDateAString(new Date()));
        mUsuario.setFechaModificacion(null);

        databaseReference.setValue(mUsuario);

        abrirActivityRegistrarDireccion();

    }
    public void abrirActivityRegistrarDireccion(){
        Intent mIntent = new Intent(this, RegistrarDireccionActivity.class);
        startActivity(mIntent);
    }
    @Override
    public void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    public void onStop(){
        super.onStop();
        if(mAuthStateListener != null){
            mAuth.removeAuthStateListener(mAuthStateListener);
        }
    }
}
