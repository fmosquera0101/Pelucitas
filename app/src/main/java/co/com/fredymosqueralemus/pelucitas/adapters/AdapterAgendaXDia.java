package co.com.fredymosqueralemus.pelucitas.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import co.com.fredymosqueralemus.pelucitas.R;
import co.com.fredymosqueralemus.pelucitas.constantes.Constantes;
import co.com.fredymosqueralemus.pelucitas.modelo.agenda.AgendaXEmpleado;
import co.com.fredymosqueralemus.pelucitas.modelo.usuario.Usuario;
import co.com.fredymosqueralemus.pelucitas.utilidades.UtilidadesFirebaseBD;

/**
 * Created by fredymosqueralemus on 29/06/17.
 */

public class AdapterAgendaXDia extends ArrayAdapter<AgendaXEmpleado> {

    private Context context;
    private int idLayout;
    private List<AgendaXEmpleado> lstAgendaXEmpleado;
    private  ItemHolderAgendaXEmpleado itemHolderAgendaXEmpleado;

    public AdapterAgendaXDia(Context context, int idLayout, List<AgendaXEmpleado> lstAgendaXEmpleado){
        super(context, idLayout, lstAgendaXEmpleado);
        this.context = context;
        this.idLayout = idLayout;
        this.lstAgendaXEmpleado = lstAgendaXEmpleado;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if(null == view){
            LayoutInflater layoutInflater = ((Activity) context).getLayoutInflater();
            view = layoutInflater.inflate(idLayout, parent, false);
            itemHolderAgendaXEmpleado = new ItemHolderAgendaXEmpleado();
            itemHolderAgendaXEmpleado.imvIconAgendaXdia = (ImageView) view.findViewById(R.id.imagen_hora_agendaxdia_layout_listview_agendaxdia);
            itemHolderAgendaXEmpleado.txvHoraAgenda = (TextView) view.findViewById(R.id.hora_agenda_layout_layout_listview_agendaxdia);
            itemHolderAgendaXEmpleado.txvSnReservado = (TextView) view.findViewById(R.id.snreservado_agenda_layout_listview_agendaxdia);
            view.setTag(itemHolderAgendaXEmpleado);
        }else {
            itemHolderAgendaXEmpleado = (ItemHolderAgendaXEmpleado) view.getTag();
        }
        AgendaXEmpleado agendaXEmpleado = lstAgendaXEmpleado.get(position);
        if(Constantes.SI.equals(agendaXEmpleado.getSnReservado())){
            itemHolderAgendaXEmpleado.imvIconAgendaXdia.setImageResource(R.drawable.ic_event_busy_black_24dp);

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(UtilidadesFirebaseBD.getUrlInserccionUsuario(agendaXEmpleado.getUidUsuarioReserva()));
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Usuario usuario = dataSnapshot.getValue(Usuario.class);
                    StringBuilder strbReservado = new StringBuilder();
                    strbReservado.append(context.getString(R.string.str_reservado)).append(" por: ");
                    strbReservado.append(usuario.getNombre()).append(" ").append(usuario.getApellidos());
                    strbReservado.append(", ").append(context.getString(R.string.str_telefono)).append(": ");
                    strbReservado.append(usuario.getTelefono());
                    itemHolderAgendaXEmpleado.txvSnReservado.setText(strbReservado.toString() );
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }else{
            itemHolderAgendaXEmpleado.imvIconAgendaXdia.setImageResource(R.drawable.ic_access_time_black_24dp);
            itemHolderAgendaXEmpleado.txvSnReservado.setText(context.getString(R.string.str_disponible));
        }
        itemHolderAgendaXEmpleado.txvHoraAgenda.setText(agendaXEmpleado.getHoraReserva());


        return view;
    }
    private class  ItemHolderAgendaXEmpleado{
        ImageView imvIconAgendaXdia;
        TextView txvHoraAgenda;
        TextView txvSnReservado;
    }
}
