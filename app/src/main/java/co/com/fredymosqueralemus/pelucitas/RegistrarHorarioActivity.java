package co.com.fredymosqueralemus.pelucitas;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import co.com.fredymosqueralemus.pelucitas.horario.Horario;

public class RegistrarHorarioActivity extends AppCompatActivity {

    Intent mItent;
    private EditText etHoraInicio;
    private EditText etHoraFin;
    private Context context;

    private CheckBox chkbLunes;
    private CheckBox chkbMartes;
    private CheckBox chkbMiercoles;
    private CheckBox chkbJueves;
    private CheckBox chkbViernes;
    private CheckBox chkbSabado;
    private CheckBox chkbDomingo;

    private String strLunes = "";
    private String strMartes = "";
    private String strMiercoles = "";
    private String strJueves = "";
    private String strViernes = "";
    private String strSabado = "";
    private String strDomingo = "";

    private Horario horarioNegocio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_horario);
        mItent = getIntent();
        inicializarViesCheckBoxDias();

    }
    public void registrarHorario(View view){

    }
    private void inicializarViesCheckBoxDias(){
        chkbLunes = (CheckBox) findViewById(R.id.chkb_dia_lunes);
        chkbMartes = (CheckBox) findViewById(R.id.chkb_dia_martes);
        chkbMiercoles = (CheckBox) findViewById(R.id.chkb_dia_miercoles);
        chkbJueves = (CheckBox) findViewById(R.id.chkb_dia_jueves);
        chkbViernes = (CheckBox) findViewById(R.id.chkb_dia_viernes);
        chkbSabado = (CheckBox) findViewById(R.id.chkb_dia_sabado);
        chkbDomingo = (CheckBox) findViewById(R.id.chkb_dia_domingo);
    }

}
