package com.bnc.ticketmanager.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.withResumed
import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun rememberNavigator(
    navController: NavHostController,
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) = remember(navController, coroutineScope) {
    Navigator(navController, coroutineScope)
}

class Navigator(
    private val navController: NavHostController,
    private val coroutineScope: CoroutineScope
) {
    fun navigate(screen: Screen) {
        coroutineScope.launch {
            navController.currentBackStackEntry?.withResumed {
                navController.navigate(screen)
            }
        }
    }

    fun navUp(): Boolean {
        return navController.navigateUp()
    }
}