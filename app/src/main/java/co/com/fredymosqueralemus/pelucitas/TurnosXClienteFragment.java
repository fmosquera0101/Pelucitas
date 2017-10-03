package co.com.fredymosqueralemus.pelucitas;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import co.com.fredymosqueralemus.pelucitas.adapters.AdapterTurnosXCliente;
import co.com.fredymosqueralemus.pelucitas.constantes.Constantes;
import co.com.fredymosqueralemus.pelucitas.modelo.agenda.TurnosXCliente;
import co.com.fredymosqueralemus.pelucitas.utilidades.UtilidadesFecha;
import co.com.fredymosqueralemus.pelucitas.utilidades.UtilidadesFirebaseBD;


public class TurnosXClienteFragment extends Fragment {

    private ListView listViewProximosTurnos;
    private ListView listViewTurnosPasados;
    private FirebaseAuth firebaseAuth;
    List<TurnosXCliente> lstTurnosxClienteProximos;
    List<TurnosXCliente> lstTurnosxClienteVencidos;
    private Context context;
    public TurnosXClienteFragment() {
        // Required empty public constructor
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_agenda_rerservas_cliente, container, false);
        context = getContext();
        firebaseAuth = FirebaseAuth.getInstance();
        lstTurnosxClienteProximos = new ArrayList<TurnosXCliente>();
        lstTurnosxClienteVencidos = new ArrayList<TurnosXCliente>();
        listViewProximosTurnos = (ListView) view.findViewById(R.id.listView_proximosTurnos_AgendaRerservasClienteFragment);
        listViewTurnosPasados = (ListView) view.findViewById(R.id.listView_TurnosPasados_AgendaRerservasClienteFragment);

        DatabaseReference databaseReferenceTurnoxCliente = FirebaseDatabase.getInstance().getReference(UtilidadesFirebaseBD.getUrlInserccionTurnosXCliente(firebaseAuth.getCurrentUser().getUid()));
        databaseReferenceTurnoxCliente.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    TurnosXCliente turnosxCliente = child.getValue(TurnosXCliente.class);
                    Date fechahoy = UtilidadesFecha.formatearDate(new Date(), "yyyy/MM/dd HH:mm");
                    Date fechaTurno = UtilidadesFecha.convertirStringADate(turnosxCliente.getFechaTurno()+" "+ turnosxCliente.getHoraTurno(), "yyyy/MM/dd HH:mm");

                    if("S".equals(turnosxCliente.getSnEjecutado()) || fechaTurno.before(fechahoy)){
                        lstTurnosxClienteVencidos.add(turnosxCliente);
                    }else{
                        lstTurnosxClienteProximos.add(turnosxCliente);
                    }
                }
                AdapterTurnosXCliente adapterTurnosXClientePasados = new AdapterTurnosXCliente(context, R.layout.layout_listview_turnosxcliente, lstTurnosxClienteVencidos);
                listViewTurnosPasados.setAdapter(adapterTurnosXClientePasados);
                AdapterTurnosXCliente adapterTurnosXClienteProximos = new AdapterTurnosXCliente(context, R.layout.layout_listview_turnosxcliente, lstTurnosxClienteProximos);
                listViewProximosTurnos.setAdapter(adapterTurnosXClienteProximos);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        listViewProximosTurnos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, TurnosActivity.class);
                TurnosXCliente turnosXCliente = lstTurnosxClienteProximos.get(position);
                intent.putExtra(Constantes.TURNOSXCLIENTE_OBJ, turnosXCliente);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_turnos_fragment, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuitem_eliminar_proximos_turnos:
                break;
            case R.id.menuitem_eliminar_turnos_vencidos:
                eliminarTurnosVencidos();
                break;

        }
        return true;
    }
    protected void eliminarTurnosVencidos() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("¿Desea eliminar TODOS los turnos vencidos?");
        builder.setPositiveButton(getString(R.string.str_aceptar), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DatabaseReference databaseReferenceTurnoxCliente = FirebaseDatabase.getInstance().getReference(UtilidadesFirebaseBD.getUrlInserccionTurnosXCliente(firebaseAuth.getCurrentUser().getUid()));
                for (TurnosXCliente turnosXClientesVencidos:lstTurnosxClienteVencidos) {

                }
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
