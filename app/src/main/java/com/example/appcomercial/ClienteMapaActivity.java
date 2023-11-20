package com.example.appcomercial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.appcomercial.util.Helper;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ClienteMapaActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback,  GoogleMap.OnMarkerDragListener, GoogleMap.OnMapClickListener {
    double latitud=0, longitud=0;
    String direccion="";
    GoogleMap mapa;
    Marker marcador;
    FloatingActionButton btnMapaCliente;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente_mapa);

        //Ocultar la barra de titulo
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        //Vincular los controles
        btnMapaCliente = findViewById(R.id.btnMapaCliente);
        btnMapaCliente.setOnClickListener(this);

        //Vincular el mapa
        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapaCliente);
        mapFragment.getMapAsync(this);

        //Definir una lat y lng de ejemplo
        this.latitud = -6.771144;
        this.longitud = -79.839780;

    }

    @Override
    public void onClick(final View view) {
        if (view.getId() == R.id.btnMapaCliente){
            retornarDireccion();
        }
    }

    private void retornarDireccion() {
        //Configurar una variable Bundle para enviar los parámetros de retorno
        final Bundle parametros = new Bundle();
        parametros.putString("p_direccion", this.direccion);
        parametros.putDouble("p_latitud", this.latitud);
        parametros.putDouble("p_longitud", this.longitud);

        //Enviar los parámetros
        final Intent intent = new Intent();
        intent.putExtras(parametros);
        this.setResult(Activity.RESULT_OK, intent);

        //Cerrar el activity del mapa
        this.finish();
    }

    @Override
    public void onMapReady(@NonNull final GoogleMap googleMap) {
        mapa = googleMap;
        mapa.setMapType(mapa.MAP_TYPE_NORMAL);

        //Configurar el mapa para que reconozca al marker (marcador)
        googleMap.setOnMarkerDragListener(this);
        googleMap.setOnMapClickListener(this);

        //Configurar las caracteriticas del marcador
        final MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.draggable(true);
        markerOptions.position(new LatLng(this.latitud, this.longitud));
        markerOptions.title("UBICAR DIRECCIÓN");

        //Agregar el marcador al mapa
        marcador = googleMap.addMarker(markerOptions);
        marcador.showInfoWindow();

        //Centrar la camara del mapa segun la latitud y longitud
        final CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(this.centrarCamara())
                .zoom(17)
                .build();
        final CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        mapa.animateCamera(cameraUpdate);
    }

    private LatLng centrarCamara() {
        final LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(new LatLng(this.latitud, this.longitud));
        final LatLngBounds bounds = builder.build();
        return bounds.getCenter();
    }

    @Override
    public void onMapClick(@NonNull final LatLng latLng) {
        //Se ejecuta cuando el usuario hace un clic en cualquier parte del mapa

        //Definir la posición del marcador
        marcador.setPosition(latLng);

        //centrar la camara
        mapa.animateCamera(CameraUpdateFactory.newLatLng(latLng));

        //Obtener la latitud y longitud según la ubicación del marker
        this.latitud =  latLng.latitude;
        this.longitud = latLng.longitude;

        Log.e("MAPA LATITUD", String.valueOf(this.latitud));
        Log.e("MAPA LONGITUD", String.valueOf(this.longitud));

        //Con la latitud y longitud obtener la dirección
        try{
            this.direccion = Helper.obtenerDireccionMapa(this, this.latitud, this.longitud);
        }catch (final Exception e){
            this.direccion = "No encontrada";
        }
        Log.e("DIRECCIÓN MAPA", this.direccion);

    }

    @Override
    public void onMarkerDrag(@NonNull final Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(@NonNull final Marker marker) {
        //Evento se ejecuta cuando el usaurio arrastra el marcador y luego lo suelta sobre una ubicaciòn

        //Centrar la camara
        mapa.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));

        //Obtener la latitud y longitud según la ubicación del marker
        this.latitud =  marker.getPosition().latitude;
        this.longitud = marker.getPosition().longitude;

        Log.e("MAPA LATITUD", String.valueOf(this.latitud));
        Log.e("MAPA LONGITUD", String.valueOf(this.longitud));

        //Con la latitud y longitud obtener la dirección
        try{
            this.direccion = Helper.obtenerDireccionMapa(this, this.latitud, this.longitud);
        }catch (final Exception e){
            this.direccion = "No encontrada";
        }
        Log.e("DIRECCIÓN MAPA", this.direccion);

    }

    @Override
    public void onMarkerDragStart(@NonNull final Marker marker) {

    }
}