package com.asp424.tristagramcompose.di.dagger2.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.asp424.tristagramcompose.viewmodels.*
import com.asp424.tristagramcompose.viewmodels.viewmodelsfactory.ViewModelFactory
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlin.reflect.KClass


@ExperimentalCoroutinesApi
@Module(includes = [ViewModelModules::class])
interface ViewModelFactoryModule {

    @Binds
    fun bindsViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}

@ExperimentalCoroutinesApi
@Module
interface ViewModelModules {
    @IntoMap
    @Binds
    @ViewModelKey(RegisterViewModel::class)
    fun bindsRegisterViewModel(viewModel: RegisterViewModel): ViewModel

    @IntoMap
    @Binds
    @ViewModelKey(MainViewModel::class)
    fun bindsMainViewModel(viewModel: MainViewModel): ViewModel

    @IntoMap
    @Binds
    @ViewModelKey(ContactsViewModel::class)
    fun bindsContactsViewModel(viewModel: ContactsViewModel): ViewModel

    @IntoMap
    @Binds
    @ViewModelKey(SettingsViewModel::class)
    fun bindsSettingsViewModel(viewModel: SettingsViewModel): ViewModel

    @IntoMap
    @Binds
    @ViewModelKey(SingleChatViewModel::class)
    fun bindsSingleChatViewModel(viewModel: SingleChatViewModel): ViewModel

    @IntoMap
    @Binds
    @ViewModelKey(MainScreenViewModel::class)
    fun bindsMainScreenViewModel(viewModel: MainScreenViewModel): ViewModel
}

@MapKey
@Target(AnnotationTarget.FUNCTION)
internal annotation class ViewModelKey(val value: KClass<out ViewModel>)
