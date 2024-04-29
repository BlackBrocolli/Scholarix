package id.ac.stiki.doleno.scholarix.model.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepository(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {
    suspend fun signUp(
        email: String,
        password: String,
        namaLengkap: String,
        noHandphone: String,
    ): Result<Boolean> =
        try {
            auth.createUserWithEmailAndPassword(email, password).await()
            //add user to firestore
            val user = User(namaLengkap, email, noHandphone)
            saveUserToFirestore(user)
            Result.Success(true)
        } catch (e: Exception) {
            Result.Error(e)
        }

    private suspend fun saveUserToFirestore(user: User) {
        firestore.collection("users").document(user.email).set(user).await()
    }
}
