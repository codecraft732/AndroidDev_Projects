package com.example.mediaplayer.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.mediaplayer.presentation.HomeScreen
import com.example.mediaplayer.presentation.LocalPlayerScreen
import com.example.mediaplayer.presentation.localvedioscreen.LocalVediosScreen
import com.example.mediaplayer.presentation.WebPlayerScreen
import com.example.mediaplayer.presentation.viewmodel.VedioViewModel

@Composable
fun NavGraph(viewModel: VedioViewModel) {

    val navController = rememberNavController()
    NavHost(
    navController = navController,
    startDestination = Routes.home){
        composable<Routes.home> {
            HomeScreen(navController)
        }

        // Local videos list screen route
        composable<Routes.LocalVedios> {
            LocalVediosScreen(navController, viewModel)
        }


        // Local video player screen route (with video ID parameter)
        composable<Routes.LocalPlayer> { backStackEntry ->
            val args = backStackEntry.toRoute<Routes.LocalPlayer>()
            LocalPlayerScreen(navController, args.vedioId, viewModel)
        }


        composable<Routes.WebPlayer>{
            WebPlayerScreen(navController)
        }
    }

}
