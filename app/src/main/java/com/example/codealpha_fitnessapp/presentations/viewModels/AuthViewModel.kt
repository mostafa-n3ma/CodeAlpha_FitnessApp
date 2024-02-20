package com.example.codealpha_fitnessapp.presentations.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.codealpha_fitnessapp.operations.dataMangment.FitRepository
import com.example.codealpha_fitnessapp.operations.dataMangment.User
import com.example.codealpha_fitnessapp.operations.dataMangment.WeightRecord
import com.example.codealpha_fitnessapp.operations.getCurrentDate
import com.example.codealpha_fitnessapp.operations.validateEmail
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel
@Inject
constructor(private val fitRepository: FitRepository) : ViewModel() {

    companion object {
        const val TAG = "AuthViewModel"
    }


    var firebaseAuth: FirebaseAuth


    private val _announceMessage = MutableStateFlow<String>("")
    val announceMessage: StateFlow<String> = _announceMessage

    fun announceMessage(msg: String) {
        _announceMessage.update {
            Log.d("announcment test  announceMessage()", "update to msg : $msg")
            msg
        }
    }


    private val _user = MutableStateFlow<FirebaseUser?>(null)
    val user: StateFlow<FirebaseUser?> = _user


    init {
        firebaseAuth = Firebase.auth
        _user.update { firebaseAuth.currentUser }
        checkAndUpdateUserStatus()
//        signOut()
    }


    fun checkAndUpdateUserStatus() {
        _user.update { firebaseAuth.currentUser }
    }


    fun signOut() {
        firebaseAuth.signOut()
        checkAndUpdateUserStatus()
    }


    fun validateEmailAndPassword(email: String, password: String): Boolean {
        Log.d(
            "announcment test  ",
            "validateEmailAndPassword() email : $email // password: $password  "
        )
        if (email.trim().isEmpty()) {
            announceMessage("email is empty")
            Log.d("announcment test", ":validateEmailAndPassword() email is empty")
            return false
        }
        if (password.trim().isEmpty()) {
            announceMessage("password is empty")
            Log.d("announcment test", "validateEmailAndPassword: password is empty")

            return false
        }
        if (!validateEmail(email)) {
            announceMessage("email is not valid!")
            Log.d("announcment test", "validateEmailAndPassword: email is not valid!")
            return false
        }

        if (password.length < 6) {
            announceMessage("password should not be less then 6 characters ")
            Log.d(
                "announcment test",
                "validateEmailAndPassword: password should not be less then 6 characters"
            )
            return false
        }

        Log.d("announcment test", "validateEmailAndPassword: nothing is wroong returning true")

        return true

    }


    //sign upScreen
    fun createUser(
        name: String,
        weight:Double,
        email: String,
        password: String
    ) {
        if (validateEmailAndPassword(email, password)) {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    if (it != null) {
                        setEvent(AuthEvents.UpdateUserStatusEvent)
                        Log.d(TAG, "createUserWithEmailAndPassword: success creating account ")
                        viewModelScope.launch(Dispatchers.Default) {
                            val weightRecord = listOf(WeightRecord(weight = weight, lastUpdate = getCurrentDate()))
                            fitRepository.insertUser(User(name = name, email = email, weight = weightRecord))
                        }
                    }
                }
                .addOnFailureListener {
                    Log.d(
                        TAG,
                        "createUserWithEmailAndPassword: failing creating account ///e: ${it.message}"
                    )
                    announceMessage("${it.message}")
                    checkAndUpdateUserStatus()
                }
        }

    }


    // sign in Screen
    fun logIn(email: String, password: String) {
        if (validateEmailAndPassword(email, password)) {
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    checkAndUpdateUserStatus()
                    Log.d(TAG, "signIn: Successfully signed in ")
                }
                .addOnFailureListener {
                    Log.d(TAG, "signIn: Fail ${it.message}")
                    announceMessage("${it.message}")
                }
        }


    }

    fun setEvent(event: AuthEvents) {
        when (event) {
            is AuthEvents.AnnouncementEvent -> {
                announceMessage(event.msg)
            }

            is AuthEvents.CreateUSerEvent -> {
                createUser(name = event.name, weight = event.weight, email = event.email, password = event.pass)
            }

            is AuthEvents.LogInInEvent -> {
                logIn(event.email, event.pass)
            }

            is AuthEvents.UpdateUserStatusEvent -> {
                checkAndUpdateUserStatus()
            }
        }
    }

}

sealed class AuthEvents() {
    data class AnnouncementEvent(val msg: String = "") : AuthEvents()
    data object UpdateUserStatusEvent : AuthEvents()
    data class CreateUSerEvent(val name: String,val weight:Double, val email: String, val pass: String) : AuthEvents()

    data class LogInInEvent(val email: String, val pass: String) : AuthEvents()

}