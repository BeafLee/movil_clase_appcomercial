package com.example.appcomercial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.appcomercial.model.Sesion;
import com.example.appcomercial.response.LoginResponse;
import com.example.appcomercial.retrofit.ApiService;
import com.example.appcomercial.retrofit.RetrofitClient;
import com.example.appcomercial.util.Helper;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    TextInputEditText txtEmail, txtClave;
    MaterialButton btnIngresar;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtEmail = findViewById(R.id.txtEmail);
        txtClave = findViewById(R.id.txtClave);
        btnIngresar = findViewById(R.id.btnIngresar);
        btnIngresar.setOnClickListener(this);
    }

    @Override
    public void onClick(final View view) {
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
        final String email = txtEmail.getText().toString();
        final String clave = Helper.convertPassMd5(txtClave.getText().toString());

        //realizar el request(petición) al end point de login
        final Call<LoginResponse> call = apiService.login(email, clave);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(final Call<LoginResponse> call, final Response<LoginResponse> response) {
                if (response.code() == 200){ //Login ha sido satisfactorio
                    //Almacenar la respuesta del end point en la variale loginResponse
                    final LoginResponse loginResponse = response.body();
                    final boolean status = loginResponse.isStatus();

                    if (status){ //True
                        //Asignar el token de la sesión del usuario
                        RetrofitClient.API_TOKEN = loginResponse.getData().getToken();
                        //Asignar el token de la sesión del usuario

                        final String nombre = loginResponse.getData().getNombre();
                        Log.e("clave ===>", clave);

                        /*Almacenar los datos de la sesión*/
                        Sesion.DATOS_SESION = loginResponse.getData();

                        /*Llamar al MenuActivity*/
                        final Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                        startActivity(intent);

                        /*Cerrar el MainActivity*/
                        MainActivity.this.finish();
                    }
                }else{
                    //http status: 500, 400, 401, etc
                    Log.e("Error ===>", response.message());
                }
            }

            @Override
            public void onFailure(final Call<LoginResponse> call, final Throwable t) {
                //En caso ocurra un error al conectarse con el servicio web, mostrará el mensaje de error
                Log.e("Fail", t.toString());
            }
        });
    }
}