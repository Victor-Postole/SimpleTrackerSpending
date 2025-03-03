package com.ag_apps.spending_tracker.core.di

import android.content.Context
import androidx.room.Room
import com.ag_apps.spending_tracker.core.data.RoomSpendingDataSource
import com.ag_apps.spending_tracker.core.data.local.CoreRepositoryImpl
import com.ag_apps.spending_tracker.core.data.remote.API.ImagesRepositoryImpl
import com.ag_apps.spending_tracker.core.data.local.SpendingDatabase
import com.ag_apps.spending_tracker.core.data.remote.API.ImagesApi
import com.ag_apps.spending_tracker.core.domain.repositories.CoreRepository
import com.ag_apps.spending_tracker.core.domain.repositories.ImageRepository
import com.ag_apps.spending_tracker.core.domain.repositories.LocalSpendingDataSource
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory



val coreModule = module {
    single {
        Room.databaseBuilder(
            androidApplication(),
            SpendingDatabase::class.java,
            "spending_database_db"
        ).build()
    }

    single { get<SpendingDatabase>().dao }

    single {
        androidApplication().getSharedPreferences("spending_tracker_preferences", Context.MODE_PRIVATE)
    }

    single{
        Retrofit.Builder()
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .baseUrl(ImagesApi.BASE_URL)
            .build()
            .create(ImagesApi::class.java)
    }


    singleOf(::RoomSpendingDataSource).bind<LocalSpendingDataSource>()
    singleOf(::CoreRepositoryImpl).bind<CoreRepository>()
    singleOf(::ImagesRepositoryImpl).bind<ImageRepository>()

}