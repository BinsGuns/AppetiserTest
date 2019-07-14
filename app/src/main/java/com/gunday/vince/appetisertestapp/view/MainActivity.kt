package com.gunday.vince.appetisertestapp.view

import android.opengl.Visibility
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MotionEvent
import android.view.View
import android.widget.*
import com.gunday.vince.appetisertestapp.R
import com.gunday.vince.appetisertestapp.adapter.TrackListAdapter
import com.gunday.vince.appetisertestapp.model.ItunesTrack

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

import io.realm.RealmList
import android.widget.AdapterView.OnItemClickListener
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class MainActivity : BaseClass() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var trackListAdapter: RecyclerView.Adapter<TrackListAdapter.TrackViewHolder>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private var loader : ProgressBar? = null
    private var tvDateHolder : TextView? = null
    private var tvResultHolder : TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewManager = LinearLayoutManager(this)
        recyclerView = findViewById(R.id.rv_tracks)


        var etSearch = findViewById(R.id.et_search) as EditText
        var btnSearch = findViewById(R.id.btnSearch) as ImageButton
        loader = findViewById(R.id.loader) as ProgressBar
        tvDateHolder = findViewById(R.id.tv_dateholder) as TextView
        tvResultHolder = findViewById(R.id.tv_noresult) as TextView

        loader!!.bringToFront()

        btnSearch.setOnClickListener{
            searchTrack(etSearch.text.toString(),"us")
            closeKeyboard()
        }



    }



   private fun searchTrack( term: String, country : String){
        tvResultHolder?.visibility = View.GONE
        loader?.visibility = View.VISIBLE
        itunesService!!.searchTrack(term,country)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::onSuccess,this::onError)
    }

    private fun onSuccess(response : ItunesTrack) {

        trackListAdapter = TrackListAdapter(response.results,this@MainActivity)

        renderToView()
        loader?.visibility = View.INVISIBLE

        if(response.resultCount != 0) {
            // save only to realm if it has data
            clearData()
            saveToRealm(response)
        } else {

            tvDateHolder?.text = ""
            tvResultHolder?.visibility = View.VISIBLE
        }
    }

    private fun renderToView(){
        recyclerView.apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)
            // use a linear layout manager
            layoutManager = viewManager
            recyclerView.adapter = trackListAdapter

        }
    }

    private fun clearData(){
        var result = realm?.where(ItunesTrack::class.java)?.findAll()
        realm?.beginTransaction()
        result?.deleteAllFromRealm()
        realm?.commitTransaction()
    }

    private fun saveToRealm(response : ItunesTrack){

        val currentDate = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val stringDate = currentDate.format(formatter)
        response.dateAdded = stringDate
        realm?.beginTransaction()
        realm?.copyToRealmOrUpdate(response)
        realm?.commitTransaction()

        // update the date to the latest
        tvDateHolder?.text = "${getString(R.string.data_shown)+" : "+response.dateAdded}"
    }

    private fun onError(error :Throwable){
        if(error.cause!!.localizedMessage.indexOf("hostname") > -1) {// no internet or network checking

            Toast.makeText(this@MainActivity,getString(R.string.no_network),Toast.LENGTH_LONG).show()
        }
        loader?.visibility = View.INVISIBLE
    }

    //checking of local database from realm
    private fun checkLocalData(){
        var result = realm?.where(ItunesTrack::class.java)?.findAll()
        if(result?.size != 0 ){
            tvResultHolder?.visibility = View.GONE

            var realmList : RealmList<ItunesTrack> = RealmList<ItunesTrack>()

            for(element in result.orEmpty().withIndex()){
                if(element.index != 0){
                    realmList.add(result?.get(element.index))
                }

            }


            tvDateHolder?.text =  "${getString(R.string.data_shown)+" : "+realmList?.get(0)?.dateAdded}"
            trackListAdapter = TrackListAdapter(realmList,this@MainActivity)



            renderToView()
        } else {
            // no local data saved or recent search is zero size
            tvResultHolder?.visibility = View.VISIBLE
            tvDateHolder?.text = ""

        }

    }

    override fun onResume() {
        super.onResume()
        // check local data every user returns back to the app
        checkLocalData()

    }


}
