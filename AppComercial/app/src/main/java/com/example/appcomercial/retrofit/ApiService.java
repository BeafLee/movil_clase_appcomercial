package com.example.appcomercial.retrofit;

import com.example.appcomercial.response.CategoriaListadoResponse;
import com.example.appcomercial.response.CiudadListadoResponse;
import com.example.appcomercial.response.ClienteActualizarResponse;
import com.example.appcomercial.response.ClienteCatalogoResponse;
import com.example.appcomercial.response.ClienteConsultarResponse;
import com.example.appcomercial.response.ClienteEliminarResponse;
import com.example.appcomercial.response.ClienteInsertarReponse;
import com.example.appcomercial.response.LoginResponse;
import com.example.appcomercial.response.ProductoVentaResponse;
import com.example.appcomercial.response.SerieListarResponse;
import com.example.appcomercial.response.TipoComprobanteListadoResponse;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    @FormUrlEncoded
    @POST("/login")
    Call<LoginResponse> login(@Field("email") String email, @Field("clave") String clave);

    @FormUrlEncoded
    @POST("/cliente/catalogo")
    Call<ClienteCatalogoResponse> catalogoCliente(@Field("ciudad_id") String ciudadId);

    @GET("/ciudad/listar")
    Call<CiudadListadoResponse> listarCiudad();

    @FormUrlEncoded
    @POST("/cliente/insertar")
    Call<ClienteInsertarReponse> insertarCliente(@Field("nombre") String nombre, @Field("direccion") String direccion, @Field("email") String email, @Field("ciudad_id") String ciudadId);

    @GET("/cliente/consultar/{id}")
    Call<ClienteConsultarResponse> consultarCliente(@Path("id") int id);

    @FormUrlEncoded
    @POST("/cliente/actualizar")
    Call<ClienteActualizarResponse> actualizarCliente(@Field("nombre") String nombre, @Field("direccion") String direccion, @Field("email") String email, @Field("ciudad_id") String ciudadId, @Field("id") int id);

    @FormUrlEncoded
    @POST("/cliente/eliminar")
    Call<ClienteEliminarResponse> eliminarCliente(@Field("id") int id);

    @FormUrlEncoded
    @POST("/producto/catalogo")
    Call<ProductoVentaResponse> catalogoProductoVenta
            (
                    @Field("categoria_id") int categoriaId,
                    @Field("almacen_id") int almacenId
            );

    @GET("/categoria/listar")
    Call<CategoriaListadoResponse> listarCategoria();


    @GET("/cliente/dni/{dni}")
    Call<ClienteConsultarResponse> consultarClienteDNI(@Path("dni") String dni);

    @GET("/comprobante/listar/venta")
    Call<TipoComprobanteListadoResponse> listarComprobanteVenta();

    @GET("/comprobante/serie/{id}")
    Call<SerieListarResponse> listarSerie(@Path("id") int idTipoComprobante);

}
