package co.com.fredymosqueralemus.pelucitas.horario;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import co.com.fredymosqueralemus.pelucitas.R;
import co.com.fredymosqueralemus.pelucitas.constantes.Constantes;
import co.com.fredymosqueralemus.pelucitas.utilidades.UtilidadesFecha;

/**
 * Created by Fredy Mosquera Lemus on 14/02/17.
 */

public class FormularioRegistrarHorario{

    private Context context;
    private Activity activity;
    private EditText etHoraInicio;
    private EditText etHoraFin;
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

    public FormularioRegistrarHorario(Context context, Activity activity){
        this.context = context;
        this.activity = activity;
        this.etHoraInicio =(EditText) activity.findViewById(R.id.hora_inicio_horari_entidad);
        this.etHoraFin = (EditText) activity.findViewById(R.id.hora_fin_horari_entidad);
        this.chkbLunes = (CheckBox) activity.findViewById(R.id.chkb_dia_lunes);
        this.chkbMartes = (CheckBox) activity.findViewById(R.id.chkb_dia_martes);
        this.chkbMiercoles = (CheckBox) activity.findViewById(R.id.chkb_dia_miercoles);
        this.chkbJueves = (CheckBox) activity.findViewById(R.id.chkb_dia_jueves);
        this.chkbViernes = (CheckBox) activity.findViewById(R.id.chkb_dia_viernes);
        this.chkbSabado = (CheckBox) activity.findViewById(R.id.chkb_dia_sabado);
        this.chkbDomingo = (CheckBox) activity.findViewById(R.id.chkb_dia_domingo);


    }
    /**
     * Metodo encargado de agregar click listener a los checkboxes dias
     * @Autor Fredy Mosquera Lemus
     * @Fecha 14/02/2016
     */
    private void addClickListenerCheckBoxLunes(){
        chkbLunes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((((CheckBox) v).isChecked())) {
                    strLunes = getString(R.string.label_lunes);
                } else {
                    strLunes = "";
                }
            }
        });
    }
    /**
     * Metodo encargado de agregar click listener a los checkboxes dias
     * @Autor Fredy Mosquera Lemus
     * @Fecha 14/02/2016
     */
    private void addClickListenerCheckBoxMartes(){
        chkbMartes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((((CheckBox) v).isChecked())) {
                    strMartes = getString(R.string.label_martes);
                } else {
                    strMartes = "";
                }
            }
        });
    }
    /**
     * Metodo encargado de agregar click listener a los checkboxes dias
     * @Autor Fredy Mosquera Lemus
     * @Fecha 14/02/2016
     */
    private void addClickListenerCheckBoxMiercoles(){
        chkbMiercoles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((((CheckBox) v).isChecked())) {
                    strMiercoles = getString(R.string.label_miercoles);
                } else {
                    strMiercoles = "";
                }
            }
        });
    }
    /**
     * Metodo encargado de agregar click listener a los checkboxes dias
     * @Autor Fredy Mosquera Lemus
     * @Fecha 14/02/2016
     */
    private void addClickListenerCheckBoxJueves(){
        chkbJueves.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((((CheckBox) v).isChecked())) {
                    strJueves = getString(R.string.label_jueves);
                } else {
                    strJueves = "";
                }
            }
        });
    }
    /**
     * Metodo encargado de agregar click listener a los checkboxes dias
     * @Autor Fredy Mosquera Lemus
     * @Fecha 14/02/2016
     */
    private void addClickListenerCheckBoxViernes(){
        chkbViernes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((((CheckBox) v).isChecked())) {
                    strViernes = getString(R.string.label_viernes);
                } else {
                    strViernes = "";
                }
            }
        });
    }

    /**
     * Metodo encargado de agregar click listener a los checkboxes dias
     * @Autor Fredy Mosquera Lemus
     * @Fecha 14/02/2016
     */
    private void addClickListenerCheckBoxSabado(){
        chkbSabado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((((CheckBox) v).isChecked())) {
                    strSabado = getString(R.string.label_sabado);
                } else {
                    strSabado = "";
                }
            }
        });
    }

    /**
     * Metodo encargado de agregar click listener a los checkboxes dias
     * @Autor Fredy Mosquera Lemus
     * @Fecha 14/02/2016
     */
    private void addClickListenerCheckBoxDomingo(){
        chkbDomingo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((((CheckBox) v).isChecked())) {
                    strDomingo = getString(R.string.label_domingo);
                } else {
                    strDomingo = "";
                }
            }
        });
    }

    private String getString(int resourceId){
        return activity.getString(resourceId);
    }
    /**
     * Metodo encargado de inicializar el timepicker para seleccionar la hora inicio y de cierre
     * @Autor Fredy Mosquera Lemus
     * @Fecha 14/02/2016
     */
    private void inicializarTimePickerFechaInicioFechaFin(){

        etHoraInicio.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    getHoraInicio();

                }
            }
        });

        etHoraFin.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    getHoraCierre();

                }
            }
        });


    }

    /**
     * Metodo encargado de obtener la hora de inicio desde timepicker
     * @Autor Fredy Mosquera Lemus
     * @Fecha 14/02/2016
     */
    private void getHoraInicio(){
        Calendar calendar = Calendar.getInstance();
        int hora = calendar.get(Calendar.HOUR_OF_DAY);
        int minuto = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog;
        timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                etHoraInicio.setText(String.valueOf(hourOfDay) + ":" + UtilidadesFecha.agregarCeroMinutosTimePicker(minute));
            }
        }, hora, minuto, true);
        timePickerDialog.setTitle(getString(R.string.titulo_horarioPicker));
        timePickerDialog.show();
    }
    /**
     * Metodo encargado de obtener la hora de cierre desde timepicker
     * @Autor Fredy Mosquera Lemus
     * @Fecha 14/02/2016
     */
    private void getHoraCierre(){
        Calendar calendar = Calendar.getInstance();
        int hora = calendar.get(Calendar.HOUR_OF_DAY);
        int minuto = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog;
        timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                etHoraFin.setText(String.valueOf(hourOfDay) + ":" + UtilidadesFecha.agregarCeroMinutosTimePicker(minute));
            }
        }, hora, minuto, true);
        timePickerDialog.setTitle(getString(R.string.titulo_horarioPicker));
        timePickerDialog.show();
    }
    /**
     * Metodo encargado de agregar click listener a los checkboxes dias
     * @Autor Fredy Mosquera Lemus
     * @Fecha 14/02/2016
     */
    public void addClickListenerCheckBoxesDias(){
        addClickListenerCheckBoxLunes();
        addClickListenerCheckBoxMartes();
        addClickListenerCheckBoxMiercoles();
        addClickListenerCheckBoxJueves();
        addClickListenerCheckBoxViernes();
        addClickListenerCheckBoxSabado();
        addClickListenerCheckBoxDomingo();
        inicializarTimePickerFechaInicioFechaFin();
    }

    /**
     * Metodo encargado de obtener el horario
     * @return
     * @Autor Fredy Mosquera Lemus
     * @Fecha 14/02/2016
     */
    public Horario getHorario(){
        Horario horario = new Horario();
        horario.setDiasLaborales(getDias().trim());
        horario.setHoraInicial(etHoraInicio.getText().toString());
        horario.setHoraFinal(etHoraFin.getText().toString());
        horario.setFechaInsercion(UtilidadesFecha.convertirDateAString(new Date(), Constantes.FORMAT_DDMMYYYYHHMMSS));
        horario.setFechaModificacion(null);
        return horario;
    }
    /**
     * Metodo encargado de obtener los dias concatenados
     * @return
     * @Autor Fredy Mosquera Lemus
     * @Fecha 14/02/2016
     */
    private String getDias(){
        StringBuilder strDias = new StringBuilder();
        if (!"".equals(strLunes)) {
            strDias.append(strLunes);
        }
        if (!"".equals(strMartes)) {
            strDias.append(" ");
            strDias.append(strMartes);

        }
        if (!"".equals(strMiercoles)) {
            strDias.append(" ");
            strDias.append(strMiercoles);

        }
        if (!"".equals(strJueves)) {
            strDias.append(" ");
            strDias.append(strJueves);

        }
        if (!"".equals(strViernes)) {
            strDias.append(" ");
            strDias.append(strViernes);

        }
        if (!"".equals(strSabado)) {
            strDias.append(" ");
            strDias.append(strSabado);

        }
        if (!"".equals(strDomingo)) {
            strDias.append(" ");
            strDias.append(strDomingo);
        }

        return strDias.toString();
    }

    /**
     * Valida los campos del formulario
     * @return
     * @Autor Fredy Mosquera Lemus
     * @Fecha 14/02/2016
     */
    public boolean isHaSeleccionadoCampos() {
        boolean validarViews = true;
        if (TextUtils.isEmpty(etHoraInicio.getText())) {
            etHoraInicio.requestFocus();
            etHoraInicio.setError(getString(R.string.error_campo_requerido));
            return false;
        }
        if (TextUtils.isEmpty(etHoraFin.getText())) {
            etHoraFin.requestFocus();
            etHoraFin.setError(getString(R.string.error_campo_requerido));
            return false;
        }
        if("".equals(getDias())){
            Toast.makeText(activity, R.string.str_debeseleccionaralgundia,
                    Toast.LENGTH_SHORT ).show();
            return false;

        }

        return validarViews;
    }
    public void setCheckedDias(Horario horario){
        if(isDiaChecked(getString(R.string.label_lunes), horario)){
            this.chkbLunes.setChecked(true);
            strLunes = getString(R.string.label_lunes);
        }
        if(isDiaChecked(getString(R.string.label_martes), horario)){
            this.chkbMartes.setChecked(isDiaChecked(getString(R.string.label_martes), horario));
            strMartes = getString(R.string.label_martes);
        }

        if(isDiaChecked(getString(R.string.label_miercoles), horario)){
            this.chkbMiercoles.setChecked(isDiaChecked(getString(R.string.label_miercoles), horario));
            strMiercoles = getString(R.string.label_miercoles);
        }
        if(isDiaChecked(getString(R.string.label_jueves), horario)){
            this.chkbJueves.setChecked(isDiaChecked(getString(R.string.label_jueves), horario));
            strJueves = getString(R.string.label_jueves);
        }

        if(isDiaChecked(getString(R.string.label_viernes), horario)){
            this.chkbViernes.setChecked(isDiaChecked(getString(R.string.label_viernes), horario));
            strViernes = getString(R.string.label_viernes);
        }
        if(isDiaChecked(getString(R.string.label_sabado), horario)){
            this.chkbSabado.setChecked(isDiaChecked(getString(R.string.label_sabado), horario));
            strSabado = getString(R.string.label_sabado);
        }
        if(isDiaChecked(getString(R.string.label_domingo), horario)){
            this.chkbDomingo.setChecked(isDiaChecked(getString(R.string.label_domingo), horario));
            strDomingo = getString(R.string.label_domingo);
        }

    }
    public void settearHorarioLaboral(Horario horario){
        etHoraInicio.setText(horario.getHoraInicial());
        etHoraFin.setText(horario.getHoraFinal());

    }


    private boolean isDiaChecked(String strDia, Horario horario){
        String [] arrayDias = horario.getDiasLaborales().split(" ");
        for (String diasChecked:arrayDias) {
            if(strDia.equals(diasChecked)){
                return true;
            }

        }
        return  false;
    }
}
