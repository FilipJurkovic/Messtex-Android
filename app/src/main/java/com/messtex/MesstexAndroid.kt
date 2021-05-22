package com.messtex

import android.app.Application
import com.messtex.data.models.localdb.UserDatabase
import com.messtex.data.repositories.mainRepository.MainRepository
import com.messtex.ui.fragments.view.StepMeterInfo
import com.messtex.ui.fragments.view.StepUserInfo
import com.messtex.utils.ViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.AndroidLifecycleScope
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.*

class MesstexAndroid : Application(), KodeinAware {

    override val kodein: Kodein = Kodein.lazy {
        import(androidXModule(this@MesstexAndroid))
        bind() from singleton { UserDatabase(instance()) }
        bind() from singleton {
            MainRepository(
                instance()
            )
        }
        bind() from provider {
            ViewModelFactory(
                instance()
            )
        }
    }
}