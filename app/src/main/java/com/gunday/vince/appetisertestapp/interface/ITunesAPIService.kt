package com.gunday.vince.appetisertestapp.`interface`

import com.gunday.vince.appetisertestapp.model.ItunesTrack
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesAPIService {
    @GET("search")
    fun searchTrack(@Query("term") term : String, @Query("country") country : String ) : Observable<ItunesTrack>
}