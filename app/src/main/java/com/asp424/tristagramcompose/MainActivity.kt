package com.asp424.tristagramcompose

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import com.asp424.tristagramcompose.application.appComponent

import com.asp424.tristagramcompose.navigator.MainScreenAct
import com.asp424.tristagramcompose.navigator.NavHostCreate
import com.asp424.tristagramcompose.navigator.RegistrationAct
import com.asp424.tristagramcompose.notification.remote_message.retrofit.ApiClient
import com.asp424.tristagramcompose.notification.remote_message.retrofit.ApiService
import com.asp424.tristagramcompose.notification.state
import com.asp424.tristagramcompose.permissions.READ_CONTACTS
import com.asp424.tristagramcompose.permissions.checkPermissions
import com.asp424.tristagramcompose.ui.theme.MyTheme
import com.asp424.tristagramcompose.utils.FOLDER_PROFILE_IMAGE
import com.asp424.tristagramcompose.utils.log
import com.asp424.tristagramcompose.viewmodels.*
import com.google.firebase.messaging.FirebaseMessaging
import com.theartofdev.edmodo.cropper.CropImage
import dagger.Lazy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent.setEventListener
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject


class MainActivity : ComponentActivity() {
    @Inject
    lateinit var viewModelFactory: Lazy<ViewModelProvider.Factory>
    private var settingsViewModel: SettingsViewModel? = null
    private var mainViewModel: MainViewModel? = null

    var selectImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { resultPhoto ->
            if (resultPhoto.resultCode == Activity.RESULT_OK && resultPhoto.data != null) {
                settingsViewModel?.putPhotoToStorage(
                    CropImage.getActivityResult(resultPhoto.data)
                        .uri, FOLDER_PROFILE_IMAGE
                )
            }
        }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()
        // WindowCompat.setDecorFitsSystemWindows (window, false)
        if (checkPermissions(READ_CONTACTS, this)) {
            start(intent)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        this.intent = intent
    }

    @OptIn(ExperimentalCoroutinesApi::class, kotlin.ExperimentalStdlibApi::class)
    @RequiresApi(Build.VERSION_CODES.O)
    fun start(intent: Intent?) {
        appComponent.inject(this)
        mainViewModel = ViewModelProvider(
            this, viewModelFactory.get()
        )[MainViewModel::class.java]
        if (mainViewModel != null)
            lifecycle.addObserver(mainViewModel!!)
        state = 1
        settingsViewModel = ViewModelProvider(
            this,
            viewModelFactory.get()
        )[SettingsViewModel::class.java]
        val singleChatViewModel = ViewModelProvider(
            this,
            viewModelFactory.get()
        )[SingleChatViewModel::class.java]
        setEventListener(
            this,
            this
        ) {
            singleChatViewModel.keyBoardState.value = it
        }

        val mainScreenViewModel = ViewModelProvider(
            this,
            viewModelFactory.get()
        )[MainScreenViewModel::class.java]
        val navigator = ViewModelProvider(
            this,
            viewModelFactory.get()
        )[NavViewModel::class.java]
        val registerViewModel = ViewModelProvider(
            this,
            viewModelFactory.get()
        )[RegisterViewModel::class.java]

        setContent {
            MyTheme {
                NavHostCreate(
                        mainViewModel!!,
                        singleChatViewModel,
                        mainScreenViewModel,
                        registerViewModel,
                        settingsViewModel!!
                    )
                }
        }
        CoroutineScope(Dispatchers.Main).launch {
            mainViewModel!!.checkForAuth {
                if (it) {
                    if (intent!!.hasExtra("id")) {
                        val id = intent.extras!!["id"]
                        val fullName = intent.extras!!["name"]
                        if (id != null && fullName != null) {
                            singleChatViewModel.goToSingleChat(
                                id.toString(),
                                fullName.toString(), MainScreenAct
                            ) {
                                intent.removeExtra("id")
                                intent.removeExtra("name")
                            }
                        }
                    }
                } else {
                    navigator.toScreen(RegistrationAct)
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        state = 0
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        start(intent)
    }
}






