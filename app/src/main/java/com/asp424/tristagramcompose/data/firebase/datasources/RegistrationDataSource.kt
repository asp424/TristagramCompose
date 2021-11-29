package com.asp424.tristagramcompose.data.firebase.datasources


import androidx.compose.ui.graphics.toArgb
import com.asp424.tristagramcompose.MainActivity
import com.asp424.tristagramcompose.data.firebase.utils.AppValueEventListener
import com.asp424.tristagramcompose.data.firebase.utils.getUserModel
import com.asp424.tristagramcompose.data.repos.registration.RegViewStates
import com.asp424.tristagramcompose.data.repos.registration.RegistrationInterface
import com.asp424.tristagramcompose.ui.theme.listColors
import com.asp424.tristagramcompose.utils.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class RegistrationDataSource @Inject constructor() : RegistrationInterface {
    private val base = FirebaseDatabase.getInstance().reference
    private val auth = FirebaseAuth.getInstance()
    private var idReg: String? = null
    private var tempPhone: String? = null

    override fun checkForAuth(result: (Boolean) -> Unit) {
        if (auth.currentUser != null) result(true) else result(false)
    }

    override fun signOut(){
        auth.signOut()
    }

    override fun startRegistrationByPhone(
        mainActivityComponent: MainActivity,
        phoneNumber: String,
        result: (Int) -> Unit
    ) {
        tempPhone = phoneNumber
        val callback = object : PhoneAuthProvider
        .OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(
                credential: PhoneAuthCredential
            ) {
                startAuthorisationByPhone(code = credential.smsCode.toString()) { _ ->
                    createNodes() { success ->
                        if (success == 1)
                            result(credential.smsCode!!.toInt())
                        else result(0)
                    }
                }
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                result(0)
            }

            override fun onCodeSent(
                id: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) { RegViewStates.OnCodeSent(id)
                idReg = id
                result(1)
            }
        }
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setCallbacks(callback)
            .setActivity(mainActivityComponent)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    override fun startAuthorisationByPhone(code: String, result: (Int) -> Unit) {
        auth.signInWithCredential(PhoneAuthProvider.getCredential(idReg!!, code))
            .addOnCompleteListener { task ->
                if (task.isSuccessful)
                    createNodes() { success ->
                        if (success == 1)
                            result(1)
                        else result(2)
                    }
            }
            .addOnFailureListener {
                result(0)
            }
    }

    private fun createNodes(success: (Int) -> Unit) {

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                success(0)
                return@OnCompleteListener
            } else {
                val currentUser = auth.currentUser?.uid.toString()
                val dateMap = mutableMapOf<String, Any>()
                base.child(NODE_USERS).child(currentUser).addListenerForSingleValueEvent(AppValueEventListener{
                    if (it.getUserModel().color.isEmpty())
                        dateMap["color"] = listColors.random().toArgb().toString()
                })
                dateMap[CHILD_PHONE] = tempPhone.toString()
                dateMap[CHILD_TOKEN] = task.result.toString()
                dateMap[CHILD_ID] = currentUser
                base.child(NODE_PHONES).child(tempPhone!!).setValue(currentUser)
                    .addOnCompleteListener {
                        base.child(NODE_USERS).child(currentUser)
                            .addListenerForSingleValueEvent(AppValueEventListener { it_is ->
                                if (!it_is.hasChild(CHILD_USERNAME)) {
                                    dateMap[CHILD_USERNAME] = currentUser
                                }
                                base.child(NODE_USERS).child(currentUser).updateChildren(
                                    dateMap
                                ).addOnSuccessListener { success(1) }
                                .addOnFailureListener {
                                    success(0)
                                }
                                .addOnCanceledListener {
                                    success(0)
                                }
                            })
            }
                .addOnFailureListener {
                    success(0)
                }
                .addOnCanceledListener {
                    success(0)
                }
        }
    })
}
}