package id.ac.stiki.doleno.scholarix.model.auth

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class UserRepository(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {
    suspend fun signUp(
        email: String,
        password: String,
        namaLengkap: String,
        noHandphone: String,
    ): Result<Boolean> = withContext(Dispatchers.IO) {
        try {
            auth.createUserWithEmailAndPassword(email, password).await()
            Log.d("UserRepository", "User created with email: $email")
            // Add user to Firestore
            val user = User(namaLengkap, email, noHandphone)
            saveUserToFirestore(user)
            Result.Success(true)
        } catch (e: Exception) {
            Log.e("UserRepository", "Error in sign up", e)
            Result.Error(e)
        }
    }

    private suspend fun saveUserToFirestore(user: User) = withContext(Dispatchers.IO) {
        try {
            firestore.collection("users").document(user.email).set(user).await()
            Log.d("UserRepository", "User saved to Firestore with email: ${user.email}")
        } catch (e: Exception) {
            Log.e("UserRepository", "Error saving user to Firestore", e)
            throw e
        }
    }

    suspend fun login(email: String, password: String): Result<Boolean> = withContext(Dispatchers.IO) {
        try {
            auth.signInWithEmailAndPassword(email, password).await()
            Result.Success(true)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}

