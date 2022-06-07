package com.yash.shortnotes.helper

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.yash.shortnotes.R
import com.yash.shortnotes.activity.AlarmActivity

class AlarmReceiver : BroadcastReceiver() {

    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onReceive(context: Context?, intent: Intent?) {

        val noteTitle = intent!!.getStringExtra(KeyClass.KEY_PENDING_TITLE)
        val noteDescription = intent.getStringExtra(KeyClass.KEY_PENDING_DESCRIPTION)
        val noteDate = intent.getStringExtra(KeyClass.KEY_PENDING_DATE)
        val noteTime = intent.getStringExtra(KeyClass.KEY_PENDING_TIME)

        val i = Intent(context,AlarmActivity::class.java)
        i.putExtra(KeyClass.KEY_PENDING_TITLE1,noteTitle)
        i.putExtra(KeyClass.KEY_PENDING_DESCRIPTION1,noteDescription)
        i.putExtra(KeyClass.KEY_PENDING_DATE1,noteDate)
        i.putExtra(KeyClass.KEY_PENDING_TIME1,noteTime)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context!!.startActivity(i)

        val pendingIntent = PendingIntent.getActivity(context,0,i,0)

        val builder = NotificationCompat.Builder(context,"alarmId")
            .setSmallIcon(R.drawable.ic_notifications)
//            .setContentTitle("Alarm Reminder")
//            .setContentText("Channel For Alarm Manager")
            .setContentTitle("$noteTitle")
            .setContentText("$noteDescription")
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
//            .addAction(R.drawable.ic_notifications,"Stop",pendingIntent)

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(123,builder.build())
    }
}