package co.com.fredymosqueralemus.pelucitas;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
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
import java.util.List;

import co.com.fredymosqueralemus.pelucitas.adapters.AdapterTurnosXCliente;
import co.com.fredymosqueralemus.pelucitas.constantes.Constantes;
import co.com.fredymosqueralemus.pelucitas.modelo.agenda.TurnosXCliente;
import co.com.fredymosqueralemus.pelucitas.utilidades.UtilidadesFirebaseBD;


public class TurnosXClienteFragment extends Fragment {

    private ListView listViewProximosTurnos;
    private ListView listViewTurnosPasados;
    private FirebaseAuth firebaseAuth;
    List<TurnosXCliente> lstTurnosxClienteProximos;
    List<TurnosXCliente> lstTurnosxClientePasados;
    private Context context;
    public TurnosXClienteFragment() {
        // Required empty public constructor
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_agenda_rerservas_cliente, container, false);
        context = getContext();
        firebaseAuth = FirebaseAuth.getInstance();
        lstTurnosxClienteProximos = new ArrayList<TurnosXCliente>();
        lstTurnosxClientePasados = new ArrayList<TurnosXCliente>();
        listViewProximosTurnos = (ListView) view.findViewById(R.id.listView_proximosTurnos_AgendaRerservasClienteFragment);
        listViewTurnosPasados = (ListView) view.findViewById(R.id.listView_TurnosPasados_AgendaRerservasClienteFragment);

        DatabaseReference databaseReferenceTurnoxCliente = FirebaseDatabase.getInstance().getReference(UtilidadesFirebaseBD.getUrlInserccionTurnosXCliente(firebaseAuth.getCurrentUser().getUid()));
        databaseReferenceTurnoxCliente.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    TurnosXCliente turnosxCliente = child.getValue(TurnosXCliente.class);
                    if("S".equals(turnosxCliente.getSnEjecutado())){
                        lstTurnosxClientePasados.add(turnosxCliente);
                    }else{
                        lstTurnosxClienteProximos.add(turnosxCliente);
                    }
                }
                AdapterTurnosXCliente adapterTurnosXClientePasados = new AdapterTurnosXCliente(context, R.layout.layout_listview_turnosxcliente, lstTurnosxClientePasados);
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


}
