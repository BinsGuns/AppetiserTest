package com.gunday.vince.appetisertestapp.view

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.gunday.vince.appetisertestapp.R
import com.gunday.vince.appetisertestapp.`interface`.ITunesAPIService
import io.realm.Realm
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import io.realm.RealmConfiguration
import android.content.Context.INPUT_METHOD_SERVICE
import android.support.v4.content.ContextCompat.getSystemService
import android.view.inputmethod.InputMethodManager


open class BaseClass : AppCompatActivity(){

    var itunesService : ITunesAPIService? = null
    var realm : Realm? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Realm.init(this)
        val config = RealmConfiguration.Builder().name("appetisertest.realm").deleteRealmIfMigrationNeeded().build()
        Realm.setDefaultConfiguration(config)
        realm = Realm.getDefaultInstance()

        val retrofit = Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(getString(R.string.base_url_itunes))
            .build()

        itunesService = retrofit.create(ITunesAPIService::class.java)

    }

    fun closeKeyboard(){
        // Check if no view has focus:
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}