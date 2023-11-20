package com.servicioshci.clienteappcomercial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.servicioshci.clienteappcomercial.model.SesionCliente;
import com.servicioshci.clienteappcomercial.response.ClienteActualizarFirebaseID;
import com.servicioshci.clienteappcomercial.response.SesionClienteResponse;
import com.servicioshci.clienteappcomercial.retrofit.ApiService;
import com.servicioshci.clienteappcomercial.retrofit.RetrofitClient;
import com.servicioshci.clienteappcomercial.sharedpreferences.FirebaseTokenManager;
import com.servicioshci.clienteappcomercial.util.Helper;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    TextInputEditText txtDNI, txtClave;
    MaterialButton btnIngresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.setTitle("Iniciar sesión");

        txtDNI = findViewById(R.id.txtDNI);
        txtClave = findViewById(R.id.txtClave);
        btnIngresar = findViewById(R.id.btnIngresar);
        btnIngresar.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnIngresar){
            login();
        }
    }

    private void login() {
        //Toast.makeText(this, "test", Toast.LENGTH_SHORT).show();
        Log.e("Error", "test");
        //Crear una instancia de ApiService para hacer un request (petición) a un servicio web
        final ApiService apiService = RetrofitClient.urlComercial.create(ApiService.class);

        //Obtener las credenciales
        final String email = txtDNI.getText().toString();
        final String clave = Helper.convertPassMd5(txtClave.getText().toString());

        //realizar el request(petición) al end point de login
        final Call<SesionClienteResponse> call = apiService.loginCliente(email, clave);
        call.enqueue(new Callback<SesionClienteResponse>() {
            @Override
            public void onResponse(final Call<SesionClienteResponse> call, final Response<SesionClienteResponse> response) {
                if (response.code() == 200){ //Login ha sido satisfactorio
                    //Almacenar la respuesta del end point en la variale loginResponse
                    final SesionClienteResponse loginResponse = response.body();
                    final boolean status = loginResponse.isStatus();

                    if (status){ //True
                        //Asignar el token de la sesión del usuario
                        RetrofitClient.API_TOKEN = loginResponse.getData().getToken();
                        //Asignar el token de la sesión del usuario

                        final String nombre = loginResponse.getData().getNombre();
                        Log.e("Nombre ===>", nombre);

                        /*Almacenar los datos de la sesión*/
                        SesionCliente.DATOS_SESION = loginResponse.getData();


                        /*Llamar al MenuActivity*/
                        final Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);

                        /*Cerrar el MainActivity*/
                        LoginActivity.this.finish();

                    }
                }else{
                    //http status: 500, 400, 401, etc
                    try {
                        Log.e("Error ===>", response.errorBody().string());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(final Call<SesionClienteResponse> call, final Throwable t) {
                //En caso ocurra un error al conectarse con el servicio web, mostrará el mensaje de error
                Log.e("Fail", t.toString());
            }
        });
    }
}