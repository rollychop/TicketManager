package com.bnc.ticketmanager.ui

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bnc.ticketmanager.ui.navigation.Screen
import com.bnc.ticketmanager.ui.navigation.rememberNavigator
import com.bnc.ticketmanager.ui.screen.add_edit_ticket.AddOrEditTicketScreen
import com.bnc.ticketmanager.ui.screen.home.HomeScreen
import com.bnc.ticketmanager.ui.theme.TicketManagerTheme

@Composable
fun MainAppScreen() {

    TicketManagerTheme {
        Surface {
            val navController = rememberNavController()
            val navigator = rememberNavigator(navController)
            NavHost(
                navController = navController,
                startDestination = Screen.HomeScreen,
            ) {
                composable<Screen.HomeScreen> { HomeScreen(navigator) }
                composable<Screen.AddTicketScreen>(
                    enterTransition = {
                        slideIntoContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Start,
                            animationSpec = tween(500)
                        )
                    },
                    exitTransition = {
                        slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.End,
                            animationSpec = tween(500)
                        )
                    },
                    popEnterTransition = {
                        slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.Start,
                            animationSpec = tween(500)
                        )
                    },
                    popExitTransition = {
                        slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.End,
                            animationSpec = tween(500)
                        )
                    }
                ) { AddOrEditTicketScreen(navigator) }
            }


        }
    }

}
