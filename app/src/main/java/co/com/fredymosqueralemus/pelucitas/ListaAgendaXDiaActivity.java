package co.com.fredymosqueralemus.pelucitas;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class ListaAgendaXDiaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_agenda_xdia);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
