package co.com.fredymosqueralemus.pelucitas;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CalendarView;

public class CalendarAgendaXEmpleadoActivity extends AppCompatActivity {
    private CalendarView calendarViewAgenda;
    private Intent intent;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_agenda_xempleado);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        intent = getIntent();
        context = this;
        calendarViewAgenda = (CalendarView) findViewById(R.id.calendar_agenda_CalendarAgendaXEmpleadoActivity);
        calendarViewAgenda.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Intent intentAgendaXdia = new Intent(context, ListaAgendaXDiaActivity.class);
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
