package co.com.fredymosqueralemus.pelucitas;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.vision.barcode.Barcode;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import co.com.fredymosqueralemus.pelucitas.adapters.AdapterAgendaXDia;
import co.com.fredymosqueralemus.pelucitas.constantes.Constantes;
import co.com.fredymosqueralemus.pelucitas.modelo.agenda.AgendaXEmpleado;
import co.com.fredymosqueralemus.pelucitas.modelo.minegocio.EmpleadosXNegocio;
import co.com.fredymosqueralemus.pelucitas.modelo.usuario.Usuario;
import co.com.fredymosqueralemus.pelucitas.utilidades.UtilidadesFecha;
import co.com.fredymosqueralemus.pelucitas.utilidades.UtilidadesFirebaseBD;

public class ListaAgendaXDiaActivity extends AppCompatActivity {
    private ProgressBar progressbarListaAgendaXDiaActivity;
    private TextView txtvMensajeNoagenda;
    private ListView listviewFragmentListaAgendaXDiaActivity;
    private Intent intent;
    private Context context;
    private Usuario usuario;
    private List<AgendaXEmpleado> lstAgendaXEmpleado;
    private AgendaXEmpleado agendaXEmpleado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_agenda_xdia);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        intent = getIntent();
        context = this;
        usuario = (Usuario) intent.getSerializableExtra(Constantes.USUARIO_OBJECT);
        getSupportActionBar().setTitle(getString(R.string.str_tituloactivitylistagenda) + " " + String.valueOf(intent.getSerializableExtra(Constantes.STR_DIA_AGENDA)));
        progressbarListaAgendaXDiaActivity = (ProgressBar) findViewById(R.id.progressbar_ListaAgendaXDiaActivity);
        txtvMensajeNoagenda = (TextView) findViewById(R.id.txtv_mensaje_noagenda_ListaAgendaXDiaActivity);
        listviewFragmentListaAgendaXDiaActivity = (ListView) findViewById(R.id.listview_fragment_ListaAgendaXDiaActivity);


        getAgendaXEmpleadoXDia();
        addOnclickListenerLisViewAgenda();
    }

    private void getAgendaXEmpleadoXDia() {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child(Constantes.AGENDA_X_EMPLEADOS).child(usuario.getUid()).child(String.valueOf(intent.getStringExtra(Constantes.STR_FECHA_AGENDA))).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lstAgendaXEmpleado = new ArrayList<AgendaXEmpleado>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    AgendaXEmpleado agendaXEmpleado = child.getValue(AgendaXEmpleado.class);
                    lstAgendaXEmpleado.add(agendaXEmpleado);
                }
                if (lstAgendaXEmpleado.isEmpty()) {
                    txtvMensajeNoagenda.setVisibility(View.VISIBLE);
                }
                AdapterAgendaXDia adapterAgendaXDia = new AdapterAgendaXDia(context, R.layout.layout_listview_agendaxdia, lstAgendaXEmpleado);
                listviewFragmentListaAgendaXDiaActivity.setAdapter(adapterAgendaXDia);
                progressbarListaAgendaXDiaActivity.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void addOnclickListenerLisViewAgenda(){
        if (callToReservaAgenda()) {
            listviewFragmentListaAgendaXDiaActivity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    final AgendaXEmpleado agendaXEmpleado = lstAgendaXEmpleado.get(position);
                    if (!Constantes.SI.equals(agendaXEmpleado.getSnReservado())) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setMessage("Desea Reservar agenda para "+ agendaXEmpleado.getHoraReserva()+"?");
                        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DatabaseReference databaseReferenceReservar = FirebaseDatabase.getInstance().getReference(UtilidadesFirebaseBD.getUrlInsercionAgendaXEmpleado(agendaXEmpleado));
                                databaseReferenceReservar.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        AgendaXEmpleado agendaReserva = dataSnapshot.getValue(AgendaXEmpleado.class);
                                        agendaReserva.setSnReservado(Constantes.SI);
                                        agendaReserva.setCedulaUsuarioReserva(usuario.getCedulaIdentificacion());
                                        agendaReserva.setUidUsuarioReserva(usuario.getUid());
                                        agendaReserva.setFechaModificacion(UtilidadesFecha.convertirDateAString(new Date(), Constantes.FORMAT_DDMMYYYYHHMMSS));
                                        UtilidadesFirebaseBD.insertarAgendaXEmpleadoFirebaseBD(agendaReserva);
                                        getAgendaXEmpleadoXDia();

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }
                        });

                        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();

                    }
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (callToAgregarAgendaEmpleado()) {
            MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.menu_agregar_agendaxdia, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int item = menuItem.getItemId();
        if (item == android.R.id.home) {
            onBackPressed();
        } else if (item == R.id.menuitem_agregar_agenda_xdia) {
            final Calendar calendar = Calendar.getInstance();
            final int hora = calendar.get(Calendar.HOUR_OF_DAY);
            final int minutos = calendar.get(Calendar.MINUTE);
            String strFechaAgenda = String.valueOf(intent.getStringExtra(Constantes.STR_FECHA_AGENDA));
            Date dateFechaAgenda = UtilidadesFecha.convertirStringADate(strFechaAgenda, Constantes.FORMAT_YYYYMMDD);
            Date dateFechaHoy = UtilidadesFecha.formatearDate(calendar.getTime(), Constantes.FORMAT_DDMMYYYY);

            if (!dateFechaAgenda.before(dateFechaHoy)) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        StringBuilder strbHoraAgenda = new StringBuilder();
                        strbHoraAgenda.append(hourOfDay);
                        strbHoraAgenda.append(":");
                        strbHoraAgenda.append(UtilidadesFecha.agregarCeroMinutosTimePicker(minute));

                        agendaXEmpleado = new AgendaXEmpleado();
                        agendaXEmpleado.setFechaAgenda(String.valueOf(intent.getStringExtra(Constantes.STR_FECHA_AGENDA)));
                        agendaXEmpleado.setHoraReserva(strbHoraAgenda.toString());
                        agendaXEmpleado.setSnReservado(Constantes.NO);
                        agendaXEmpleado.setUidEmpleado(usuario.getUid());
                        agendaXEmpleado.setFechaInsercion(UtilidadesFecha.convertirDateAString(calendar.getTime(), Constantes.FORMAT_DDMMYYYYHHMMSS));
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(UtilidadesFirebaseBD.getUrlInsercionAgendaXEmpleado(agendaXEmpleado));
                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                AgendaXEmpleado agendaFromFB = dataSnapshot.getValue(AgendaXEmpleado.class);
                                if (null == agendaFromFB) {
                                    UtilidadesFirebaseBD.insertarAgendaXEmpleadoFirebaseBD(agendaXEmpleado);
                                    getAgendaXEmpleadoXDia();
                                } else {
                                    Toast.makeText(context, "No se puede ingesar esta hora en la agenda, esta hora ya se inserto, intenta con otra", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        txtvMensajeNoagenda.setVisibility(View.GONE);

                    }
                }, hora, minutos, true);
                timePickerDialog.setTitle(getString(R.string.str_seleccionar_hora_agenda));
                timePickerDialog.show();
            } else {
                Toast.makeText(context, getString(R.string.str_no_se_puede_agregar_agenda_para_este_dia), Toast.LENGTH_SHORT).show();
            }

        }
        return super.onOptionsItemSelected(menuItem);
    }

    private boolean callToAgregarAgendaEmpleado() {
        return ConfiguracionActivity.class.getName().equals(intent.getStringExtra(Constantes.CALL_TO_AGREGAR_AGENDA_XEMPLEADO));
    }

    private boolean callToReservaAgenda() {
        return VerListaEmpleadosParaReservarActivity.class.getName().equals(intent.getStringExtra(Constantes.CALL_FROM_ACTIVITY_VER_PERFIL_PARA_AGENDAR));
    }
}
