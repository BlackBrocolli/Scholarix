package id.ac.stiki.doleno.scholarix.viewmodel

import android.net.Uri
import android.util.Log
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

    // Jumlah beasiswa di firebase
    private val _totalScholarshipsCount = MutableLiveData<Int>()
    val totalScholarshipsCount: LiveData<Int> = _totalScholarshipsCount

    // == Start of Searching variables
    // Variabel yang menyimpan hasil pencarian
    private val _searchedScholarships = MutableLiveData<List<Beasiswa>>()
    val searchedScholarships: LiveData<List<Beasiswa>> = _searchedScholarships

    // Jumlah beasiswa hasil pencarian
    private var _totalSearchedScholarshipsCount = MutableLiveData<Int>()
    val totalSearchedScholarshipsCount: LiveData<Int> = _totalSearchedScholarshipsCount

    private val _isSearching = MutableLiveData<Boolean>()
    val isSearching: LiveData<Boolean> = _isSearching

    fun resetSearching() {
        _isSearching.value = false
        _searchText.value = ""
    }

    private val _searchText = MutableLiveData("")
    val searchText: LiveData<String> = _searchText

    fun setSearchText(text: String) {
        _searchText.value = text
    }
    // == End of Searching variables

    // == Start of FILTERING ==
    // TODO: add variables and functions for filtering
    // == End of FILTERING ==

    // Get ALL scholarships from firebase
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
                _totalScholarshipsCount.value = documents.size()
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

    // Search Scholarships
    fun searchScholarshipsByName(name: String) {
        _isSearching.value = true
        _searchedScholarships.value = _scholarships.value?.filter { scholarship ->
            scholarship.name.contains(name, ignoreCase = true)
        } ?: emptyList()

        _totalSearchedScholarshipsCount.value = _searchedScholarships.value!!.size
    }
}