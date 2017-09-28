package co.com.fredymosqueralemus.pelucitas;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;

import com.google.firebase.storage.StorageReference;

import co.com.fredymosqueralemus.pelucitas.constantes.Constantes;
import co.com.fredymosqueralemus.pelucitas.modelo.agenda.TurnosXCliente;
import co.com.fredymosqueralemus.pelucitas.utilidades.UtilidadesFirebaseBD;
import co.com.fredymosqueralemus.pelucitas.utilidades.UtilidadesImagenes;

public class TurnosActivity extends AppCompatActivity {

    private ImageView imgvImagenEmpleado;
    private StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turnos);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        imgvImagenEmpleado = (ImageView) findViewById(R.id.imagen_empleado_TurnosActivity);
        storageReference = UtilidadesFirebaseBD.getFirebaseStorageFromUrl();
        Intent intent = getIntent();
        TurnosXCliente turnosXCliente = (TurnosXCliente) intent.getSerializableExtra(Constantes.TURNOSXCLIENTE_OBJ);
        UtilidadesImagenes.cargarImagenPerfilUsuario(imgvImagenEmpleado, turnosXCliente.getFechaActualizacionImagenUsuario(), turnosXCliente.getCedulaIdentificacionEmpleado(), this, storageReference);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int item = menuItem.getItemId();
        if (item == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
