package id.ac.stiki.doleno.scholarix.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import id.ac.stiki.doleno.scholarix.model.Beasiswa
import id.ac.stiki.doleno.scholarix.model.DummyBeasiswa
import id.ac.stiki.doleno.scholarix.navigation.Screen

class MainViewModel : ViewModel() {

    private val _currentScreen: MutableState<Screen> = mutableStateOf(Screen.BottomScreen.Home)

    val currentScreen: MutableState<Screen>
        get() = _currentScreen

    fun setCurrentScreen(screen: Screen) {
        _currentScreen.value = screen
    }

    fun getABeasiswaById(id: Long): Beasiswa? {
        return DummyBeasiswa.beasiswaList.find { it.id == id }
    }

}