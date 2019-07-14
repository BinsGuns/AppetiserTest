package com.gunday.vince.appetisertestapp.adapter

import android.app.Dialog
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gunday.vince.appetisertestapp.R
import com.gunday.vince.appetisertestapp.model.ItunesTrack
import com.gunday.vince.appetisertestapp.view.MainActivity
import io.realm.RealmList

class TrackListAdapter(private val trackList : RealmList<ItunesTrack>?, private val context : MainActivity) : RecyclerView.Adapter<TrackListAdapter.TrackViewHolder>(){

    class  TrackViewHolder(val view : View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): TrackViewHolder {

        val trackListLayout = LayoutInflater.from(p0.context).inflate(R.layout.track_list_layout,p0,false)

        return TrackViewHolder(trackListLayout)

    }

    override fun getItemCount(): Int {
        if(trackList != null){
            return trackList.size
        } else {
            return 0
        }

    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {

        var ivTrackThumbnail = holder.view.findViewById(R.id.iv_track_thumbnail) as ImageView
        var tvTrackView = holder.view.findViewById(R.id.tv_trackname) as TextView
        var tvTrackCategory= holder.view.findViewById(R.id.tv_trackcategory) as TextView
        var tvTrackPrice = holder.view.findViewById(R.id.tv_trackprice) as TextView



        Glide.with(holder.view.context)
            .load(trackList?.get(position)?.artworkUrl60)
           .placeholder(R.drawable.track_placeholder)
            .centerCrop()
            .into(ivTrackThumbnail)

        tvTrackView.text = trackList?.get(position)?.trackName
        tvTrackCategory.text = trackList?.get(position)?.primaryGenreName
        var price = trackList?.get(position)?.trackPrice
        tvTrackPrice.text = "${ if(trackList?.get(position)?.trackPrice == null) "No information" else price.toString()+" "+trackList?.get(position)?.currency}"

        holder.view.setOnClickListener({
            renderDialogDetails(trackList?.get(position))
        })

    }

    private fun renderDialogDetails(itunesTrack: ItunesTrack?){
        val dialog = Dialog(context)
        dialog .requestWindowFeature(Window.FEATURE_NO_TITLE)

        dialog .setContentView(R.layout.detailed_layout)
        val iv_thumbnail = dialog .findViewById(R.id.iv_track) as ImageView
        val tv_trackname_detail = dialog .findViewById(R.id.tv_trackname_detail) as TextView
        val tv_category_detail = dialog .findViewById(R.id.tv_category_detail) as TextView
        val tv_price_detail = dialog .findViewById(R.id.tv_trackprice_detail) as TextView
        val tv_desciption_detail = dialog .findViewById(R.id.tv_description_detail) as TextView

        Glide.with(context)
            .load(itunesTrack?.artworkUrl60)
            .apply(RequestOptions.circleCropTransform())
           .placeholder(R.drawable.track_placeholder)
            .into(iv_thumbnail)

        tv_trackname_detail.text = "Title : "+itunesTrack?.trackName
        tv_category_detail.text = "Category : "+itunesTrack?.primaryGenreName
        var price = itunesTrack?.trackPrice
        tv_price_detail.text = "Price : ${ if(itunesTrack?.trackPrice == null) "No information" else price.toString()+" "+itunesTrack?.currency}"
        tv_desciption_detail.text = "Description : ${if (itunesTrack?.longDescription == null) "No Information" else itunesTrack?.longDescription}"

        dialog .show()
    }



}