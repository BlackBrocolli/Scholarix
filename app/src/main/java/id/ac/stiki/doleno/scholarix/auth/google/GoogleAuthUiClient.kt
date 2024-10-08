package id.ac.stiki.doleno.scholarix.auth.google

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest.GoogleIdTokenRequestOptions
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import id.ac.stiki.doleno.scholarix.R
import id.ac.stiki.doleno.scholarix.model.google.SignInResult
import id.ac.stiki.doleno.scholarix.model.google.UserData
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CancellationException

class GoogleAuthUiClient(
    private val context: Context,
    private val oneTapClient: SignInClient
) {
    private val _auth = Firebase.auth

    suspend fun signIn(): IntentSender? {
        val result = try {
            oneTapClient.beginSignIn(
                buildSignInRequest()
            ).await()
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            null
        }
        return result?.pendingIntent?.intentSender
    }

    suspend fun signInWithIntent(intent: Intent): SignInResult {
        val credential = oneTapClient.getSignInCredentialFromIntent(intent)
        val googleIdToken = credential.googleIdToken
        val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)
//        return try {
//            val user = _auth.signInWithCredential(googleCredentials).await().user
//            SignInResult(
//                data = user?.run {
//                    UserData(
//                        userId = uid,
//                        username = displayName,
//                        profilePictureUrl = photoUrl?.toString(),
//                        email = email
//                    )
//                }, errorMessage = null
//            )
//        } catch (e: Exception) {
//            e.printStackTrace()
//            if (e is CancellationException) throw e
//            SignInResult(
//                data = null,
//                errorMessage = e.message
//
//            )
//        }
        return try {
            val user = _auth.signInWithCredential(googleCredentials).await().user
            val userData = user?.run {
                UserData(
                    userId = uid,
                    username = displayName,
                    profilePictureUrl = photoUrl?.toString(),
                    email = email
                )
            }
            userData?.let {
                saveUserToFirestore(it) // Save user data to Firestore
            }
            SignInResult(
                data = userData,
                errorMessage = null
            )
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            SignInResult(
                data = null,
                errorMessage = e.message
            )
        }
    }

    suspend fun signOut() {
        try {
            oneTapClient.signOut().await()
            _auth.signOut()
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
        }
    }

    fun getSignInUser(): UserData? = _auth.currentUser?.run {
        UserData(
            userId = uid,
            username = displayName,
            profilePictureUrl = photoUrl?.toString(),
            email = email
        )
    }

    private fun buildSignInRequest(): BeginSignInRequest {
        return BeginSignInRequest.Builder()
            .setGoogleIdTokenRequestOptions(
                GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(context.getString(R.string.default_web_client_id)).build()
            )
            .setAutoSelectEnabled(true)
            .build()
    }

    private val firestore = FirebaseFirestore.getInstance()

    private suspend fun saveUserToFirestore(user: UserData) {

        val userDocRef = firestore.collection("users").document(user.email!!)

        try {
            val document = userDocRef.get().await()

            if (!document.exists()) {
                val userMap = mapOf(
                    "namaLengkap" to user.username,
                    "email" to user.email,
                    "noHandphone" to "",
                    "profilePictureUrl" to user.profilePictureUrl,
                    "favorites" to emptyList<String>()
                )
                // Dokumen belum ada, lakukan set untuk membuat dokumen baru.
                userDocRef.set(userMap).await()
                // User data saved successfully
            }
            // Jika dokumen sudah ada, tidak perlu melakukan apa-apa.
        } catch (e: Exception) {
            e.printStackTrace()
            // Handle failure to check document existence or to save user data
        }
    }
}