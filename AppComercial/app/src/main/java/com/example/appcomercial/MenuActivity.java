package com.example.appcomercial;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.appcomercial.model.Sesion;
import com.example.appcomercial.retrofit.RetrofitClient;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appcomercial.databinding.ActivityMenuBinding;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Retrofit;

public class MenuActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMenuBinding binding;

    /*Variables para la cabecera del menú*/
    CircleImageView imagenUsuario;
    TextView nombreUsuario, emailUsuario;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMenu.toolbar);

        /*
        binding.appBarMenu.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */

        final DrawerLayout drawer = binding.drawerLayout;
        final NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_clientes, R.id.nav_productos, R.id.nav_ventas)
                .setOpenableLayout(drawer)
                .build();
        final NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_menu);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        /**Enlazar a los controles de la cabecera del menú*/
        final View view = navigationView.getHeaderView(0); //Hacer referencia a la cabecera del menú
        imagenUsuario = view.findViewById(R.id.imagenUsuario);
        nombreUsuario = view.findViewById(R.id.nombreUsuario);
        emailUsuario = view.findViewById(R.id.emailUsuario);

        /*Mostrar los datos del usuario que ha iniciado sesión*/
        nombreUsuario.setText(Sesion.DATOS_SESION.getNombre());
        emailUsuario.setText(Sesion.DATOS_SESION.getEmail());

        Log.e("URL Imagen", RetrofitClient.urlComercial.baseUrl() + Sesion.DATOS_SESION.getImagen());

        Glide
                .with(this)
                .load(RetrofitClient.urlComercial.baseUrl() + Sesion.DATOS_SESION.getImagen())
                .into(imagenUsuario);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        final NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_menu);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}