package id.ac.stiki.doleno.scholarix.viewmodel

// Import Firebase
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.ac.stiki.doleno.scholarix.model.Country
import kotlinx.coroutines.launch

class CountryViewModel : ViewModel() {

    private val _countriesState = mutableStateOf(CountryState())
    val countriesState: State<CountryState> = _countriesState

    init {
        fetchCountries()
    }

    private fun fetchCountries() {
        viewModelScope.launch {
            try {
                val db = FirebaseFirestore.getInstance()
                db.collection("countries")
                    .get()
                    .addOnSuccessListener { result ->
                        val countries = result.map { document ->
                            Country(
                                name = document.getString("name") ?: "",
                                flag = document.getString("flag") ?: ""
                            )
                        }
                        _countriesState.value = CountryState(
                            list = countries,
                            loading = false,
                            error = null
                        )
                    }
                    .addOnFailureListener { exception ->
                        _countriesState.value = CountryState(
                            loading = false,
                            error = "Error fetch countries from Firebase: ${exception.message}"
                        )
                    }
            } catch (e: Exception) {
                _countriesState.value = CountryState(
                    loading = false,
                    error = "Error fetch country: ${e.message}"
                )
            }
        }
    }

    data class CountryState(
        val loading: Boolean = true,
        val error: String? = null,
        val list: List<Country> = emptyList()
    )

    // == FITUR SEARCH COUNTRIES ==
    private val _searchText = MutableLiveData("")
    val searchText: LiveData<String> = _searchText

    private val _isSearching = MutableLiveData(false)
    val isSearching: LiveData<Boolean> = _isSearching

    private val _searchedCountries = MutableLiveData<List<Country>>()
    val searchedCountries: LiveData<List<Country>> = _searchedCountries

    fun setSearchText(text: String) {
        _searchText.value = text
        searchCountriesByName(text) // Memanggil fungsi pencarian
    }

    fun resetSearching() {
        _searchText.value = ""
        _isSearching.value = false
        _searchedCountries.value = emptyList()
    }

    private fun searchCountriesByName(name: String) {
        _isSearching.value = true
        _searchedCountries.value = _countriesState.value.list.filter { it.name.contains(name, ignoreCase = true) }
    }
}
