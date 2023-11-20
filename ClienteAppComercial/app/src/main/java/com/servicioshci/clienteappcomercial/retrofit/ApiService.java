package com.servicioshci.clienteappcomercial.retrofit;

import com.servicioshci.clienteappcomercial.response.ClienteActualizarFirebaseID;
import com.servicioshci.clienteappcomercial.response.SesionClienteResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    @FormUrlEncoded
    @POST("/cliente/login")
    Call<SesionClienteResponse> loginCliente(@Field("dni") String email, @Field("clave") String clave);

    @FormUrlEncoded
    @POST("/cliente/actualizar/firebaseid")
    Call<ClienteActualizarFirebaseID> actualizarFirebaseID(@Field("cliente_id") int clienteID, @Field("firebase_id") String firebaseID);

}
