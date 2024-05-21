package id.ac.stiki.doleno.scholarix.viewmodel

import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import id.ac.stiki.doleno.scholarix.model.Beasiswa
import id.ac.stiki.doleno.scholarix.model.auth.User
import id.ac.stiki.doleno.scholarix.navigation.Screen

class MainViewModel : ViewModel() {

    private val _currentScreen: MutableState<Screen> = mutableStateOf(Screen.BottomScreen.Home)

    val currentScreen: MutableState<Screen>
        get() = _currentScreen

//    fun setCurrentScreen(screen: Screen) {
//        _currentScreen.value = screen
//    }

    // == Start of SORTING ==
    private val _inputUrutkanBerdasar: MutableState<String> = mutableStateOf("Nama A-Z")
    val inputUrutkanBerdasar: MutableState<String>
        get() = _inputUrutkanBerdasar

    fun updateSortingPreference(value: String) {
        _inputUrutkanBerdasar.value = value
        sortScholarships()
    }

    private fun sortScholarships() {
        val sortByDeadline: (Beasiswa) -> String? = { it.deadline }
        val sortByName: (Beasiswa) -> String = { it.name }

        _scholarships.value = _scholarships.value?.let { list ->
            when (_inputUrutkanBerdasar.value) {
                "Deadline Terdekat" -> list.sortedBy(sortByDeadline)
                "Deadline Terjauh" -> list.sortedByDescending(sortByDeadline)
                "Nama A-Z" -> list.sortedBy(sortByName)
                "Nama Z-A" -> list.sortedByDescending(sortByName)
                else -> list
            }
        }

        _searchedScholarships.value = _searchedScholarships.value?.let { list ->
            when (_inputUrutkanBerdasar.value) {
                "Deadline Terdekat" -> list.sortedBy(sortByDeadline)
                "Deadline Terjauh" -> list.sortedByDescending(sortByDeadline)
                "Nama A-Z" -> list.sortedBy(sortByName)
                "Nama Z-A" -> list.sortedByDescending(sortByName)
                else -> list
            }
        }

        _filteredScholarships.value = _filteredScholarships.value?.let { list ->
            when (_inputUrutkanBerdasar.value) {
                "Deadline Terdekat" -> list.sortedBy(sortByDeadline)
                "Deadline Terjauh" -> list.sortedByDescending(sortByDeadline)
                "Nama A-Z" -> list.sortedBy(sortByName)
                "Nama Z-A" -> list.sortedByDescending(sortByName)
                else -> list
            }
        }
    }
    // == End of SORTING ==

    // == GET SCHOLARSHIPS FROM DB ==
    private val db = FirebaseFirestore.getInstance()

    private val _scholarships = MutableLiveData<List<Beasiswa>>()
    val scholarships: MutableLiveData<List<Beasiswa>> = _scholarships

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


    // -------- Start of SEARCHING ----------
    // Variabel yang menyimpan hasil pencarian
    private val _searchedScholarships = MutableLiveData<List<Beasiswa>>()
    val searchedScholarships: LiveData<List<Beasiswa>> = _searchedScholarships

    // Jumlah beasiswa hasil pencarian
    private var _totalSearchedScholarshipsCount = MutableLiveData<Int>()
    val totalSearchedScholarshipsCount: LiveData<Int> = _totalSearchedScholarshipsCount

    private val _isSearching = MutableLiveData<Boolean>()
    val isSearching: LiveData<Boolean> = _isSearching

    private val _searchText = MutableLiveData("")
    val searchText: LiveData<String> = _searchText

    fun resetSearching() {
        _isSearching.value = false
        _searchText.value = ""
    }

    fun setSearchText(text: String) {
        _searchText.value = text
    }
    fun searchScholarshipsByName(name: String) {
        _isSearching.value = true
        _searchedScholarships.value = _scholarships.value?.filter { scholarship ->
            scholarship.name.contains(name, ignoreCase = true)
        } ?: emptyList()

        _totalSearchedScholarshipsCount.value = _searchedScholarships.value!!.size
        resetCurrentPage()
    }
    // ------- End of Searching ---------

    // ----------- Start of PAGINATION ----------
    private var _currentPage = MutableLiveData(0)
    val currentPage: LiveData<Int> = _currentPage
    fun setCurrentPage(value: Int) {
        _currentPage.value = _currentPage.value?.plus(value)
    }

    private fun resetCurrentPage() {
        _currentPage.value = 0
    }
    // ----------- End of PAGINATION ---------

    // == Start of FILTERING ==
    // TODO: add variables and functions for filtering

    // State to track the selected status of each card
    private val _selectedCards = MutableLiveData<Map<String, Boolean>>()
    val selectedCards: LiveData<Map<String, Boolean>> = _selectedCards

    init {
        // Initialize with default values (all cards unselected)
        _selectedCards.value = mapOf(
            "S1" to false,
            "S2" to false,
            "S3" to false,
            "Lainnya" to false,
            "Fully Funded" to false,
            "Partial Funded" to false,
        )
    }

    fun toggleCardSelection(card: String) {
        _selectedCards.value = _selectedCards.value?.mapValues { entry ->
            if (entry.key == card) !entry.value else entry.value
        }
    }

    fun resetCardSelections() {
        _selectedDegrees.value = emptyList()
        _selectedFundingStatus.value = emptyList()
        _selectedCards.value = _selectedCards.value?.mapValues { false }
    }

    private val _isFiltering = MutableLiveData<Boolean>()
    val isFiltering: LiveData<Boolean> = _isFiltering

    fun setFiltering(boolean: Boolean) {
        _isFiltering.value = boolean
    }

    private val _selectedDegrees = MutableLiveData<List<String>>(emptyList())
    private val _selectedFundingStatus = MutableLiveData<List<String>>(emptyList())

    private val _filteredScholarships = MutableLiveData<List<Beasiswa>>()
    val filteredScholarships: LiveData<List<Beasiswa>> = _filteredScholarships

    private var _totalFilteredScholarshipsCount = MutableLiveData<Int>()
    val totalFilteredScholarshipsCount: LiveData<Int> = _totalFilteredScholarshipsCount

    private fun addSelectedDegree(degree: String) {
        val currentDegrees = _selectedDegrees.value?.toMutableList() ?: mutableListOf()
        if (!currentDegrees.contains(degree)) {
            currentDegrees.add(degree)
            _selectedDegrees.value = currentDegrees
        }
    }

    private fun removeSelectedDegree(degree: String) {
        val currentDegrees = _selectedDegrees.value?.toMutableList() ?: mutableListOf()
        if (currentDegrees.contains(degree)) {
            currentDegrees.remove(degree)
            _selectedDegrees.value = currentDegrees
        }
    }

    private fun addSelectedFundingStatus(fundingStatus: String) {
        val currentFundingStatus = _selectedFundingStatus.value?.toMutableList() ?: mutableListOf()
        if (!currentFundingStatus.contains(fundingStatus)) {
            currentFundingStatus.add(fundingStatus)
            _selectedFundingStatus.value = currentFundingStatus
        }
    }

    private fun removeSelectedFundingStatus(fundingStatus: String) {
        val currentFundingStatus = _selectedFundingStatus.value?.toMutableList() ?: mutableListOf()
        if (currentFundingStatus.contains(fundingStatus)) {
            currentFundingStatus.remove(fundingStatus)
            _selectedFundingStatus.value = currentFundingStatus
        }
    }

    fun toggleDegreeSelection(degree: String) {
        if (_selectedDegrees.value?.contains(degree) == true) {
            removeSelectedDegree(degree)
        } else {
            addSelectedDegree(degree)
        }
        toggleCardSelection(degree)  // Toggle the card selection state
    }

    fun toggleFundingStatusSelection(status: String) {
        if (_selectedFundingStatus.value?.contains(status) == true) {
            removeSelectedFundingStatus(status)
        } else {
            addSelectedFundingStatus(status)
        }
        toggleCardSelection(status)  // Toggle the card selection state
    }

    fun checkAndSetFiltering() {
        val hasSelectedDegrees = _selectedDegrees.value?.isNotEmpty() == true
        val hasSelectedFundingStatus = _selectedFundingStatus.value?.isNotEmpty() == true
        val isFilteringNow = hasSelectedDegrees || hasSelectedFundingStatus
        _isFiltering.value = isFilteringNow

        if (isFilteringNow) {
            performFiltering()
        }
    }

    fun performFiltering() {
        val degrees = _selectedDegrees.value ?: emptyList()
        val fundingStatus = _selectedFundingStatus.value ?: emptyList()
        val scholarshipsToFilter =
            if (_isSearching.value == true) _searchedScholarships.value else _scholarships.value

        if (scholarshipsToFilter != null) {
            val filteredList = scholarshipsToFilter.filter { scholarship ->
                val matchesDegrees = degrees.isEmpty() || degrees.any { it in scholarship.degrees }
                val matchesFundingStatus =
                    fundingStatus.isEmpty() || fundingStatus.contains(scholarship.fundingStatus)
                matchesDegrees && matchesFundingStatus
            }

            _filteredScholarships.value = filteredList
            _totalFilteredScholarshipsCount.value = filteredList.size
        }
    }
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

    // ------ Start of FAVORITE SCHOLARSHIPS ------
    private val _favorites = MutableLiveData<List<String>>()
    val favorites: LiveData<List<String>> get() = _favorites

    fun loadUserFavorites(userEmail: String) {
        db.collection("users").document(userEmail).get()
            .addOnSuccessListener { document ->
                val user = document.toObject(User::class.java)
                user?.let {
                    _favorites.value = it.favorites
                }
            }
    }

    // Function to toggle favorite status
    fun toggleFavorite(email: String, scholarshipName: String) {
        val currentFavorites = _favorites.value ?: emptyList()
        if (currentFavorites.contains(scholarshipName)) {
            removeFavorite(email, scholarshipName)
        } else {
            addFavorite(email, scholarshipName)
        }
    }

    private fun addFavorite(email: String, scholarshipName: String) {
        val userDocRef = db.collection("users").document(email)
        userDocRef.update("favorites", FieldValue.arrayUnion(scholarshipName))
            .addOnSuccessListener {
                // Update LiveData
                val updatedFavorites = (_favorites.value ?: emptyList()) + scholarshipName
                _favorites.value = updatedFavorites
            }
            .addOnFailureListener {
                // Handle failure
            }
    }

    private fun removeFavorite(email: String, scholarshipName: String) {
        val userDocRef = db.collection("users").document(email)
        userDocRef.update("favorites", FieldValue.arrayRemove(scholarshipName))
            .addOnSuccessListener {
                // Update LiveData
                val updatedFavorites = (_favorites.value ?: emptyList()) - scholarshipName
                _favorites.value = updatedFavorites
            }
            .addOnFailureListener {
                // Handle failure
            }
    }
    // ------ End of FAVORITE SCHOLARSHIPS ------
}