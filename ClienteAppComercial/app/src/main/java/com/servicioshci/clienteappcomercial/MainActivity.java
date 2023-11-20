package com.servicioshci.clienteappcomercial;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.servicioshci.clienteappcomercial.databinding.ActivityMainBinding;
import com.servicioshci.clienteappcomercial.model.SesionCliente;
import com.servicioshci.clienteappcomercial.response.ClienteActualizarFirebaseID;
import com.servicioshci.clienteappcomercial.retrofit.ApiService;
import com.servicioshci.clienteappcomercial.retrofit.RetrofitClient;
import com.servicioshci.clienteappcomercial.util.Helper;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_facturacion, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        //Grabar el Token de Firebase en la BD
        grabarFirebaseID();

    }

    private void grabarFirebaseID(){
        String token = "123xyz..."; //FirebaseTokenManager.getFirebaseToken(getApplicationContext());

        if (token != null) {
            Log.e("TOKEN firebase ID", token);

            ApiService apiService = RetrofitClient.createService();
            Call<ClienteActualizarFirebaseID> callFT = apiService.actualizarFirebaseID(SesionCliente.DATOS_SESION.getId(), token);
            callFT.enqueue(new Callback<ClienteActualizarFirebaseID>() {
                @Override
                public void onResponse(Call<ClienteActualizarFirebaseID> callFT, Response<ClienteActualizarFirebaseID> response) {
                    if (response.code() == 200){ //200=OK
                        ClienteActualizarFirebaseID actFbToken = response.body();
                        if (! actFbToken.isStatus()) {
                            Helper.mensajeError(MainActivity.this, "Error al actualizar el firebase token del cliente", actFbToken.getMessage());
                        }
                    }else{ //Status=500,401,403,etc
                        try {
                            Helper.mensajeError(MainActivity.this, "Error al actualizar el firebase token del cliente", response.message() + "\n" + response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ClienteActualizarFirebaseID> callFT, Throwable t) {
                    Helper.mensajeError(MainActivity.this, "Error al conectar al servicio web para actualizar el token", t.getMessage());
                }
            });
        } else {
            // Si el token no está guardado, haz algo (por ejemplo, muestra un mensaje de error)
            Helper.mensajeError(MainActivity.this, "Verifique", "No se ha podido extraer el firebase id token");
        }
    }

}