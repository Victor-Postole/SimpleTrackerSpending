package com.ag_apps.spending_tracker

import android.app.Application
import com.ag_apps.spending_tracker.core.di.coreModule
import com.ag_apps.spending_tracker.spending_balance.di.balanceModule
import com.ag_apps.spending_tracker.spending_overview.di.spendingOverviewModule
import com.ag_apps.spending_tracker.spending_details.di.spendingDetailsModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class App: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(
                coreModule,
                balanceModule,
                spendingOverviewModule,
                spendingDetailsModule
            )
        }

    }

}