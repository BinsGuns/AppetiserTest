package com.gunday.vince.appetisertestapp.model

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class ItunesTrack(
     @PrimaryKey
     open var trackId : Int? = 0,
     open var dateAdded : String? ="",
     open var resultCount: Int? = 0,
     open var results : RealmList<ItunesTrack>? = null,
     open var longDescription : String? = null,
     open var trackName : String? = null,
     open var trackPrice : Float? = null,
     open var currency : String? = null,
     open var artworkUrl30 : String? = null,
     open var artworkUrl60 : String? = null,
     open var artworkUrl100 : String? = null,
     open var primaryGenreName : String? = null
): RealmObject()