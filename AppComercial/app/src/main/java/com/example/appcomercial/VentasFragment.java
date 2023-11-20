package com.example.appcomercial;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.appcomercial.adapter.ClienteCatalogoAdapter;
import com.example.appcomercial.adapter.ProductoVentaAdapter;
import com.example.appcomercial.model.Categoria;
import com.example.appcomercial.model.Ciudad;
import com.example.appcomercial.model.Cliente;
import com.example.appcomercial.model.Producto;
import com.example.appcomercial.model.Serie;
import com.example.appcomercial.model.Sesion;
import com.example.appcomercial.model.TipoComprobante;
import com.example.appcomercial.response.CategoriaListadoResponse;
import com.example.appcomercial.response.ClienteConsultarResponse;
import com.example.appcomercial.response.ProductoVentaResponse;
import com.example.appcomercial.response.SerieListarResponse;
import com.example.appcomercial.response.TipoComprobanteListadoResponse;
import com.example.appcomercial.response.VentaResponse;
import com.example.appcomercial.retrofit.ApiService;
import com.example.appcomercial.retrofit.RetrofitClient;
import com.example.appcomercial.util.Helper;
import com.example.appcomercial.util.Pickers;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VentasFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener {

    /*Colocar al fab con especificador de acceso publico para que se pueda acceder desde otra clase*/
    public static ExtendedFloatingActionButton fab;
    /*Colocar al fab con especificador de acceso publico para que se pueda acceder desde otra clase*/

    RecyclerView recyclerViewProductosVenta;
    ProductoVentaAdapter productoVentaAdapter;
    List<Producto> productoLista = new ArrayList<>();
    List<Categoria> categoriaLista = new ArrayList<>();
    List<Serie> listaSerie = new ArrayList<>();
    List<TipoComprobante> tipoComprobanteLista = new ArrayList<>();
    SwipeRefreshLayout swipeRefreshLayoutProductosVenta;

    AutoCompleteTextView autoCompleteTextViewCategoria;
    MaterialButton btnFiltrar;

    int categoriaId=0;
    int clienteId = 0;
    int tipoComprobanteId = 0;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_ventas, container, false);

        //Botón flotante
        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(this);

        //Enlazar el autoCompleteTextViewCategoria y btnFiltrar
        autoCompleteTextViewCategoria = view.findViewById(R.id.autoCompleteTextViewCategoria);
        autoCompleteTextViewCategoria.setOnItemClickListener(this);

        btnFiltrar = view.findViewById(R.id.btnFiltrar);
        btnFiltrar.setOnClickListener(this);

        //Configurar el recyclerview
        recyclerViewProductosVenta = view.findViewById(R.id.recyclerViewProductosVenta);
        recyclerViewProductosVenta.setLayoutManager(new LinearLayoutManager(getContext()));
        productoVentaAdapter = new ProductoVentaAdapter(getContext(), productoLista);
        recyclerViewProductosVenta.setAdapter(productoVentaAdapter);

        //Configurar el swipeRefreshLayoutProductosVenta
        swipeRefreshLayoutProductosVenta = view.findViewById(R.id.swipeRefreshLayoutProductosVenta);
        swipeRefreshLayoutProductosVenta.setOnRefreshListener(this); //Reconozca el evento onRefresh
        swipeRefreshLayoutProductosVenta.setColorSchemeResources(R.color.primaryColor, R.color.primaryColorDark, R.color.colorAccent);

        //Cargar las categorías
        cargarCategorias();

        //Mostrar el listado de productos
        listar(0);

        return view;

    }

    private void cargarCategorias() {
        final ApiService apiService = RetrofitClient.createService();
        final Call<CategoriaListadoResponse> call = apiService.listarCategoria();
        call.enqueue(new Callback<CategoriaListadoResponse>() {
            @Override
            public void onResponse(final Call<CategoriaListadoResponse> call, final Response<CategoriaListadoResponse> response) {
                if (response.code()==200){
                    if (response.body().isStatus()){
                        categoriaLista.clear();
                        /*Agregar una categoría adicional para filtrar todos los productos de cualquier catagoría*/
                        categoriaLista.add(new Categoria(0, "Todas"));

                        categoriaLista.addAll(Arrays.asList(response.body().getData()));
                        /*MOSTRAR EN EL AUTOCOMPLETE TEXT VIEW*/
                        final String[] nombreProductos = new String[categoriaLista.size()];
                        for (int i=0; i<categoriaLista.size();i++){
                            nombreProductos[i] = categoriaLista.get(i).getNombre();
                        }
                        final ArrayAdapter<String> adapter = new ArrayAdapter<String>
                                (
                                        getActivity(),
                                        android.R.layout.simple_list_item_1,
                                        nombreProductos
                                );
                        autoCompleteTextViewCategoria.setAdapter(adapter);

                    }else{
                        Helper.mensajeInformacion(getActivity(), "Error al cargar las categorías", response.body().getMessage());
                    }
                }else{
                    try {
                        Helper.mensajeError(getActivity(), "Error al cargar las categorías", response.errorBody().string());
                    } catch (final IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(final Call<CategoriaListadoResponse> call, final Throwable t) {
                Helper.mensajeError(getActivity(), "No se puede conectar al servicio web de categorías", t.getMessage());
            }
        });
    }

    private void listar(final int categoriaId) {
        final ApiService apiService = RetrofitClient.createService();
        final int almacenId = Sesion.DATOS_SESION.getAlmacen_id();
        final Call<ProductoVentaResponse> call = apiService.catalogoProductoVenta(categoriaId, almacenId);
        call.enqueue(new Callback<ProductoVentaResponse>() {
            @Override
            public void onResponse(final Call<ProductoVentaResponse> call, final Response<ProductoVentaResponse> response) {
                if (response.code()==200){
                    if (response.body().isStatus()){
                        productoLista.clear();
                        productoLista.addAll(Arrays.asList(response.body().getData()));
                        productoVentaAdapter.notifyDataSetChanged();
                    }else{
                        Helper.mensajeInformacion(getActivity(), "Error al listar los productos", response.body().getMessage());
                    }
                }else{
                    try {
                        Helper.mensajeError(getActivity(), "Error al listar los productos", response.errorBody().string());
                    } catch (final IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(final Call<ProductoVentaResponse> call, final Throwable t) {
                Helper.mensajeError(getActivity(), "No se puede conectar al servicio web de catálogo de productos", t.getMessage());
            }
        });
    }

    @Override
    public void onClick(final View view) {
        if (view.getId() == R.id.btnFiltrar){
            listar(this.categoriaId);
        }else if (view.getId() == R.id.fab){
            dialogoRegistrarVenta();
        }
    }

    @Override
    public void onRefresh() {
        listar(0);
        swipeRefreshLayoutProductosVenta.setRefreshing(false);
    }


    @Override
    public void onItemClick(final AdapterView<?> adapterView, final View view, final int i, final long l) {
        //Obtener el id de la categoría según el item seleccionado en al autocompleteTextView
        categoriaId = categoriaLista.get(i).getId();
    }


    private void dialogoRegistrarVenta(){
        //Declarar el dialog y asignar sus propiedades
        final LayoutInflater li = LayoutInflater.from(getActivity());
        final View view = li.inflate(R.layout.venta_confirmar_dialog, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(view);
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.setTitle("Registrar venta");

        //Declarar los controles del dialog y enlazarlos con el diseño XML
        final TextView txtResumenVenta = view.findViewById(R.id.txtResumenVenta);
        final TextInputEditText txtDNI = view.findViewById(R.id.txtDNI);
        final TextInputEditText txtNombre = view.findViewById(R.id.txtNombre);
        final TextInputEditText txtFechaEmision = view.findViewById(R.id.txtFechaEmision);
        final AutoCompleteTextView autoCompleteTextViewTipoComprobante = view.findViewById(R.id.autoCompleteTextViewTipoComprobante);
        final AutoCompleteTextView autoCompleteTextViewSerie = view.findViewById(R.id.autoCompleteTextViewSerie);
        final TextInputEditText txtSubTotal = view.findViewById(R.id.txtSubTotal);
        final TextInputEditText txtIgv = view.findViewById(R.id.txtIgv);
        final TextInputEditText txtTotal = view.findViewById(R.id.txtTotal);
        final MaterialButton btnRegistrarVenta = view.findViewById(R.id.btnRegistrarVenta);
        final MaterialButton btnSalir  = view.findViewById(R.id.btnSalir);
        final MaterialButton btnBuscar = view.findViewById(R.id.btnBuscar);
        final ProgressBar progressBarCliente = view.findViewById(R.id.progressBarCliente);
        final TextInputLayout txtLayoutFechaEmision = view.findViewById(R.id.txtLayoutFechaEmision);


        final AlertDialog alertDialog = alertDialogBuilder.show();

        /*Mostrar los productos en el resumen de la venta*/
        mostrarProductosResumenVenta(txtResumenVenta);
        /*Mostrar los productos en el resumen de la venta*/

        /*Mostrar la fecha actual*/
        txtFechaEmision.setText(Helper.obtenerFechaActual());
        /*Mostrar la fecha actual*/

        /*Implementar la selección de fecha*/
        txtLayoutFechaEmision.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Pickers.obtenerFecha(getActivity(), txtFechaEmision);
            }
        });
        /*Implementar la selección de fecha*/
        
        /*Cargar tipo de comprobante*/
        cargarTipoComprobante(autoCompleteTextViewTipoComprobante);
        /*Cargar tipo de comprobante*/

        //Cargar la serie segun el tipo de comrpobante
        autoCompleteTextViewTipoComprobante.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> adapterView, final View view, final int i, final long l) {
                tipoComprobanteId = tipoComprobanteLista.get(i).getId();
                cargarSerie(tipoComprobanteId, autoCompleteTextViewSerie);
            }
        });


        //mostrar los totales segùn los productos del carrito
        final double[] totales = productoVentaAdapter.calcularTotales();
        txtSubTotal.setText(Helper.formatearNumero(totales[0]));
        txtIgv.setText(Helper.formatearNumero(totales[1]));
        txtTotal.setText(Helper.formatearNumero(totales[2]));


        //Implementar el botón buscar por DNI
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (txtDNI.getText().toString().isEmpty()){
                    Helper.mensajeInformacion(getActivity(), "Verifique", "Debe ingresar un DNI");
                    return;
                }

                if (txtDNI.getText().toString().length() != 8){
                    Helper.mensajeInformacion(getActivity(), "Verifique", "Debe ingresar un DNI de 8 dígitos");
                    return;
                }

                final String dni = txtDNI.getText().toString();

                //Buscar cliente por DNI
                buscarClienteDNI(dni, txtNombre, progressBarCliente);

            }
        });

        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                alertDialog.dismiss();
            }
        });

        //Implementar el botón Registrar venta
        btnRegistrarVenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Helper.mensajeConfirmacion(
                        getActivity(), "Confirme", "Desea grabar la venta",
                        "SI GRABAR", "NO",
                        new GrabarVentaTask(
                                clienteId, tipoComprobanteId,
                                autoCompleteTextViewSerie.getText().toString(),
                                Helper.formatearAMD_to_DMA(txtFechaEmision.getText().toString()),
                                Sesion.DATOS_SESION.getId(),
                                Sesion.DATOS_SESION.getAlmacen_id(),
                                alertDialog
                        )
                );
            }
        });


        alertDialog.show();
    }

    private void mostrarProductosResumenVenta(final TextView txtResumenVenta){
        //Leer los productos cargados en el carrito
        txtResumenVenta.setText("");
        for(final Producto producto: productoVentaAdapter.carritoVenta){
            txtResumenVenta.append("(" + producto.getCantidad() + ") " + producto.getNombre() + "\n");
        }
    }

    private void buscarClienteDNI(final String dni, final TextInputEditText txtNombre, final ProgressBar progressBarCliente){

        //Mostrar el progressBarCliente
        progressBarCliente.setVisibility(View.VISIBLE);

        final ApiService apiService = RetrofitClient.createService();
        final Call<ClienteConsultarResponse> call = apiService.consultarClienteDNI(dni);
        call.enqueue(new Callback<ClienteConsultarResponse>() {
            @Override
            public void onResponse(final Call<ClienteConsultarResponse> call, final Response<ClienteConsultarResponse> response) {
                //Ocultar el progressBarCliente
                progressBarCliente.setVisibility(View.GONE);

                if(response.code()==200){
                    if (response.body().isStatus()){
                        //Si se encontró al cliente
                        final Cliente cliente = response.body().getData();
                        txtNombre.setText(cliente.getNombre());
                        clienteId = cliente.getId();
                    }else{
                        Helper.mensajeError(getActivity(), "Verifique", "Cliente no encontrdo");
                    }
                }else{
                    try {
                        Helper.mensajeError(getActivity(), "Error al buscar el cliente", response.errorBody().string());
                    } catch (final IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(final Call<ClienteConsultarResponse> call, final Throwable t) {
                //Ocultar el progressBarCliente
                progressBarCliente.setVisibility(View.GONE);

                Helper.mensajeError(getActivity(), "Error al conectar con el servicio web", t.getMessage());
            }
        });
    }


    private void cargarTipoComprobante(final AutoCompleteTextView autoCompleteTextViewTipoComprobante){
        final ApiService apiService = RetrofitClient.createService();
        final Call<TipoComprobanteListadoResponse> call = apiService.listarComprobanteVenta();
        call.enqueue(new Callback<TipoComprobanteListadoResponse>() {
            @Override
            public void onResponse(final Call<TipoComprobanteListadoResponse> call, final Response<TipoComprobanteListadoResponse> response) {
                if (response.code()==200){
                    final boolean status = response.body().isStatus();
                    if (status){ //True
                        tipoComprobanteLista.clear();
                        tipoComprobanteLista.addAll(Arrays.asList(response.body().getData()));
                        final String[] nombreTipoComprobante = new String[tipoComprobanteLista.size()];
                        for (final TipoComprobante tipoComprobante: tipoComprobanteLista) {
                            nombreTipoComprobante[tipoComprobanteLista.indexOf(tipoComprobante)] = tipoComprobante.getNombre();
                        }
                        final ArrayAdapter<String> adapter = new ArrayAdapter<>
                                (
                                        getActivity(),
                                        android.R.layout.simple_list_item_1,
                                        nombreTipoComprobante
                                );
                        autoCompleteTextViewTipoComprobante.setAdapter(adapter);

                    }else{
                        Helper.mensajeError(getActivity(), "Verifique", "Error cargar la lista de tipo de comprobante");
                    }
                }else{
                    //Status: 500, 401, 400, etc
                    try {
                        Helper.mensajeError(getActivity(), "Error al ejecutar el servicio de listar tipo de comprobante", response.errorBody().string());
                    } catch (final IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(final Call<TipoComprobanteListadoResponse> call, final Throwable t) {
                Helper.mensajeError(getActivity(), "Error al conectar con el servicio web", t.getMessage());
            }
        });
    }

    private void cargarSerie(final int tipoComprobanteId, final AutoCompleteTextView actvSerie) {
        final ApiService apiService = RetrofitClient.createService();
        final Call<SerieListarResponse> call = apiService.listarSerie(tipoComprobanteId);
        call.enqueue(new Callback<SerieListarResponse>() {
            @Override
            public void onResponse(final Call<SerieListarResponse> call, final Response<SerieListarResponse> response) {
                if (response.code() == 200) {
                    final SerieListarResponse serie = response.body();
                    if (serie.isStatus()) {
                        listaSerie.clear();
                        listaSerie.addAll(Arrays.asList(serie.getData()));

                        final String[] arraySeries = new String[listaSerie.size()];

                        for (int i = 0; i < listaSerie.size(); i++) {
                            arraySeries[i] = listaSerie.get(i).getSerie();
                        }

                        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, arraySeries);

                        //Mostar el adaptador en el auto complete text view serie
                        actvSerie.setText("");
                        actvSerie.setAdapter(adapter);

                    } else {
                        try {
                            
                            Helper.mensajeError(getActivity(), "Verifique", response.errorBody().string());
                        } catch (final IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }

            @Override
            public void onFailure(final Call<SerieListarResponse> call, final Throwable t) {
                Helper.mensajeError(getActivity(), "Verifique", t.getMessage());
            }
        });

    }

    private void grabarVenta(
            final int clienteId, final int tipoComprobanteId, final String nser, final String fdoc,
            final int usuarioIdRegistro, final int almacenId,
            final AlertDialog dialog
            )
    {

        //generar un json array con los producto del detalle venta
        final JSONArray jsonArray = new JSONArray();
        for (final Producto producto: productoVentaAdapter.carritoVenta) {
            jsonArray.put(producto.getJSONItemProducto());
        }

        final String detalleVentaJSONArray = jsonArray.toString();

        final ApiService apiService = RetrofitClient.createService();
        final Call<VentaResponse> call = apiService.resgistrarVenta(clienteId, tipoComprobanteId, nser,
                                fdoc, usuarioIdRegistro, almacenId, detalleVentaJSONArray);

        call.enqueue(new Callback<VentaResponse>() {
            @Override
            public void onResponse(final Call<VentaResponse> call, final Response<VentaResponse> response) {
                if (response.code() == 200) {
                    final VentaResponse ventaResponse = response.body();
                    if (ventaResponse.isStatus()) {
                        final String mensajeVenta = "La venta se ha registrado correctamente \n\n" +
                                "Tipo Comprobante: " + String.valueOf(tipoComprobanteId) + "\n" +
                                "Serie: " + String.valueOf(nser) + "\n" +
                                "Número de comprobante: " + ventaResponse.getData().getNdoc() + "\n" +
                                "Total Neto: " + ventaResponse.getData().getTotal();

                        Helper.mensajeInformacion(getActivity(), "Venta", mensajeVenta);

                        dialog.dismiss();

                        //
                        listar(0);

                        //Limpiar el carrito de venta
                        productoVentaAdapter.carritoVenta.clear();
                        fab.setText("Carrito");
                    }
                } else {
                    try {
                        Helper.mensajeError(getActivity(), "Error", response.errorBody().string());
                    } catch (final IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(final Call<VentaResponse> call, final Throwable t) {
                Helper.mensajeError(getActivity(), "Error", t.getMessage());
            }
        });
        
    }


    private class GrabarVentaTask implements Runnable {
        private int clienteId;
        private int tipoComprobanteId;
        private String nser;
        private String fdoc;
        private int usuarioIdRegistro;
        private int almancenId;
        AlertDialog dialog;


        public GrabarVentaTask(final int clienteId,
                               final int tipoComprobanteId,
                               final String nser,
                               final String fdoc,
                               final int usuarioIdRegistro,
                               final int almacenId,
                               final AlertDialog dialog
        ) {
            //Asignar valores a los atributos
            this.clienteId = clienteId;
            this.tipoComprobanteId = tipoComprobanteId;
            this.nser = nser;
            this.fdoc = fdoc;
            this.usuarioIdRegistro = usuarioIdRegistro;
            this.almancenId = almacenId;
            this.dialog = dialog;
        }


        @Override
        public void run() {
            //Ejecutar el metodo grabarVenta
            grabarVenta(clienteId, tipoComprobanteId, nser, fdoc, usuarioIdRegistro, almancenId, dialog);
        }
    }
}