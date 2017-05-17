package co.com.fredymosqueralemus.pelucitas;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.google.android.gms.vision.text.Line;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

import co.com.fredymosqueralemus.pelucitas.constantes.Constantes;
import co.com.fredymosqueralemus.pelucitas.modelo.usuario.PerfilesXUsuario;
import co.com.fredymosqueralemus.pelucitas.modelo.usuario.Usuario;
import co.com.fredymosqueralemus.pelucitas.utilidades.UtilidadesFecha;
import co.com.fredymosqueralemus.pelucitas.utilidades.UtilidadesFirebaseBD;

public class RegistrarPerfilUsuarioActivity extends AppCompatActivity {

    private CheckBox ckbxPerfilEmpleado;
    private CheckBox ckbxPerfilAdministrador;

    private LinearLayout linearLayoutEditarCanceraRegistroPerfil;
    private LinearLayout linearLayoutRegistrarPerfil;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private Usuario usuario;
    private Intent intent;
    private PerfilesXUsuario perfilesXUsuarioAdministrador;
    private  PerfilesXUsuario perfilesXUsuarioEmpleado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_perfil_usuario);
        ckbxPerfilEmpleado = (CheckBox) findViewById(R.id.perfilempleado_chkbx_registrarperfillayout);
        ckbxPerfilAdministrador = (CheckBox) findViewById(R.id.perfiladministrador_chkbx_registrarperfillayout);
        linearLayoutEditarCanceraRegistroPerfil = (LinearLayout) findViewById(R.id.layout_editar_cancelar_registrarperfillayout);
        linearLayoutRegistrarPerfil = (LinearLayout) findViewById(R.id.layout_registrarpefil_registrarperfillayout);

        intent = getIntent();
        if(AdministrarMiPerfilActivity.class.getName().equals(intent.getStringExtra(Constantes.CALL_FROM_ACTIVITY_ADMINISTRARMIPERFIL))){
            getSupportActionBar().setTitle(getString(R.string.srt_ditarperfilusuario));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            linearLayoutEditarCanceraRegistroPerfil.setVisibility(View.VISIBLE);
            linearLayoutRegistrarPerfil.setVisibility(View.GONE);
            usuario = (Usuario) intent.getSerializableExtra(Constantes.USUARIO_OBJECT);
            settearPerfiles(usuario);
        }
        mAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                firebaseUser = firebaseAuth.getCurrentUser();
            }
        };

    }
    private void settearPerfiles(Usuario usuario){
        DatabaseReference databaseReference = firebaseDatabase.getReference(UtilidadesFirebaseBD.getUrlInserccionPerfilAdministrador(usuario.getKeyUid()));
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                perfilesXUsuarioAdministrador = (PerfilesXUsuario) dataSnapshot.getValue(PerfilesXUsuario.class);
                if("S".equals(perfilesXUsuarioAdministrador.getActivo())){
                    ckbxPerfilAdministrador.setChecked(true);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        databaseReference = firebaseDatabase.getReference(UtilidadesFirebaseBD.getUrlInserccionPerfilEmpleado(usuario.getKeyUid()));
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                perfilesXUsuarioEmpleado = (PerfilesXUsuario) dataSnapshot.getValue(PerfilesXUsuario.class);
                if("S".equals(perfilesXUsuarioEmpleado.getActivo())){
                    ckbxPerfilEmpleado.setChecked(true);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * Este metodo se ejecuta en respuesta al clic del boto siguiente de esta activity
     * Es el encargado de registrar la informacion de la direccion ingresada por el usuario
     * @param view
     * Created by Fredy Mosquera Lemus on 9/02/17.
     */
    public void registrarPerfilesXUsuario(View view){
        guardarPerfilUsuario(firebaseUser.getUid());
        abrirActivityHome();
    }
    public void editarPerfilesXUsuario(View view){
        guardarPerfilUsuario(usuario.getKeyUid());
        finish();
    }
    protected void guardarPerfilUsuario(String usuarioUid){

        if(AdministrarMiPerfilActivity.class.getName().equals(intent.getStringExtra(Constantes.CALL_FROM_ACTIVITY_ADMINISTRARMIPERFIL))){

            perfilesXUsuarioAdministrador.setFechaModificacion(UtilidadesFecha.convertirDateAString(new Date()));
            insertarPerfilAdministrador(perfilesXUsuarioAdministrador);
            perfilesXUsuarioEmpleado.setFechaModificacion(UtilidadesFecha.convertirDateAString(new Date()));
            insertarPerfilEmpleado(perfilesXUsuarioEmpleado);

        }else{
            PerfilesXUsuario perfilesXUsuario = new PerfilesXUsuario();
            perfilesXUsuario.setFechaModificacion(null);
            perfilesXUsuario.setFechaInsercion(UtilidadesFecha.convertirDateAString(new Date()));
            perfilesXUsuario.setKeyUsuarioId(firebaseUser.getUid());
            insertarPerfilEmpleado(perfilesXUsuario);
            insertarPerfilAdministrador(perfilesXUsuario);
        }


    }
    private void insertarPerfilEmpleado(PerfilesXUsuario perfilesXUsuario){
        if(ckbxPerfilEmpleado.isChecked()){
            perfilesXUsuario.setActivo("S");

        }else{
            perfilesXUsuario.setActivo("N");
        }
        DatabaseReference databaseReference = firebaseDatabase.getReference(UtilidadesFirebaseBD.getUrlInserccionPerfilEmpleado(perfilesXUsuario.getKeyUsuarioId()));
        databaseReference.setValue(perfilesXUsuario);
    }
    private void insertarPerfilAdministrador(PerfilesXUsuario perfilesXUsuario){
        if(ckbxPerfilAdministrador.isChecked()){
            perfilesXUsuario.setActivo("S");

        }else{
            perfilesXUsuario.setActivo("N");
        }
        DatabaseReference databaseReference = firebaseDatabase.getReference(UtilidadesFirebaseBD.getUrlInserccionPerfilAdministrador(perfilesXUsuario.getKeyUsuarioId()));

        databaseReference.setValue(perfilesXUsuario);
    }
    public void cancelarEditarPerfilesXUsuario(View view){
        finish();
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
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        int item = menuItem.getItemId();
        if(AdministrarMiNegocioActivity.class.getName().equals(intent.getStringExtra(Constantes.CALL_FROM_ACTIVITY_ADMINISTRARMINEGOCIO))){
            if(item == android.R.id.home){
                onBackPressed();
            }
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
