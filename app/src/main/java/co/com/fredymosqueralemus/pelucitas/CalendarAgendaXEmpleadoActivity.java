package co.com.fredymosqueralemus.pelucitas;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CalendarView;

import co.com.fredymosqueralemus.pelucitas.constantes.Constantes;
import co.com.fredymosqueralemus.pelucitas.modelo.usuario.Usuario;

public class CalendarAgendaXEmpleadoActivity extends AppCompatActivity {
    private CalendarView calendarViewAgenda;
    private Intent intent;
    private Context context;
    private Usuario usuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_agenda_xempleado);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        intent = getIntent();
        context = this;
        usuario = (Usuario) intent.getSerializableExtra(Constantes.USUARIO_OBJECT);
        calendarViewAgenda = (CalendarView) findViewById(R.id.calendar_agenda_CalendarAgendaXEmpleadoActivity);
        calendarViewAgenda.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                StringBuilder strbFechaAgenda = new StringBuilder();
                strbFechaAgenda.append(dayOfMonth).append("/").append(month).append("/").append(year);
                Intent intentAgendaXdia = new Intent(context, ListaAgendaXDiaActivity.class);

                intentAgendaXdia.putExtra(Constantes.STR_DIA_AGENDA, strbFechaAgenda.toString());

                strbFechaAgenda = new StringBuilder();
                strbFechaAgenda.append(year).append("/").append(month).append("/").append(dayOfMonth);


                intentAgendaXdia.putExtra(Constantes.STR_FECHA_AGENDA, strbFechaAgenda.toString());
                intentAgendaXdia.putExtra(Constantes.USUARIO_OBJECT, usuario);

                intentAgendaXdia.putExtra(Constantes.CALL_TO_AGREGAR_AGENDA_XEMPLEADO, intent.getStringExtra(Constantes.CALL_TO_AGREGAR_AGENDA_XEMPLEADO));
                startActivity(intentAgendaXdia);
            }
        });
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
