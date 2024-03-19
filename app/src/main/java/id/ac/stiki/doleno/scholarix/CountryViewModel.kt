package id.ac.stiki.doleno.scholarix

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
                val response = countryService.getCountries()
                _countriesState.value = _countriesState.value.copy(
                    list = response,
                    loading = false,
                    error = null
                )
            } catch (e: Exception) {
                _countriesState.value = _countriesState.value.copy(
                    loading = false,
                    error = "Error fetch country API ${e.message}"
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