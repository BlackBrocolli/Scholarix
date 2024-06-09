package id.ac.stiki.doleno.scholarix.viewmodel

// Import Firebase
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
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
}
