package com.asp424.tristagramcompose.notification

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentResolver
import android.content.Intent
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.Person
import androidx.core.graphics.drawable.IconCompat
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.asp424.tristagramcompose.MainActivity
import com.asp424.tristagramcompose.R
import com.asp424.tristagramcompose.application.appComponent
import com.asp424.tristagramcompose.data.firebase.models.UserModel
import com.asp424.tristagramcompose.data.firebase.utils.AppValueEventListener
import com.asp424.tristagramcompose.data.firebase.utils.getPhoneContactModel
import com.asp424.tristagramcompose.data.repos.main_list.MainListRepo
import com.asp424.tristagramcompose.utils.NODE_PHONES_CONTACTS
import com.asp424.tristagramcompose.utils.log
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.*
import javax.inject.Inject


var state = 0

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class FirebaseNotificationService : FirebaseMessagingService() {
    @Inject
    lateinit var mainRepo: MainListRepo

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onCreate() {
        appComponent.inject(this)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
        log("GET")
        if (state == 0)
            checkForAuth {
                if (it) {
                    updateMainListRoom {
                        val id = p0.data["userUid"]
                        val fullName = p0.data["fullName"]
                        val textMessage = p0.data["textMessage"]
                        val photoUrl = p0.data["photoUrl"]
                        if (id != null && textMessage != null && photoUrl != null) {
                            if (fullName != null)
                                FirebaseDatabase.getInstance()
                                    .reference.child(NODE_PHONES_CONTACTS)
                                    .child(FirebaseAuth.getInstance().currentUser?.uid.toString())
                                    .child(id)
                                    .addListenerForSingleValueEvent(AppValueEventListener { ass ->
                                        if (ass.getPhoneContactModel().fullname.isEmpty())
                                            addNotification(textMessage, id, fullName, photoUrl)
                                        else addNotification(
                                            textMessage, id,
                                            ass.getPhoneContactModel().fullname, photoUrl
                                        )
                                    })
                        }
                    }
                }
            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createMessageChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.not)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val soundBuilder =
                AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION).build()
            val sound =
                Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + this.packageName + "/" + R.raw.a);
            val channel = NotificationChannel(
                resources.getString(R.string.default_notification_channel_id),
                name,
                importance
            ).apply {
                setSound(sound, soundBuilder)
                description = "Call Notifications";
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC
                vibrationPattern = longArrayOf(200, 200, 100, 100, 50, 100, 300, 500)
            }
            val notificationManager: NotificationManager =
                getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("UnspecifiedImmutableFlag", "RemoteViewLayout")
    private fun addNotification(text: String, from: String, name: String, photoUrl: String) =
        CoroutineScope(Dispatchers.IO).launch {
            createMessageChannel()
            val contentIntent = PendingIntent.getActivity(
                this@FirebaseNotificationService, 0,
                Intent(this@FirebaseNotificationService, MainActivity::class.java).apply {
                    putExtra("id", from)
                    putExtra("name", name)
                }, PendingIntent.FLAG_UPDATE_CURRENT
            )
            val replyIntent =
                Intent(this@FirebaseNotificationService, MainActivity::class.java).apply {
                    putExtra("ass", from)
                }
            val replyPendingIntent = PendingIntent.getService(
                this@FirebaseNotificationService,
                123, replyIntent, 0
            )
            if (photoUrl != "null" && photoUrl.isNotEmpty()) {
                val futureTarget = Glide.with(this@FirebaseNotificationService)
                    .asBitmap()
                    .load(photoUrl)
                    .circleCrop()
                    .submit()
                val person = Person.Builder()
                    .setName(name).setKey("Личное сообщение")
                    .setIcon(IconCompat.createWithBitmap(futureTarget.get())).build()
                val builder = NotificationCompat
                    .Builder(
                        this@FirebaseNotificationService, resources.getString(
                            R.string.default_notification_channel_id
                        )
                    )
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.logo)
                    .setContentIntent(contentIntent)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .addAction(1, "Ok", replyPendingIntent)
                    .addAction(1, "Was read", replyPendingIntent)
                    .setStyle(
                        NotificationCompat.MessagingStyle(person)
                            .setConversationTitle(person.key)
                            .addMessage(text, Calendar.getInstance().time.time, person)
                    )
                Glide.with(this@FirebaseNotificationService).clear(futureTarget)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    with(NotificationManagerCompat.from(this@FirebaseNotificationService)) {
                        notify(R.string.default_notification_channel_id, builder.build())
                    }
                } else {
                    builder.setSound(Uri.parse("android.resource://" + packageName + "/" + R.raw.a))
                    val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                    manager.notify(R.string.default_notification_channel_id, builder.build())
                }
            } else {
                val builder = NotificationCompat
                    .Builder(
                        this@FirebaseNotificationService, resources.getString(
                            R.string.default_notification_channel_id
                        )
                    )
                    .setAutoCancel(true)
                    .setContentTitle(name)
                    .setContentText(text)
                    .setSmallIcon(R.drawable.logo)
                    .setContentIntent(contentIntent)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .addAction(1, "Ok", replyPendingIntent)
                    .addAction(1, "Was read", replyPendingIntent)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    with(NotificationManagerCompat.from(this@FirebaseNotificationService)) {
                        notify(R.string.default_notification_channel_id, builder.build())
                    }
                } else {
                    builder.setSound(Uri.parse("android.resource://" + packageName + "/" + R.raw.a))
                    val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                    manager.notify(R.string.default_notification_channel_id, builder.build())
                }
            }
        }

    private fun checkForAuth(result: (Boolean) -> Unit) {
        if (FirebaseAuth.getInstance().currentUser != null) result(true) else result(false)
    }

    private fun updateMainListRoom(function: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            mainRepo.updateRoom {
                function()
                cancel()
            }
        }
    }

    
}






