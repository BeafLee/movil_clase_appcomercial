package com.example.appcomercial.adapter;

import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.appcomercial.R;
import com.example.appcomercial.model.Cliente;

import java.util.List;

public class ClienteCatalogoAdapter extends RecyclerView.Adapter<ClienteCatalogoAdapter.ClienteViewHolder>{

    private List<Cliente> clienteLista;
    public int posicionItemSeleccionado;

    public ClienteCatalogoAdapter(final List<Cliente> clienteLista) {
        this.clienteLista = clienteLista;
    }

    @NonNull
    @Override
    public ClienteViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        //Vincular el adapter a la vista (cardview cliente). Archivo: cliente_cardview.xml
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cliente_cardview, parent, false);
        return new ClienteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ClienteViewHolder holder, final int position) {
        //Sirve para imprimir los datos en el cardview
        final Cliente cliente = clienteLista.get(position);
        holder.bind(cliente);
    }

    @Override
    public int getItemCount() {
        //Determina la cantidad de registros a asignar al recyclerView
        return clienteLista.size();
    }

    public class ClienteViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener, View.OnLongClickListener{
        //Declarar los controles que tiene el cardview de cliente
        private TextView nombreTextView, direccionTextView, ciudadTextView, emailTextView;

        public ClienteViewHolder(@NonNull final View itemView) {
            super(itemView);
            //Relacionar los controles del cardview con los controles declarados en java
            nombreTextView = itemView.findViewById(R.id.nombreTextView);
            direccionTextView = itemView.findViewById(R.id.direccionTextView);
            ciudadTextView = itemView.findViewById(R.id.ciudadTextView);
            emailTextView = itemView.findViewById(R.id.emailTextView);

            //Reconocer los eventos OnCreateContextMenuListener y OnLongClickListener
            itemView.setOnCreateContextMenuListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void bind(final Cliente cliente){
            //Asignar los datos del cliente a cada control del cardview
            nombreTextView.setText(cliente.getNombre());
            direccionTextView.setText(cliente.getDireccion());
            ciudadTextView.setText(cliente.getCiudad());
            emailTextView.setText(cliente.getEmail());
        }

        @Override
        public void onCreateContextMenu(final ContextMenu contextMenu, final View view, final ContextMenu.ContextMenuInfo contextMenuInfo) {
            //Crear las opciones del menú. Se visualizará cuando el usuario haga un clic prolongado sobre el cardview
            contextMenu.setHeaderTitle("Elija una opción");
            contextMenu.add(0, 1, 0, "EDITAR");
            contextMenu.add(0, 2, 0, "ELIMINAR");
        }

        @Override
        public boolean onLongClick(final View view) {
            posicionItemSeleccionado = this.getAdapterPosition();
            Log.e("posicionItemSeleccionado", String.valueOf(posicionItemSeleccionado));
            return false;
        }
    }

}
