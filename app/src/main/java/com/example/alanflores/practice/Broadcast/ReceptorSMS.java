package com.example.alanflores.practice.Broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

/**
 * Created by alan.flores on 1/18/17.
 */

public class ReceptorSMS extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v("ReceptorSMS", "SMS recibido");

        Bundle b = intent.getExtras();

        if (b != null) {
            Object[] pdus = (Object[]) b.get("pdus");

            SmsMessage[] mensajes = new SmsMessage[pdus.length];

            for (int i = 0; i < mensajes.length; i++) {
                mensajes[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);

                String idMensaje = mensajes[i].getOriginatingAddress();
                String textoMensaje = mensajes[i].getMessageBody();

                Log.i("ReceptorSMS", "Remitente: " + idMensaje);
                Log.i("ReceptorSMS", "Mensaje: " + textoMensaje);
            }
        }
    }

}