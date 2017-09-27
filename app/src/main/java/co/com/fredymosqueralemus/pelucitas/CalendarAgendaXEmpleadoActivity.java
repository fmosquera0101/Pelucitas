package co.com.fredymosqueralemus.pelucitas;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.com.fredymosqueralemus.pelucitas.adapters.AdapterAgendaXDia;
import co.com.fredymosqueralemus.pelucitas.constantes.Constantes;
import co.com.fredymosqueralemus.pelucitas.modelo.agenda.AgendaXEmpleado;
import co.com.fredymosqueralemus.pelucitas.modelo.agenda.TurnosXCliente;
import co.com.fredymosqueralemus.pelucitas.modelo.reserva.NotificacionReservaXUsuario;
import co.com.fredymosqueralemus.pelucitas.modelo.usuario.Usuario;
import co.com.fredymosqueralemus.pelucitas.services.NotificadorReservaAgendaService;
import co.com.fredymosqueralemus.pelucitas.utilidades.UtilidadesFecha;
import co.com.fredymosqueralemus.pelucitas.utilidades.UtilidadesFirebaseBD;

public class CalendarAgendaXEmpleadoActivity extends AppCompatActivity {
    private Intent intent;
    private Context context;
    private Usuario usuario;
    private Calendar calendar;

    private ProgressBar progressbarListaAgendaXDiaActivity;
    private TextView txtvMensajeNoagenda;
    private ListView listviewFragmentListaAgendaXDiaActivity;
    private Usuario empleado;
    private List<AgendaXEmpleado> lstAgendaXEmpleado;
    private AgendaXEmpleado agendaXEmpleado;
    private long childreCount = 0;
    private long cantidadChildren = 0;
    private FirebaseAuth firebaseAuth;
    private String strFechaAgenda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_agenda_xempleado);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        intent = getIntent();
        context = this;
        usuario = (Usuario) intent.getSerializableExtra(Constantes.USUARIO_OBJECT);
        CalendarView calendarViewAgenda = (CalendarView) findViewById(R.id.calendar_agenda_CalendarAgendaXEmpleadoActivity);
        calendar = Calendar.getInstance();


        firebaseAuth = FirebaseAuth.getInstance();
        empleado = usuario;
        //getSupportActionBar().setTitle(getString(R.string.str_tituloactivitylistagenda) + " " + String.valueOf(intent.getSerializableExtra(Constantes.STR_DIA_AGENDA)));
        progressbarListaAgendaXDiaActivity = (ProgressBar) findViewById(R.id.progressbar_ListaAgendaXDiaActivity);
        txtvMensajeNoagenda = (TextView) findViewById(R.id.txtv_mensaje_noagenda_ListaAgendaXDiaActivity);
        listviewFragmentListaAgendaXDiaActivity = (ListView) findViewById(R.id.listview_fragment_ListaAgendaXDiaActivity);
        strFechaAgenda = UtilidadesFecha.convertirDateAString(calendar.getTime(), Constantes.FORMAT_YYYYMMDD);
        if (callToAdministrarAgenda()) {
            getAgendaXEmpleadoParaAdministrar(strFechaAgenda);
            setOnItemLongClickListenerLisView();
        } else if (callToReservaAgenda()) {
            getAgendaXEmpleadoParaReservar(strFechaAgenda);
            addOnclickListenerLisViewAgenda();
        }

        calendarViewAgenda.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                //Intent intentAgendaXdia = getIntentAgendaXdia(intent);
                //startActivity(intentAgendaXdia);

                strFechaAgenda = UtilidadesFecha.convertirDateAString(calendar.getTime(), Constantes.FORMAT_YYYYMMDD);
                if (callToAdministrarAgenda()) {
                    getAgendaXEmpleadoParaAdministrar(strFechaAgenda);
                    setOnItemLongClickListenerLisView();
                } else if (callToReservaAgenda()) {
                    getAgendaXEmpleadoParaReservar(strFechaAgenda);
                    addOnclickListenerLisViewAgenda();
                }
            }
        });
    }

    private Intent getIntentAgendaXdia(Intent intent) {
        Intent intentAgendaXdia = new Intent(context, ListaAgendaXDiaActivity.class);
        intentAgendaXdia.putExtra(Constantes.STR_DIA_AGENDA, UtilidadesFecha.convertirDateAString(calendar.getTime(), Constantes.FORMAT_DDMMYYYY));
        intentAgendaXdia.putExtra(Constantes.STR_FECHA_AGENDA, UtilidadesFecha.convertirDateAString(calendar.getTime(), Constantes.FORMAT_YYYYMMDD));
        intentAgendaXdia.putExtra(Constantes.USUARIO_OBJECT, usuario);
        intentAgendaXdia.putExtra(Constantes.CALL_TO_AGREGAR_AGENDA_XEMPLEADO, intent.getStringExtra(Constantes.CALL_TO_AGREGAR_AGENDA_XEMPLEADO));
        intentAgendaXdia.putExtra(Constantes.CALL_FROM_ACTIVITY_VER_PERFIL_PARA_AGENDAR, intent.getStringExtra(Constantes.CALL_FROM_ACTIVITY_VER_PERFIL_PARA_AGENDAR));
        intentAgendaXdia.putExtra(Constantes.CALL_FROM_ACTIVITY_ADMINISTRARMIPERFIL, intent.getStringExtra(Constantes.CALL_FROM_ACTIVITY_ADMINISTRARMIPERFIL));
        return intentAgendaXdia;

    }
//    @Override
//    public boolean onOptionsItemSelected(MenuItem menuItem) {
//        int item = menuItem.getItemId();
//        if(item == android.R.id.home){
//            onBackPressed();
//        }
//        return super.onOptionsItemSelected(menuItem);
//    }


    //
    private void getAgendaXEmpleadoParaReservar(String strFechaReserva) {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child(Constantes.AGENDA_X_EMPLEADOS).child(empleado.getUid()).child(strFechaReserva).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lstAgendaXEmpleado = new ArrayList<AgendaXEmpleado>();
                childreCount = dataSnapshot.getChildrenCount();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    AgendaXEmpleado agendaXEmpleado = child.getValue(AgendaXEmpleado.class);
                    agendaXEmpleado.setPuedeVerimagenReservaAgenda(false);
                    lstAgendaXEmpleado.add(agendaXEmpleado);

                }

                if (lstAgendaXEmpleado.isEmpty()) {
                    txtvMensajeNoagenda.setVisibility(View.VISIBLE);
                    progressbarListaAgendaXDiaActivity.setVisibility(View.GONE);
                    listviewFragmentListaAgendaXDiaActivity.setVisibility(View.GONE);
                } else {
                    addAdapterAgendaXDia(lstAgendaXEmpleado);
                    txtvMensajeNoagenda.setVisibility(View.GONE);
                    progressbarListaAgendaXDiaActivity.setVisibility(View.GONE);
                    listviewFragmentListaAgendaXDiaActivity.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void getAgendaXEmpleadoParaAdministrar(String strFechaReserva) {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child(Constantes.AGENDA_X_EMPLEADOS).child(empleado.getUid()).child(strFechaReserva).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lstAgendaXEmpleado = new ArrayList<AgendaXEmpleado>();
                childreCount = dataSnapshot.getChildrenCount();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    AgendaXEmpleado agendaXEmpleado = child.getValue(AgendaXEmpleado.class);
                    agendaXEmpleado.setPuedeVerimagenReservaAgenda(true);
                    getReservadoPorAgendaEmpleado(agendaXEmpleado);
                }
                if (childreCount == 0) {
                    txtvMensajeNoagenda.setVisibility(View.VISIBLE);
                    progressbarListaAgendaXDiaActivity.setVisibility(View.GONE);
                    listviewFragmentListaAgendaXDiaActivity.setVisibility(View.GONE);
                } else {
                    txtvMensajeNoagenda.setVisibility(View.GONE);
                    progressbarListaAgendaXDiaActivity.setVisibility(View.GONE);
                    listviewFragmentListaAgendaXDiaActivity.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void addOnclickListenerLisViewAgenda() {

        listviewFragmentListaAgendaXDiaActivity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final AgendaXEmpleado agendaXEmpleado = lstAgendaXEmpleado.get(position);
                if (!Constantes.SI.equals(agendaXEmpleado.getSnReservado())) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Desea Reservar agenda para " + agendaXEmpleado.getHoraReserva() + "?");
                    builder.setPositiveButton(getString(R.string.str_aceptar), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            DatabaseReference databaseReferenceReservar = FirebaseDatabase.getInstance().getReference(UtilidadesFirebaseBD.getUrlInsercionAgendaXEmpleado(agendaXEmpleado));
                            databaseReferenceReservar.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    AgendaXEmpleado agendaReserva = dataSnapshot.getValue(AgendaXEmpleado.class);
                                    agendaReserva.setSnReservado(Constantes.SI);
                                    agendaReserva.setCedulaUsuarioReserva(empleado.getCedulaIdentificacion());
                                    agendaReserva.setUidUsuarioReserva(firebaseAuth.getCurrentUser().getUid());
                                    agendaReserva.setFechaModificacion(UtilidadesFecha.convertirDateAString(new Date(), Constantes.FORMAT_DDMMYYYYHHMMSS));
                                    UtilidadesFirebaseBD.insertarAgendaXEmpleadoFirebaseBD(agendaReserva);
                                    getAgendaXEmpleadoParaReservar(agendaReserva.getFechaAgenda());

                                    TurnosXCliente turnosXCliente = new TurnosXCliente();
                                    turnosXCliente.setNombreEmpleado(empleado.getNombre());
                                    turnosXCliente.setUidEmpleado(empleado.getUid());
                                    turnosXCliente.setHoraTurno(agendaReserva.getHoraReserva());

                                    turnosXCliente.setNombreNegocio("");
                                    turnosXCliente.setDireccionNegocio("");
                                    turnosXCliente.setTelefonoNegocio("");
                                    DatabaseReference databaseReferenceInsertarTurno = FirebaseDatabase.getInstance().getReference(UtilidadesFirebaseBD.getUrlInserccionTurnosXCliente(firebaseAuth.getCurrentUser().getUid()));
                                    databaseReferenceInsertarTurno.push().setValue(turnosXCliente);




                                    DatabaseReference dbr = FirebaseDatabase.getInstance().getReference(UtilidadesFirebaseBD.getUrlInserccionNofiticaciones(agendaReserva.getUidEmpleado()));
                                    String pushKey = dbr.push().getKey();

                                    NotificacionReservaXUsuario notificacionReservaXUsuario = new NotificacionReservaXUsuario();
                                    notificacionReservaXUsuario.setUidUsuarioReserva(firebaseAuth.getCurrentUser().getUid());
                                    notificacionReservaXUsuario.setFechaAgenda(agendaReserva.getFechaAgenda());
                                    notificacionReservaXUsuario.setFechaInsercion(UtilidadesFecha.convertirDateAString(new Date(), Constantes.FORMAT_DDMMYYYYHHMMSS));
                                    notificacionReservaXUsuario.setKeyUidHoraAgenda(agendaReserva.getHoraReserva());
                                    notificacionReservaXUsuario.setKeyUidEmpleadoDuenoAgenda(agendaXEmpleado.getUidEmpleado());

                                    Map<String, Object> mapNotificaciones = notificacionReservaXUsuario.toMap();
                                    Map<String, Object> childActualizaciones = new HashMap<String, Object>();
                                    childActualizaciones.put(pushKey, mapNotificaciones);
                                    dbr.setPriority(ServerValue.TIMESTAMP);
                                    dbr.updateChildren(childActualizaciones, new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                            if (null == databaseError) {

                                            }
                                        }
                                    });


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
        });

    }

    private void setOnItemLongClickListenerLisView() {
        listviewFragmentListaAgendaXDiaActivity.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                final AgendaXEmpleado agendaXEmpleado = lstAgendaXEmpleado.get(position);
                if (!Constantes.SI.equals(agendaXEmpleado.getSnReservado())) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Desea eliminar la siguente agenda: " + agendaXEmpleado.getHoraReserva() + "?");
                    builder.setPositiveButton(getString(R.string.str_aceptar), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            DatabaseReference databaseReferenceReservar = FirebaseDatabase.getInstance().getReference(UtilidadesFirebaseBD.getUrlInsercionAgendaXEmpleado(agendaXEmpleado));
                            databaseReferenceReservar.setValue(null);
                            lstAgendaXEmpleado.remove(position);
                            addAdapterAgendaXDia(lstAgendaXEmpleado);
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


                return false;
            }
        });

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
            Date dateFechaAgenda = UtilidadesFecha.convertirStringADate(strFechaAgenda, Constantes.FORMAT_YYYYMMDD);
            Date dateFechaHoy = UtilidadesFecha.formatearDate(calendar.getTime(), Constantes.FORMAT_DDMMYYYY);

            if (!dateFechaAgenda.before(dateFechaHoy)) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        agendaXEmpleado = getAgendaXEmpleadoFromDatePicker(hourOfDay, minute, calendar);
                        getAgendaXEmpleadoParavalidarAgendaIngresada(agendaXEmpleado);
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

    private void getAgendaXEmpleadoParavalidarAgendaIngresada(final AgendaXEmpleado agendaXEmpleado) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(UtilidadesFirebaseBD.getUrlInsercionAgendaXEmpleado(agendaXEmpleado));
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                AgendaXEmpleado agendaFromFB = dataSnapshot.getValue(AgendaXEmpleado.class);
                if (null == agendaFromFB) {
                    UtilidadesFirebaseBD.insertarAgendaXEmpleadoFirebaseBD(agendaXEmpleado);
                    getAgendaXEmpleadoParaAdministrar(agendaXEmpleado.getFechaAgenda());
                    txtvMensajeNoagenda.setVisibility(View.GONE);
                    lstAgendaXEmpleado.add(agendaXEmpleado);
                    addAdapterAgendaXDia(lstAgendaXEmpleado);

                } else {
                    Toast.makeText(context, getString(R.string.str_mensaje_error_ingresarhoraagenda), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getReservadoPorAgendaEmpleado(final AgendaXEmpleado agendaXEmpleado) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(UtilidadesFirebaseBD.getUrlInserccionUsuario(agendaXEmpleado.getUidUsuarioReserva()));
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.getValue(Usuario.class);
                if (null != usuario) {
                    agendaXEmpleado.setReservadoPor(getReservadoPor(usuario));
                    agendaXEmpleado.setUsuario(usuario);
                }
                lstAgendaXEmpleado.add(agendaXEmpleado);
                if (childreCount == cantidadChildren) {
                    addAdapterAgendaXDia(lstAgendaXEmpleado);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
        cantidadChildren++;
    }

    private void addAdapterAgendaXDia(List<AgendaXEmpleado> lstAgendaXEmpleado) {
        AdapterAgendaXDia adapterAgendaXDia = new AdapterAgendaXDia(context, R.layout.layout_listview_agendaxdia, lstAgendaXEmpleado);
        listviewFragmentListaAgendaXDiaActivity.setAdapter(adapterAgendaXDia);

    }

    private AgendaXEmpleado getAgendaXEmpleadoFromDatePicker(int hourOfDay, int minute, Calendar calendar) {
        AgendaXEmpleado agendaXEmpleado = new AgendaXEmpleado();
        agendaXEmpleado.setFechaAgenda(strFechaAgenda);
        agendaXEmpleado.setHoraReserva(getHoraReserva(hourOfDay, minute));
        agendaXEmpleado.setSnReservado(Constantes.NO);
        agendaXEmpleado.setUidEmpleado(empleado.getUid());
        agendaXEmpleado.setFechaInsercion(UtilidadesFecha.convertirDateAString(calendar.getTime(), Constantes.FORMAT_DDMMYYYYHHMMSS));
        return agendaXEmpleado;

    }

    private String getHoraReserva(int hourOfDay, int minute) {
        StringBuilder strbHoraAgenda = new StringBuilder();
        strbHoraAgenda.append(hourOfDay);
        strbHoraAgenda.append(":");
        strbHoraAgenda.append(UtilidadesFecha.agregarCeroMinutosTimePicker(minute));
        return strbHoraAgenda.toString();
    }

    private String getReservadoPor(Usuario usuario) {
        StringBuilder strbReservado = new StringBuilder();
        strbReservado.append(context.getString(R.string.str_reservado)).append(" por: ");
        strbReservado.append(usuario.getNombre()).append(" ").append(usuario.getApellidos());
        strbReservado.append(", ").append(context.getString(R.string.str_telefono)).append(": ");
        strbReservado.append(usuario.getTelefono());
        return strbReservado.toString();
    }

    private boolean callToAgregarAgendaEmpleado() {
        return ConfiguracionActivity.class.getName().equals(intent.getStringExtra(Constantes.CALL_TO_AGREGAR_AGENDA_XEMPLEADO));
    }

    private boolean callToReservaAgenda() {
        return VerListaEmpleadosParaReservarActivity.class.getName().equals(intent.getStringExtra(Constantes.CALL_FROM_ACTIVITY_VER_PERFIL_PARA_AGENDAR));
    }

    private boolean callToAdministrarAgenda() {
        return AdministrarMiPerfilActivity.class.getName().equals(intent.getStringExtra(Constantes.CALL_FROM_ACTIVITY_ADMINISTRARMIPERFIL))
                || NotificadorReservaAgendaService.class.getName().equals(intent.getStringExtra(Constantes.CALL_FROM_NOTIFICADORRESERVAAGENDA));
    }
}
