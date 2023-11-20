package com.example.appcomercial;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.example.appcomercial.model.Ciudad;
import com.example.appcomercial.model.Cliente;
import com.example.appcomercial.response.CiudadListadoResponse;
import com.example.appcomercial.response.ClienteActualizarResponse;
import com.example.appcomercial.response.ClienteConsultarResponse;
import com.example.appcomercial.response.ClienteInsertarReponse;
import com.example.appcomercial.retrofit.ApiService;
import com.example.appcomercial.retrofit.RetrofitClient;
import com.example.appcomercial.util.Helper;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClienteAgregarFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {
    TextInputEditText txtNombre, txtDireccion, txtEmail;
    AutoCompleteTextView autoCompleteTextViewCiudad;
    MaterialButton btnRegistrar;
    TextInputLayout inputLayoutDireccion;

    List<Ciudad> listaCiudad = new ArrayList<>();
    int posicionItemSeleccionado;
    String operacion=""; //agregar, editar
    int id=0;

    int ciudadId=0; //Variable para almacenar el ID de la ciudad que le pertenece al cliente

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_cliente_agregar, container, false);

        //Enlazar los controles
        txtNombre = view.findViewById(R.id.txtNombre);
        txtDireccion = view.findViewById(R.id.txtDireccion);
        txtEmail = view.findViewById(R.id.txtEmail);
        autoCompleteTextViewCiudad = view.findViewById(R.id.autoCompleteTextViewCiudad);
        autoCompleteTextViewCiudad.setOnItemClickListener(this);
        btnRegistrar = view.findViewById(R.id.btnRegistrar);
        btnRegistrar.setOnClickListener(this);

        /*Enlazar al inputLayoutDireccion*/
        inputLayoutDireccion = view.findViewById(R.id.input_layout_direccion);
        inputLayoutDireccion.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                //Toast.makeText(ClienteAgregarFragment.this.getActivity(), "Abrir mapa", Toast.LENGTH_SHORT).show();
                final Intent intent = new Intent(getActivity(), ClienteMapaActivity.class);
                activityResultLauncherMap.launch(intent);
            }
        });

        //Cargar las ciudades en el autoCompleteTextViewCiudad
        cargarCiudad();

        //Validar si tenemos parámetros de entrada
        final Bundle parametros = this.getArguments();
        if (parametros != null) {
            operacion = "editar";
            id =  parametros.getInt("cliente_id");
            consultarDatos(id);

            //Actualizar el titulo del fragment cuando estamos editando
            ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle("Editar cliente");
            btnRegistrar.setText("ACTUALIZAR DATOS DE CLIENTE");
        }else{
            ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle("Agregar cliente");
            operacion = "agregar";
        }

        return view;
    }

    private void consultarDatos(final int id) {
        //Consultar los datos de un cliente mediante su ID
        final ApiService apiService = RetrofitClient.createService();
        final Call<ClienteConsultarResponse> call = apiService.consultarCliente(id);
        call.enqueue(new Callback<ClienteConsultarResponse>() {
            @Override
            public void onResponse(final Call<ClienteConsultarResponse> call, final Response<ClienteConsultarResponse> response) {
                if (response.code()==200){
                    if (response.body().isStatus()){
                        //Cliente encontrado
                        final Cliente cliente = response.body().getData();
                        txtNombre.setText(cliente.getNombre());
                        txtDireccion.setText(cliente.getDireccion());
                        txtEmail.setText(cliente.getEmail());
                        autoCompleteTextViewCiudad.setText(cliente.getCiudad());
                        ClienteAgregarFragment.this.ciudadId = cliente.getCiudadId();

                    }else{
                        Helper.mensajeInformacion(ClienteAgregarFragment.this.getActivity(), "Verifique", "Cliente no encontrado");
                    }
                }else{
                    //Status: 500, 401, 400, etc
                    try {
                        Helper.mensajeError(ClienteAgregarFragment.this.getActivity(), "Error", response.errorBody().string());
                    } catch (final IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(final Call<ClienteConsultarResponse> call, final Throwable t) {
                Helper.mensajeError(ClienteAgregarFragment.this.getActivity(), "Error al conectar con el servicio web", t.getMessage());
            }
        });
    }

    private void cargarCiudad() {
        final ApiService apiService = RetrofitClient.createService();
        final Call<CiudadListadoResponse> call = apiService.listarCiudad();
        call.enqueue(new Callback<CiudadListadoResponse>() {
            @Override
            public void onResponse(final Call<CiudadListadoResponse> call, final Response<CiudadListadoResponse> response) {
                if (response.code()==200){
                    final boolean status = response.body().isStatus();
                    if (status){ //True
                        listaCiudad.clear();
                        listaCiudad.addAll(Arrays.asList(response.body().getData()));
                        final String[] nombreCiudad = new String[listaCiudad.size()];
                        for (final Ciudad ciudad: listaCiudad) {
                            nombreCiudad[listaCiudad.indexOf(ciudad)] = ciudad.getNombre();
                        }
                        final ArrayAdapter<String> adapter = new ArrayAdapter<>
                                (
                                        ClienteAgregarFragment.this.getActivity(),
                                        android.R.layout.simple_list_item_1,
                                        nombreCiudad
                                );
                        autoCompleteTextViewCiudad.setAdapter(adapter);

                    }
                }else{
                    //Status: 500, 401, 400, etc
                    try {
                        Log.e("Error al ejecutar el servicio de listar ciudad", response.errorBody().string());
                    } catch (final IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(final Call<CiudadListadoResponse> call, final Throwable t) {
                Log.e("Error al conectar con el servicio web de ciudades", t.getMessage());
            }
        });
    }

    @Override
    public void onClick(final View view) {
        if (view.getId() == R.id.btnRegistrar){
            if (this.operacion.equalsIgnoreCase("agregar")){
                registrar();
            }else{
                actualizar();
            }
        }
    }

    private void registrar() {
        if (validarDatos()){
            final String nombre = txtNombre.getText().toString();
            final String direccion = txtDireccion.getText().toString();
            final String email = txtEmail.getText().toString();
            //final String ciudadId = String.valueOf(listaCiudad.get(posicionItemSeleccionado).getId());
            final String ciudadId = String.valueOf(this.ciudadId);

            final ApiService apiService = RetrofitClient.createService();
            final Call<ClienteInsertarReponse> call = apiService.insertarCliente(nombre,direccion,email,ciudadId);
            call.enqueue(new Callback<ClienteInsertarReponse>() {
                @Override
                public void onResponse(final Call<ClienteInsertarReponse> call, final Response<ClienteInsertarReponse> response) {
                    if (response.code() == 200){
                        if (response.body().isStatus()){ //True
                            Helper.mensajeInformacion(getActivity(), "Mensaje", "Cliente registrado correctamente");
                            final NavController navController = Navigation.findNavController(ClienteAgregarFragment.this.getView());
                            navController.navigateUp();
                        }
                    }else{
                        try {
                            Helper.mensajeError(getActivity(), "Error al registrar el cliente", response.errorBody().string());
                        } catch (final IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }

                @Override
                public void onFailure(final Call<ClienteInsertarReponse> call, final Throwable t) {
                    Log.e("Error al conectar con el servicio web de clientes", t.getMessage());
                }
            });

        }
    }

    private void actualizar(){
        if (validarDatos()) {
            final String nombre = txtNombre.getText().toString();
            final String direccion = txtDireccion.getText().toString();
            final String email = txtEmail.getText().toString();
            //final String ciudadId = String.valueOf(listaCiudad.get(posicionItemSeleccionado).getId());
            final String ciudadId = String.valueOf(this.ciudadId);

            final ApiService apiService = RetrofitClient.createService();
            final Call<ClienteActualizarResponse> call = apiService.actualizarCliente(nombre, direccion, email, ciudadId, id);
            call.enqueue(new Callback<ClienteActualizarResponse>() {
                @Override
                public void onResponse(final Call<ClienteActualizarResponse> call, final Response<ClienteActualizarResponse> response) {
                    if (response.code() == 200){
                        if (response.body().isStatus()){ //True
                            Helper.mensajeInformacion(getActivity(), "Mensaje", "Datos actualizados correctamente");
                            final NavController navController = Navigation.findNavController(ClienteAgregarFragment.this.getView());
                            navController.navigateUp();
                        }
                    }else{
                        try {
                            Helper.mensajeError(getActivity(), "Error al actualizar los datos del cliente", response.errorBody().string());
                        } catch (final IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }

                @Override
                public void onFailure(final Call<ClienteActualizarResponse> call, final Throwable t) {
                    Helper.mensajeError(ClienteAgregarFragment.this.getActivity(), "Error al conectar al servicio web", t.getMessage());
                }
            });
        }
    }


    private boolean validarDatos() {
        if (txtNombre.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Falta ingresar el nombre", Toast.LENGTH_SHORT).show();
            return false;

        }else if (txtDireccion.getText().toString().isEmpty()){
            Toast.makeText(getActivity(), "Falta ingresar la direccion", Toast.LENGTH_SHORT).show();
            return false;

        }else if (txtEmail.getText().toString().isEmpty()){
            Toast.makeText(getActivity(), "Falta ingresar el email", Toast.LENGTH_SHORT).show();
            return false;

        }else if (autoCompleteTextViewCiudad.getText().toString().isEmpty()){
            Toast.makeText(getActivity(), "Falta seleccionar una ciudad", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onItemClick(final AdapterView<?> adapterView, final View view, final int i, final long l) {
        //Obtener la posición del item seleccionado
        posicionItemSeleccionado = i;
        this.ciudadId = listaCiudad.get(posicionItemSeleccionado).getId();
        Log.e("Posición del item seleccionado en ciudad", String.valueOf(posicionItemSeleccionado));
    }


    //Permite abrir el Activity ClienteMapaActivity y posteriormente recuperar la dirección seleccionada en el mapa
    ActivityResultLauncher<Intent> activityResultLauncherMap = registerForActivityResult
            (
                    new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(final ActivityResult result) {
                            //Capturar la dirección que devuelva la API Google Maps
                            if (result.getResultCode() == Activity.RESULT_OK) {
                                Bundle parametros = new Bundle();

                                //Obtener los parámetros que devuelve el mapa
                                parametros = result.getData().getExtras();

                                //Mostrar la dirección que devuelve el mapa en la caja de texto
                                txtDireccion.setText(parametros.getString("p_direccion"));
                            }
                        }
                    }
            );
}
