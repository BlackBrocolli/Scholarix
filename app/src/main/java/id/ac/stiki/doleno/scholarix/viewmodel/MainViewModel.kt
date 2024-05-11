package id.ac.stiki.doleno.scholarix.viewmodel

import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import id.ac.stiki.doleno.scholarix.model.Beasiswa
import id.ac.stiki.doleno.scholarix.navigation.Screen

class MainViewModel : ViewModel() {

    private val _currentScreen: MutableState<Screen> = mutableStateOf(Screen.BottomScreen.Home)

    val currentScreen: MutableState<Screen>
        get() = _currentScreen

    fun setCurrentScreen(screen: Screen) {
        _currentScreen.value = screen
    }

    // Tambahkan properti inputUrutkanBerdasar
    private val _inputUrutkanBerdasar: MutableState<String> = mutableStateOf("Deadline Terdekat")
    val inputUrutkanBerdasar: MutableState<String>
        get() = _inputUrutkanBerdasar

    fun setInputUrutkanBerdasar(value: String) {
        _inputUrutkanBerdasar.value = value
    }

    // == GET SCHOLARSHIPS FROM DB ==
    private val db = FirebaseFirestore.getInstance()

    private val _scholarships = MutableLiveData<List<Beasiswa>>()
    val scholarships: LiveData<List<Beasiswa>> = _scholarships

    // Variabel untuk menyimpan satu beasiswa saja
    private val _scholarship = MutableLiveData<Beasiswa>()
    val scholarship: LiveData<Beasiswa> = _scholarship

    private val _isLoadingAllScholarships = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoadingAllScholarships

    private val _isErrorAllScholarships = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isErrorAllScholarships

    // Variabel untuk status loading ambil satu beasiswa
    private val _isLoadingScholarship = MutableLiveData<Boolean>()
    val isLoadingScholarship: LiveData<Boolean> = _isLoadingScholarship

    // Variabel untuk status error ambil satu beasiswa
    private val _isErrorScholarship = MutableLiveData<Boolean>()
    val isErrorScholarship: LiveData<Boolean> = _isErrorScholarship

    fun fetchScholarshipDetails() {
        _isLoadingAllScholarships.value = true
        _isErrorAllScholarships.value = false
        db.collection("scholarships")
            .get()
            .addOnSuccessListener { documents ->
                _isLoadingAllScholarships.value = false
                val scholarshipList = documents.mapNotNull { doc ->
                    doc.toObject(Beasiswa::class.java).apply { id = doc.id }
                }
                _scholarships.value = scholarshipList
            }
            .addOnFailureListener {
                _isLoadingAllScholarships.value = false
                _isErrorAllScholarships.value = true
            }
    }

    // Metode untuk mengambil satu beasiswa berdasarkan ID dari database
    fun fetchScholarshipById(encodedId: String) {
        val id = Uri.decode(encodedId) // Decode ID yang telah di-encoded sebelumnya
        _isLoadingScholarship.value = true
        _isErrorScholarship.value = false
        db.collection("scholarships").document(id)
            .get()
            .addOnSuccessListener { document ->
                _isLoadingScholarship.value = false
                if (document.exists()) {
                    val beasiswa = document.toObject(Beasiswa::class.java)
                    beasiswa?.let {
                        _scholarship.value = it
                    }
                } else {
                    _isErrorScholarship.value = true
                }
            }
            .addOnFailureListener {
                _isLoadingScholarship.value = false
                _isErrorScholarship.value = true
            }
    }
}