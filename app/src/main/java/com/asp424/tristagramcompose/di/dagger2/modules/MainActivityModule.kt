package com.asp424.tristagramcompose.di.dagger2.modules

import com.asp424.tristagramcompose.MainActivity
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class MainActivityModule {
    @Provides
    @Singleton
    fun mainActivity(): MainActivity {
        return MainActivity()
    }
}


