package com.example.dispositivosmoviles.Proyecto.ui.activities

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.dispositivosmoviles.R
import com.example.dispositivosmoviles.databinding.ActivityNotificactionBinding
import java.util.zip.Inflater

class NotificactionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNotificactionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificactionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnNotification.setOnClickListener {
//            solo se crea una vez el canal
//            createNotificationChannel()
            senNotificaction()
        }
    }


    val CHANNEL: String = "Notificaciones"

    //DEFINIMOS EL CANAL Y SU IMPORTANCIA
    //podemos tener varios canales
    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Variedades"
            val descriptionText = "Notificaciones simples de variedades"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    //DEFINIMOS SOLO LA NOTIFICACION
    @SuppressLint("MissingPermission")
    fun senNotificaction() {
        //CUERPO DE LA NOTIFICACION
        val noti = NotificationCompat.Builder(this, CHANNEL)
        noti.setContentTitle("Primer notificacion")
        noti.setContentText("Tienes una NOTIFICACION")
        noti.setSmallIcon(R.drawable.icons8_facebook_13)
        noti.setPriority(NotificationCompat.PRIORITY_DEFAULT)

        noti.setStyle(
            NotificationCompat.BigTextStyle()
                .bigText("Esta es una notificacion para recordar que estamos trabajando en Android")
        )

        //ENVIA LA NOTIFICACION
        with(NotificationManagerCompat.from(this)) {
            notify(1, noti.build())
        }

    }
}