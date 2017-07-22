package co.com.fredymosqueralemus.pelucitas;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CalendarView;

import java.util.Calendar;

import co.com.fredymosqueralemus.pelucitas.constantes.Constantes;
import co.com.fredymosqueralemus.pelucitas.modelo.usuario.Usuario;
import co.com.fredymosqueralemus.pelucitas.utilidades.UtilidadesFecha;

public class CalendarAgendaXEmpleadoActivity extends AppCompatActivity {
    private Intent intent;
    private Context context;
    private Usuario usuario;
    private Calendar calendar;
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
        calendarViewAgenda.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                Intent intentAgendaXdia = getIntentAgendaXdia(intent);
                startActivity(intentAgendaXdia);
            }
        });
    }
    private Intent getIntentAgendaXdia(Intent intent){
        Intent intentAgendaXdia = new Intent(context, ListaAgendaXDiaActivity.class);
        intentAgendaXdia.putExtra(Constantes.STR_DIA_AGENDA, UtilidadesFecha.convertirDateAString(calendar.getTime(), Constantes.FORMAT_DDMMYYYY));
        intentAgendaXdia.putExtra(Constantes.STR_FECHA_AGENDA, UtilidadesFecha.convertirDateAString(calendar.getTime(), Constantes.FORMAT_YYYYMMDD));
        intentAgendaXdia.putExtra(Constantes.USUARIO_OBJECT, usuario);
        intentAgendaXdia.putExtra(Constantes.CALL_TO_AGREGAR_AGENDA_XEMPLEADO, intent.getStringExtra(Constantes.CALL_TO_AGREGAR_AGENDA_XEMPLEADO));
        intentAgendaXdia.putExtra(Constantes.CALL_FROM_ACTIVITY_VER_PERFIL_PARA_AGENDAR, intent.getStringExtra(Constantes.CALL_FROM_ACTIVITY_VER_PERFIL_PARA_AGENDAR));
        intentAgendaXdia.putExtra(Constantes.CALL_FROM_ACTIVITY_ADMINISTRARMIPERFIL, intent.getStringExtra(Constantes.CALL_FROM_ACTIVITY_ADMINISTRARMIPERFIL));
        return  intentAgendaXdia;

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int item = menuItem.getItemId();
        if(item == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
