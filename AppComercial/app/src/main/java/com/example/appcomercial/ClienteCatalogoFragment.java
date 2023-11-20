package com.example.appcomercial;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.appcomercial.adapter.ClienteCatalogoAdapter;
import com.example.appcomercial.model.Cliente;
import com.example.appcomercial.response.ClienteCatalogoResponse;
import com.example.appcomercial.response.ClienteEliminarResponse;
import com.example.appcomercial.retrofit.ApiService;
import com.example.appcomercial.retrofit.RetrofitClient;
import com.example.appcomercial.util.Helper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClienteCatalogoFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener{
    FloatingActionButton fab;
    RecyclerView recyclerViewClientes;
    ClienteCatalogoAdapter clienteCatalogoAdapter;
    List<Cliente> clienteLista = new ArrayList<>();
    SwipeRefreshLayout swipeRefreshLayoutClientes;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_cliente_catalogo, container, false);

        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(this);

        //Configurar el recyclerview
        recyclerViewClientes = view.findViewById(R.id.recyclerViewClientes);
        recyclerViewClientes.setLayoutManager(new LinearLayoutManager(getContext()));
        clienteCatalogoAdapter = new ClienteCatalogoAdapter(clienteLista);
        recyclerViewClientes.setAdapter(clienteCatalogoAdapter);

        //Configurar el swipeRefreshLayoutClientes
        swipeRefreshLayoutClientes = view.findViewById(R.id.swipeRefreshLayoutClientes);
        swipeRefreshLayoutClientes.setOnRefreshListener(this); //Reconozca el evento onRefresh
        swipeRefreshLayoutClientes.setColorSchemeResources(R.color.primaryColor, R.color.primaryColorDark, R.color.colorAccent);

        //Mostrar el listado
        listar();

        return view;
    }

    private void listar() {
        //Crear una instancia de ApiService para hacer un request al servicio web
        final ApiService apiService = RetrofitClient.createService();
        final Call<ClienteCatalogoResponse> call = apiService.catalogoCliente("0");
        call.enqueue(new Callback<ClienteCatalogoResponse>() {
            @Override
            public void onResponse(final Call<ClienteCatalogoResponse> call, final Response<ClienteCatalogoResponse> response) {
                if (response.code() == 200){
                    final boolean status = response.body().isStatus();
                    if(status){ //True
                        clienteLista.clear();
                        clienteLista.addAll(Arrays.asList(response.body().getData()));
                        //Indicar que hay cambios en los registros para actualizar
                        clienteCatalogoAdapter.notifyDataSetChanged();
                    }
                }else{
                    //http: 500, 400, 401, etc
                    Log.e("Error al acceder al servicio web", response.message());
                }
            }

            @Override
            public void onFailure(final Call<ClienteCatalogoResponse> call, final Throwable t) {
                Log.e("Falla al conectarse al servicio web", t.getMessage());
            }
        });
    }

    @Override
    public void onClick(final View view) {
        if (view.getId() == R.id.fab){
            agregar();
        }
    }

    private void agregar() {
        final NavController navController = Navigation.findNavController(this.getView());
        navController.navigate(R.id.action_nav_cliente_agregar);
    }

    @Override
    public void onCreateContextMenu(@NonNull final ContextMenu menu, @NonNull final View v, @Nullable final ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(@NonNull final MenuItem item) {
        if (item.getItemId() == 1){ //Opción Editar del menú
            final int id = clienteLista.get(clienteCatalogoAdapter.posicionItemSeleccionado).getId();
            Log.e("id de cliente", String.valueOf(id));

            /*LLamar al fragment de agregar clientes, enviando como parámetro el ID del cliente a editar*/
            //Crear una variable para enviar parámetros
            final Bundle parametros = new Bundle();
            parametros.putInt("cliente_id", id);

            final NavController navController = Navigation.findNavController(this.getView());
            navController.navigate(R.id.action_nav_cliente_agregar, parametros);

        }else if (item.getItemId() == 2){ //Opción Eliminar del menú
            final Runnable EliminarClienteTask = new Runnable() {
                @Override
                public void run() {
                    final int id = clienteLista.get(clienteCatalogoAdapter.posicionItemSeleccionado).getId();
                    eliminar(id);
                }
            };

            Helper.mensajeConfirmacion
                    (
                            ClienteCatalogoFragment.this.getActivity(),
                            "Confirme",
                            "Desea eliminar al cliente",
                            "SI ELIMINAR",
                            "NO",
                            EliminarClienteTask
                    );
        }

        return super.onContextItemSelected(item);
    }

    private void eliminar(final int id) {
        final ApiService apiService = RetrofitClient.createService();
        final Call<ClienteEliminarResponse> call = apiService.eliminarCliente(id);
        call.enqueue(new Callback<ClienteEliminarResponse>() {
            @Override
            public void onResponse(final Call<ClienteEliminarResponse> call, final Response<ClienteEliminarResponse> response) {
                if (response.code() == 200){
                    if (response.body().isStatus()){
                        Helper.mensajeInformacion(ClienteCatalogoFragment.this.getActivity(), "Cliente eliminado", response.body().getMessage());
                        listar(); //Refrescar la lista
                    }
                }else{
                    //http status_: 401, 402, 403, 500, etc
                    try {
                        Helper.mensajeError(ClienteCatalogoFragment.this.getActivity(), "Error al eliminar cliente", response.errorBody().string());
                    } catch (final IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(final Call<ClienteEliminarResponse> call, final Throwable t) {
                Helper.mensajeError(ClienteCatalogoFragment.this.getActivity(), "Error al conectar al servcio web", t.getMessage());
            }
        });
    }

    @Override
    public void onRefresh() {
        listar(); //Refrescar el contenido de la lista
        swipeRefreshLayoutClientes.setRefreshing(false); //Desactivar la animación
    }
}