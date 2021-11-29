package com.asp424.tristagramcompose.di.dagger2.modules

import androidx.lifecycle.ViewModel
import com.asp424.tristagramcompose.navigator.CustomNavigator
import com.asp424.tristagramcompose.viewmodels.NavViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Module
abstract class NavViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(NavViewModel::class)
    abstract fun bindNavViewModel(navViewModel: NavViewModel) : ViewModel
}

@Module
class CustomNavigatorModule {
    @Provides
    @Singleton
    fun navCustom(): CustomNavigator {
        return CustomNavigator()
    }
}