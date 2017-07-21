package co.com.fredymosqueralemus.pelucitas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import co.com.fredymosqueralemus.pelucitas.constantes.Constantes;
import co.com.fredymosqueralemus.pelucitas.modelo.usuario.Usuario;
import co.com.fredymosqueralemus.pelucitas.utilidades.UtilidadesFirebaseBD;
import co.com.fredymosqueralemus.pelucitas.utilidades.UtilidadesImagenes;

public class VerPerfilEmpleadoParaAgendarActivity extends AppCompatActivity {

    private ImageView imgvImagenPerfilEmpleado;
    private TextView txtvNombreEmpleado;
    private TextView txtvTelefonoEmpleado;
    private TextView txtvCorreoEmpleado;

    private Intent intent;
    private Usuario usuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_perfil_empleado_para_agendar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        imgvImagenPerfilEmpleado = (ImageView) findViewById(R.id.imagenmiperfil_VerPerfilEmpleadoParaAgendarActivity);
        txtvNombreEmpleado = (TextView) findViewById(R.id.nombreusuario_VerPerfilEmpleadoParaAgendarActivity);
        txtvTelefonoEmpleado = (TextView) findViewById(R.id.telefono_VerPerfilEmpleadoParaAgendarActivity);
        txtvCorreoEmpleado = (TextView) findViewById(R.id.correo_VerPerfilEmpleadoParaAgendarActivity);

        intent = getIntent();
        usuario = (Usuario) intent.getSerializableExtra(Constantes.USUARIO_OBJECT);
        UtilidadesImagenes.cargarImagenPerfilUsuario(imgvImagenPerfilEmpleado, usuario, this, UtilidadesFirebaseBD.getFirebaseStorageFromUrl());
        txtvNombreEmpleado.setText(usuario.getNombre());
        txtvTelefonoEmpleado.setText(usuario.getTelefono());
        txtvCorreoEmpleado.setText(usuario.getEmail());
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int item = menuItem.getItemId();
        if(item == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }
    public void verAgendaEmpleado(View view){
        Intent intent = new Intent(this, CalendarAgendaXEmpleadoActivity.class);
        intent.putExtra(Constantes.USUARIO_OBJECT, usuario);
        intent.putExtra(Constantes.CALL_FROM_ACTIVITY_VER_PERFIL_PARA_AGENDAR, VerPerfilEmpleadoParaAgendarActivity.class.getName());
        startActivity(intent);
    }
}
