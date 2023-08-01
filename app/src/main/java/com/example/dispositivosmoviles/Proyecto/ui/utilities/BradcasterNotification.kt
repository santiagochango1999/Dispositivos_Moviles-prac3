package com.example.dispositivosmoviles.Proyecto.ui.utilities

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import com.example.dispositivosmoviles.Proyecto.ui.activities.CameraActivity
import com.example.dispositivosmoviles.R

class BradcasterNotification : BroadcastReceiver() {
    val CHANNEL: String = "Notificaciones"

    override fun onReceive(context: Context, intent: Intent) {

        val myIntent = Intent(context, CameraActivity::class.java)
        val myPendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            myIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notif = NotificationCompat.Builder(context, CHANNEL)

        notif.setContentTitle(" BROADCAST:v")
        notif.setContentText("TIENES UNA NOTIFICACION :v")
        notif.setSmallIcon(R.drawable.baseline_favorite_24)
        notif.setPriority(NotificationCompat.PRIORITY_DEFAULT)

        notif.setStyle(
            NotificationCompat.BigTextStyle()
                .bigText("Esta es una notificacion en android que no se te olvide xd")
        )

        notif.setContentIntent(myPendingIntent)
        notif.setAutoCancel(true)

        //crear un manager de tipo broadcast
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(
            1,
            notif.build()
        )

    }
}