package com.asp424.tristagramcompose.di.dagger2.modules

import androidx.room.Room

import com.asp424.tristagramcompose.application.App
import com.asp424.tristagramcompose.data.firebase.datasources.*
import com.asp424.tristagramcompose.data.repos.registration.RegistrationRepo
import com.asp424.tristagramcompose.data.repos.contacts.ContactsRepo
import com.asp424.tristagramcompose.data.repos.main_list.MainListRepo
import com.asp424.tristagramcompose.data.repos.settings.SettingsRepo
import com.asp424.tristagramcompose.data.repos.single_chat.SingeChatRepo
import com.asp424.tristagramcompose.data.room.database.Database
import com.asp424.tristagramcompose.data.room.main_list.MainListDao
import com.asp424.tristagramcompose.data.room.single_chat.HeaderDao
import com.asp424.tristagramcompose.data.room.single_chat.SingleChatDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RegistrationRepositoryModule {
    @Provides
    @Singleton
    fun repository(
        registrationDataSource: RegistrationDataSource
    ): RegistrationRepo {
        return RegistrationRepo(registrationDataSource)
    }
}

@Module
class FirebaseRegistrationDataSourceModule {
    @Provides
    @Singleton
    fun regDatasource(): RegistrationDataSource {
        return RegistrationDataSource()
    }
}

@Module
class ContactsRepositoryModule {
    @Provides
    @Singleton
    fun repository(
        contactsDataSource: ContactsDataSource
    ): ContactsRepo {
        return ContactsRepo(contactsDataSource)
    }
}

@Module
class FirebaseContactsDataSourceModule {
    @Provides
    @Singleton
    fun contactsDatasource(): ContactsDataSource {
        return ContactsDataSource()
    }
}

@Module
class MainListRepositoryModule {
    @Provides
    @Singleton
    fun repository(
        mainListDataSource: MainListDataSource,
        mainListDao: MainListDao
    ): MainListRepo {
        return MainListRepo(mainListDataSource, mainListDao)
    }
}

@Module
class FirebaseMainListDataSourceModule {
    @Provides
    @Singleton
    fun mainListDatasource(): MainListDataSource {
        return MainListDataSource()
    }
}

@Module
class SingleChatRepositoryModule {
    @Provides
    @Singleton
    fun repository(
        singleChatDataSource: SingleChatDataSource,
        singleChatDao: SingleChatDao,
        mainListRepo: MainListRepo,
        headerDao: HeaderDao
    ): SingeChatRepo {
        return SingeChatRepo(singleChatDataSource, singleChatDao, headerDao, mainListRepo)
    }
}

@Module
class FirebaseSingleChatDataSourceModule {
    @Provides
    @Singleton
    fun singleChatDatasource(): SingleChatDataSource {
        return SingleChatDataSource()
    }
}


@Module
class SettingsRepositoryModule {
    @Provides
    @Singleton
    fun repository(
        settingsDataSource: SettingsDataSource
    ): SettingsRepo {
        return SettingsRepo(settingsDataSource)
    }
}

@Module
class FirebaseSettingsDataSourceModule {
    @Provides
    @Singleton
    fun settingsDataSource(): SettingsDataSource {
        return SettingsDataSource()
    }
}
@Module
class RoomModules {
    @Singleton
    @Provides
    fun providesMainListDatabase(app: App): Database {
        return Room.databaseBuilder(app, Database::class.java, "main_list_database").build()
    }


    @Singleton
    @Provides
    fun providesMainListDao(db: Database): MainListDao {
        return db.mainListDao()
    }

        @Provides
        @Singleton
        fun providesSingleChatDao(db: Database): SingleChatDao {
            return db.singleChatDao()
        }

    @Provides
    @Singleton
    fun providesHeaderDao(db: Database): HeaderDao {
        return db.headerDao()
    }
}

