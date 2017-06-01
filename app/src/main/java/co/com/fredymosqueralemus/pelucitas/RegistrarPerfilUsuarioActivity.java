package co.com.fredymosqueralemus.pelucitas;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;

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

    private Usuario usuario;
    private Intent intent;
    private PerfilesXUsuario perfilesXUsuarioAdministrador;
    private PerfilesXUsuario perfilesXUsuarioEmpleado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_perfil_usuario);
        ckbxPerfilEmpleado = (CheckBox) findViewById(R.id.perfilempleado_chkbx_registrarperfillayout);
        ckbxPerfilAdministrador = (CheckBox) findViewById(R.id.perfiladministrador_chkbx_registrarperfillayout);
        linearLayoutEditarCanceraRegistroPerfil = (LinearLayout) findViewById(R.id.layout_editar_cancelar_registrarperfillayout);
        linearLayoutRegistrarPerfil = (LinearLayout) findViewById(R.id.layout_registrarpefil_registrarperfillayout);
        intent = getIntent();
        usuario = (Usuario) intent.getSerializableExtra(Constantes.USUARIO_OBJECT);

        if (AdministrarMiPerfilActivity.class.getName().equals(intent.getStringExtra(Constantes.CALL_FROM_ACTIVITY_ADMINISTRARMIPERFIL))) {
            getSupportActionBar().setTitle(getString(R.string.srt_ditarperfilusuario));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            linearLayoutEditarCanceraRegistroPerfil.setVisibility(View.VISIBLE);
            linearLayoutRegistrarPerfil.setVisibility(View.GONE);
            settearPerfiles(usuario);
        }

    }

    private void settearPerfiles(Usuario usuario) {

        perfilesXUsuarioAdministrador = usuario.getPerfilAdministrador();
        if ("S".equals(perfilesXUsuarioAdministrador.getActivo())) {
            ckbxPerfilAdministrador.setChecked(true);
        }

        perfilesXUsuarioEmpleado = usuario.getPerfilEmpleado();
        if ("S".equals(perfilesXUsuarioEmpleado.getActivo())) {
            ckbxPerfilEmpleado.setChecked(true);
        }

    }

    /**
     * Este metodo se ejecuta en respuesta al clic del boto siguiente de esta activity
     * Es el encargado de registrar la informacion de la direccion ingresada por el usuario
     *
     * @param view Created by Fredy Mosquera Lemus on 9/02/17.
     */
    public void registrarPerfilesXUsuario(View view) {
        guardarPerfilUsuario(usuario.getUid());
        abrirActivityHome();
    }

    public void editarPerfilesXUsuario(View view) {
        guardarPerfilUsuario(usuario.getUid());
        finish();
    }

    protected void guardarPerfilUsuario(String usuarioUid) {

        if (AdministrarMiPerfilActivity.class.getName().equals(intent.getStringExtra(Constantes.CALL_FROM_ACTIVITY_ADMINISTRARMIPERFIL))) {
            perfilesXUsuarioAdministrador.setFechaModificacion(UtilidadesFecha.convertirDateAString(new Date()));
            usuario.setPerfilAdministrador(perfilesXUsuarioAdministrador);
            perfilesXUsuarioEmpleado.setFechaModificacion(UtilidadesFecha.convertirDateAString(new Date()));
            usuario.setPerfilEmpleado(perfilesXUsuarioEmpleado);

            insertarPerfilEmpleado(perfilesXUsuarioEmpleado);
            insertarPerfilAdministrador(perfilesXUsuarioAdministrador);
        } else {
            PerfilesXUsuario perfilesXUsuario = new PerfilesXUsuario();
            perfilesXUsuario.setFechaModificacion(null);
            perfilesXUsuario.setFechaInsercion(UtilidadesFecha.convertirDateAString(new Date()));
            insertarPerfilEmpleado(perfilesXUsuario);
            insertarPerfilAdministrador(perfilesXUsuario);
        }


    }

    private void insertarPerfilEmpleado(PerfilesXUsuario perfilesXUsuario) {
        if (ckbxPerfilEmpleado.isChecked()) {
            perfilesXUsuario.setActivo("S");

        } else {
            perfilesXUsuario.setActivo("N");
        }
        usuario.setPerfilEmpleado(perfilesXUsuario);
        DatabaseReference databaseReference = getDatabaseReference(usuario.getUid());
        usuario.setFechaModificacion(UtilidadesFecha.convertirDateAString(new Date()));
        databaseReference.setValue(usuario);
    }

    private void insertarPerfilAdministrador(PerfilesXUsuario perfilesXUsuario) {
        if (ckbxPerfilAdministrador.isChecked()) {
            perfilesXUsuario.setActivo("S");

        } else {
            perfilesXUsuario.setActivo("N");
        }
        usuario.setPerfilAdministrador(perfilesXUsuario);
        DatabaseReference databaseReference = getDatabaseReference(usuario.getUid());
        usuario.setFechaModificacion(UtilidadesFecha.convertirDateAString(new Date()));
        databaseReference.setValue(usuario);
    }

    public void cancelarEditarPerfilesXUsuario(View view) {
        finish();
    }

    private void abrirActivityHome() {
        finish();
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int item = menuItem.getItemId();
        if (AdministrarMiNegocioActivity.class.getName().equals(intent.getStringExtra(Constantes.CALL_FROM_ACTIVITY_ADMINISTRARMINEGOCIO))) {
            if (item == android.R.id.home) {
                onBackPressed();
            }
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private DatabaseReference getDatabaseReference(String userId) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference(UtilidadesFirebaseBD.getUrlInserccionUsuario(userId));
        return databaseReference;
    }
}
