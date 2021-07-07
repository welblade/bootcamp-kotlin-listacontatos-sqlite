package com.github.welblade.listadecontatos.application

import android.app.Application
import com.github.welblade.listadecontatos.helper.DbHelper

class ContatoApplication : Application() {

    companion object {
        lateinit var instance : ContatoApplication
    }

    var helperDb: DbHelper? = null
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this
        helperDb = DbHelper(this)
    }
}