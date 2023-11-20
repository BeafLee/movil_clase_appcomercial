package com.servicioshci.clienteappcomercial;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;

public class ServicioNotificacionFCM extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull final String token) {
        super.onNewToken(token);

        Log.e("Firebase ID token del usuario", token);


    }
}
