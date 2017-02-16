package co.com.fredymosqueralemus.pelucitas;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

import co.com.fredymosqueralemus.pelucitas.modelo.usuario.PerfilesXUsuario;
import co.com.fredymosqueralemus.pelucitas.utilidades.UtilidadesFecha;
import co.com.fredymosqueralemus.pelucitas.utilidades.UtilidadesFirebaseBD;

public class RegistrarPerfilUsuarioActivity extends AppCompatActivity {

    private CheckBox ckbxPerfilEmpleado;
    private CheckBox ckbxPerfilAdministrador;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_perfil_usuario);
        ckbxPerfilEmpleado = (CheckBox) findViewById(R.id.perfilempleado_chkbx_registrarperfillayout);
        ckbxPerfilAdministrador = (CheckBox) findViewById(R.id.perfiladministrador_chkbx_registrarperfillayout);

        mAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                firebaseUser = firebaseAuth.getCurrentUser();
            }
        };

    }

    /**
     * Este metodo se ejecuta en respuesta al clic del boto siguiente de esta activity
     * Es el encargado de registrar la informacion de la direccion ingresada por el usuario
     * @param view
     * Created by Fredy Mosquera Lemus on 9/02/17.
     */
    public void registrarPerfilesXUsuario(View view){
        PerfilesXUsuario perfilesXUsuario;
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference;
        perfilesXUsuario = new PerfilesXUsuario();
        perfilesXUsuario.setKeyUsuarioId(firebaseUser.getUid());
        perfilesXUsuario.setFechaInsercion(UtilidadesFecha.convertirDateAString(new Date()));
        perfilesXUsuario.setFechaModificacion(null);
        //Setteamos por defecto el perfil Usuario o cliente
        databaseReference = firebaseDatabase.getReference(UtilidadesFirebaseBD.getUrlInserccionPerfilEmpleado(firebaseUser.getUid()));
        databaseReference.setValue(perfilesXUsuario);

        if(ckbxPerfilEmpleado.isChecked()){
            databaseReference = firebaseDatabase.getReference(UtilidadesFirebaseBD.getUrlInserccionPerfilEmpleado(firebaseUser.getUid()));
            databaseReference.setValue(perfilesXUsuario);

        }
        if(ckbxPerfilAdministrador.isChecked()){
            databaseReference = firebaseDatabase.getReference(UtilidadesFirebaseBD.getUrlInserccionPerfilAdministrador(firebaseUser.getUid()));
            databaseReference.setValue(perfilesXUsuario);
        }

        abrirActivityHome();

    }
    private void abrirActivityHome(){
        /*Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);*/
        finish();
    }
    @Override
    public void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }
    @Override
    public void onStop(){
        super.onStop();
        mAuth.removeAuthStateListener(mAuthStateListener);
    }
}
