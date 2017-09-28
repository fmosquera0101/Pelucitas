package co.com.fredymosqueralemus.pelucitas;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import co.com.fredymosqueralemus.pelucitas.constantes.Constantes;
import co.com.fredymosqueralemus.pelucitas.modelo.agenda.TurnosXCliente;
import co.com.fredymosqueralemus.pelucitas.modelo.usuario.Usuario;
import co.com.fredymosqueralemus.pelucitas.utilidades.Utilidades;
import co.com.fredymosqueralemus.pelucitas.utilidades.UtilidadesFirebaseBD;
import co.com.fredymosqueralemus.pelucitas.utilidades.UtilidadesImagenes;

public class TurnosActivity extends AppCompatActivity {

    private ImageView imgvImagenEmpleado;
    private TextView txtNombreEmpleado;
    private TextView txtTelefonoEmpleado;
    private TextView txtDireccionEmpleado;
    private StorageReference storageReference;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turnos);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        imgvImagenEmpleado = (ImageView) findViewById(R.id.imagen_empleado_TurnosActivity);
        txtNombreEmpleado = (TextView) findViewById(R.id.nombreempleado_TurnosActivity);
        txtTelefonoEmpleado = (TextView) findViewById(R.id.telefono_empleado_TurnosActivity);
        txtDireccionEmpleado = (TextView) findViewById(R.id.direccion_empleado_TurnosActivity);
        storageReference = UtilidadesFirebaseBD.getFirebaseStorageFromUrl();
        Intent intent = getIntent();
        context = this;
        final TurnosXCliente turnosXCliente = (TurnosXCliente) intent.getSerializableExtra(Constantes.TURNOSXCLIENTE_OBJ);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(UtilidadesFirebaseBD.getUrlInserccionUsuario(turnosXCliente.getUidEmpleado()));
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.getValue(Usuario.class);
                txtNombreEmpleado.setText(usuario.getNombre() +" "+usuario.getApellidos());
                txtTelefonoEmpleado.setText(usuario.getTelefono());
                txtDireccionEmpleado.setText(Utilidades.getStrDireccion(usuario.getDireccion()) + " "+ usuario.getDireccion().getDatosAdicionales());
                UtilidadesImagenes.cargarImagenPerfilUsuario(imgvImagenEmpleado, turnosXCliente.getFechaActualizacionImagenUsuario(), turnosXCliente.getCedulaIdentificacionEmpleado(), context, storageReference);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
