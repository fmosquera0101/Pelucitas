package co.com.fredymosqueralemus.pelucitas;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.Date;

import co.com.fredymosqueralemus.pelucitas.constantes.Constantes;
import co.com.fredymosqueralemus.pelucitas.modelo.agenda.AgendaXEmpleado;
import co.com.fredymosqueralemus.pelucitas.modelo.agenda.TurnosXCliente;
import co.com.fredymosqueralemus.pelucitas.modelo.usuario.Usuario;
import co.com.fredymosqueralemus.pelucitas.utilidades.Utilidades;
import co.com.fredymosqueralemus.pelucitas.utilidades.UtilidadesFecha;
import co.com.fredymosqueralemus.pelucitas.utilidades.UtilidadesFirebaseBD;
import co.com.fredymosqueralemus.pelucitas.utilidades.UtilidadesImagenes;

public class TurnosActivity extends AppCompatActivity {

    private ImageView imgvImagenEmpleado;
    private TextView txtNombreEmpleado;
    private TextView txtTelefonoEmpleado;
    private TextView txtDireccionEmpleado;
    private TextView txtFechaTurno;
    private StorageReference storageReference;
    private Context context;
    private FirebaseAuth firebaseAuth;
    private TurnosXCliente turnosXCliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turnos);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        inicializarViewsActivityTurnos();
        storageReference = UtilidadesFirebaseBD.getFirebaseStorageFromUrl();
        Intent intent = getIntent();
        context = this;
        firebaseAuth = FirebaseAuth.getInstance();
        turnosXCliente = (TurnosXCliente) intent.getSerializableExtra(Constantes.TURNOSXCLIENTE_OBJ);
        consultarInformacionTurnoUsuario();
    }

    private void consultarInformacionTurnoUsuario() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(UtilidadesFirebaseBD.getUrlInserccionUsuario(turnosXCliente.getUidEmpleado()));
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.getValue(Usuario.class);
                settearInfoUsuarioViewsTurnosActivity(usuario);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void settearInfoUsuarioViewsTurnosActivity(Usuario usuario) {
        txtNombreEmpleado.setText(usuario.getNombre() + " " + usuario.getApellidos());
        txtTelefonoEmpleado.setText(usuario.getTelefono());
        txtDireccionEmpleado.setText(Utilidades.getStrDireccion(usuario.getDireccion()) + " " + usuario.getDireccion().getDatosAdicionales());
        txtFechaTurno.setText(context.getString(R.string.str_fechaturno) + ": " + turnosXCliente.getFechaTurno());
        UtilidadesImagenes.cargarImagenPerfilUsuario(imgvImagenEmpleado, turnosXCliente.getFechaActualizacionImagenUsuario(), turnosXCliente.getCedulaIdentificacionEmpleado(), context, storageReference);
    }

    private void inicializarViewsActivityTurnos() {
        imgvImagenEmpleado = (ImageView) findViewById(R.id.imagen_empleado_TurnosActivity);
        txtNombreEmpleado = (TextView) findViewById(R.id.nombreempleado_TurnosActivity);
        txtTelefonoEmpleado = (TextView) findViewById(R.id.telefono_empleado_TurnosActivity);
        txtDireccionEmpleado = (TextView) findViewById(R.id.direccion_empleado_TurnosActivity);
        txtFechaTurno = (TextView) findViewById(R.id.fechaturno_empleado_TurnosActivity);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int item = menuItem.getItemId();
        if (item == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void cancelarTurno(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("¿Desea cancelar este turno?");
        builder.setPositiveButton(getString(R.string.str_aceptar), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                String[] arrayFechaTurno = turnosXCliente.getFechaTurno().split(" ");
                AgendaXEmpleado agendaXEmpleado = new AgendaXEmpleado();
                agendaXEmpleado.setUidEmpleado(turnosXCliente.getUidEmpleado());
                agendaXEmpleado.setFechaAgenda(arrayFechaTurno[0]);
                agendaXEmpleado.setHoraReserva(arrayFechaTurno[1]);
                DatabaseReference dbrAgendaXEmpleado = FirebaseDatabase.getInstance().getReference(UtilidadesFirebaseBD.getUrlInsercionAgendaXEmpleado(agendaXEmpleado));
                dbrAgendaXEmpleado.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        AgendaXEmpleado agendaXEmpleado = dataSnapshot.getValue(AgendaXEmpleado.class);
                        agendaXEmpleado.setSnReservado(Constantes.NO);
                        agendaXEmpleado.setFechaModificacion(UtilidadesFecha.convertirDateAString(new Date(), Constantes.FORMAT_DDMMYYYYHHMMSS));
                        UtilidadesFirebaseBD.insertarAgendaXEmpleadoFirebaseBD(agendaXEmpleado);
                        DatabaseReference databaseReferenceTurnoxCliente = FirebaseDatabase.getInstance().getReference(UtilidadesFirebaseBD.getUrlInserccionTurnosXCliente(firebaseAuth.getCurrentUser().getUid()));
                        databaseReferenceTurnoxCliente.child(turnosXCliente.getPushKey()).setValue(null);
                        Toast.makeText(TurnosActivity.this, R.string.str_mensaje_turno_cancelado,
                                Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        });

        builder.setNegativeButton(getString(R.string.str_cancelar), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
