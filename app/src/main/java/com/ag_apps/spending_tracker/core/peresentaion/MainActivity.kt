package com.ag_apps.spending_tracker.core.peresentaion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ag_apps.spending_tracker.spending_balance.presentation.BalanceScreenCore
import com.ag_apps.spending_tracker.core.peresentaion.util.SpendingTrackerTheme
import com.ag_apps.spending_tracker.core.peresentaion.util.Background
import com.ag_apps.spending_tracker.core.peresentaion.util.Screen
import com.ag_apps.spending_tracker.spending_details.presentation.SpendingDetailsScreenCore
import com.ag_apps.spending_tracker.spending_overview.presentation.SpendingOverviewScreenCore
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SpendingTrackerTheme {
                Navigation(modifier = Modifier.fillMaxSize())
            }
        }
    }

    @Composable
    fun Navigation(modifier: Modifier = Modifier) {
        val navController = rememberNavController()

        Background()

        NavHost(
            modifier = modifier,
            navController = navController,
            startDestination = Screen.SpendingOverview
        ) {

            composable<Screen.SpendingOverview> {
                   SpendingOverviewScreenCore(onBalanceClick = {
                       navController.navigate(Screen.BalanceScreen)
                   }, onAddSpendingClick = {
                       navController.navigate(Screen.SpendingDetalis(-1))
                   }, onCardSpendingClick = { spendingId ->
                       // Navigate to SpendingDetails with the spendingId
                       navController.navigate("spendingDetails/$spendingId")
                   }
               )
            }

            // SpendingDetails screen takes a spendingId argument
            composable("spendingDetails/{spendingId}") { backStackEntry ->
                val spendingId = backStackEntry.arguments?.getString("spendingId")?.toIntOrNull()
                if (spendingId != null) {
                    SpendingDetailsScreenCore(
                        spendingId = spendingId,
                        onSaveSpending = {
                            navController.popBackStack()
                        }
                    )
                }
            }

            composable<Screen.SpendingDetalis> {
                SpendingDetailsScreenCore(
                    onSaveSpending = {
                        navController.popBackStack()
                    },
                    spendingId =0,
                    viewModel = koinViewModel { parametersOf(0) } // Inject the ViewModel with spendingId
                )
            }

            composable<Screen.BalanceScreen> {
                BalanceScreenCore(
                    onSaveClick = {
                        navController.popBackStack()
                    }
                )
            }

        }
    }

}
