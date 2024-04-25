package id.ac.stiki.doleno.scholarix.view.main

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import id.ac.stiki.doleno.scholarix.MainNavigation
import id.ac.stiki.doleno.scholarix.auth.sign_in.GoogleAuthUiClient
import id.ac.stiki.doleno.scholarix.navigation.Screen
import id.ac.stiki.doleno.scholarix.navigation.screensInBottomBar
import id.ac.stiki.doleno.scholarix.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope

@Composable
fun MainView(googleAuthUiClient: GoogleAuthUiClient) {
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val scope: CoroutineScope = rememberCoroutineScope()
    val viewModel: MainViewModel = viewModel()
    val controller: NavController = rememberNavController()
    val navBackStackEntry by controller.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val currentScreen = remember {
        viewModel.currentScreen.value
    }
    val title = remember {
        mutableStateOf(currentScreen.title)
    }
    val shouldShowBottomBar =
        currentRoute == Screen.BottomScreen.Home.route ||
                currentRoute == Screen.BottomScreen.Favorit.route ||
                currentRoute == Screen.BottomScreen.Profil.route

    val bottomBar: @Composable () -> Unit = {
        if (shouldShowBottomBar) {
            BottomNavigation(modifier = Modifier.wrapContentSize()) {
                screensInBottomBar.forEach { item ->
                    BottomNavigationItem(
                        selected = currentRoute == item.bottomRoute,
                        onClick = { controller.navigate(item.bottomRoute) },
                        icon = {
                            Icon(
                                painter = painterResource(id = item.icon),
                                contentDescription = item.bottomTitle
                            )
                        },
                        label = {
                            Text(text = item.bottomTitle)
                        },
                        selectedContentColor = Color.White,
                        unselectedContentColor = Color.Black
                    )
                }
            }
        }
    }

    val topBar: @Composable () -> Unit = {
        if (currentRoute == Screen.BottomScreen.Profil.route) {
            TopAppBar(
                title = { Text(text = "Profil") },
                elevation = 4.dp
            )
        } else if (currentRoute == Screen.BottomScreen.Home.route) {
            TopAppBar(
                title = { Text(text = "Judul ini ganti search bar") },
                elevation = 4.dp
            )
        } else if (currentRoute == Screen.BottomScreen.Favorit.route) {
            TopAppBar(
                title = { Text(text = "Favorit") },
                elevation = 4.dp
            )
        }
    }

    Scaffold(
        topBar = topBar,
        bottomBar = bottomBar
    ) {
        MainNavigation(
            navController = controller,
            mainViewModel = viewModel,
            pd = it,
            googleAuthUiClient = googleAuthUiClient,
            shouldShowBottomBar
        )
    }
}