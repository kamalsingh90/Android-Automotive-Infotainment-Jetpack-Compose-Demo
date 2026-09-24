package com.example.autoconnect.app

import android.content.Context

/**
 * AppModule providing explicit dependency factory bindings.
 * Conforms to Clean Architecture inversion of control patterns.
 */
object AppModule {
    fun provideAppContainer(context: Context): AppContainer {
        return DefaultAppContainer(context)
    }
}
