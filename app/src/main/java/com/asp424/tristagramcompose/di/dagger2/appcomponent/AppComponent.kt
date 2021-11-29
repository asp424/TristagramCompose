package com.asp424.tristagramcompose.di.dagger2.appcomponent


import com.asp424.tristagramcompose.MainActivity
import com.asp424.tristagramcompose.application.App
import com.asp424.tristagramcompose.data.repos.main_list.MainListRepo
import com.asp424.tristagramcompose.data.room.main_list.MainListDao
import com.asp424.tristagramcompose.di.dagger2.modules.*
import com.asp424.tristagramcompose.navigator.CustomNavigator
import com.asp424.tristagramcompose.notification.FirebaseNotificationService
import com.asp424.tristagramcompose.viewmodels.viewmodelsfactory.ViewModelFactory
import dagger.BindsInstance
import dagger.Component
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton


@ExperimentalCoroutinesApi
@Singleton
@Component(
    modules = [MainActivityModule::class, ViewModelFactoryModule::class,
        FirebaseRegistrationDataSourceModule::class,
        RegistrationRepositoryModule::class, NavViewModelModule::class,
        CustomNavigatorModule::class, FirebaseContactsDataSourceModule::class, ContactsRepositoryModule::class,
        MainListRepositoryModule::class, FirebaseMainListDataSourceModule::class, FirebaseSingleChatDataSourceModule::class,
        SingleChatRepositoryModule::class, RoomModules::class, FirebaseSettingsDataSourceModule::class, SettingsRepositoryModule::class]

)
interface AppComponent {
    fun inject(activity: MainActivity)
    fun inject(service: FirebaseNotificationService)
    val mainListDataBase: MainListDao
    val mainRepo: MainListRepo
    val navigator: CustomNavigator
    val mainActivityComponent: MainActivity
    val viewModelsFactoryComponent: ViewModelFactory

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: App): Builder
        fun create(): AppComponent
    }

}