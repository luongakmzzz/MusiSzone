package com.example.test_main_music_app

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import android.widget.ListView
import android.widget.SearchView
import android.widget.SearchView.OnQueryTextListener
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.test_main_music_app.Adapter.MusicPlayerRecyrclerViewAdapter
import com.example.test_main_music_app.Model.AllCategory
import com.example.test_main_music_app.databinding.ActivityLoginBinding.inflate
import com.example.test_main_music_app.databinding.FragmentHomeBinding
import com.example.test_main_music_app.databinding.FragmentMusicBinding
import com.google.firebase.database.*
import java.util.*
import kotlin.collections.ArrayList


class MusicFragment : Fragment() {
    private lateinit var binding : FragmentMusicBinding

    private lateinit var musicsearchAdapter: MusicPlayerRecyrclerViewAdapter

    private lateinit var dbref: DatabaseReference

    private var arraymusic: ArrayList<AllCategory> = ArrayList()

    private var arraymusicsearch : ArrayList<AllCategory> = ArrayList()

    private lateinit var musiclistRecyclerView: RecyclerView

    private lateinit var searchView: SearchView

    private lateinit var listview: ListView

    private var database = FirebaseDatabase.getInstance()




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?{
        binding = FragmentMusicBinding.inflate(inflater, container, false)

        searchView = binding.searchViewMusic

        musiclistRecyclerView = binding.listviewsearchMusic
        binding.listviewsearchMusic.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        arraymusic = arrayListOf()

        getmusiclistdata()

        musicsearchAdapter = MusicPlayerRecyrclerViewAdapter(MusicFragment(), arraymusic) {
            val intent = Intent(activity, MusicPlayerActivity::class.java)
            intent.putExtra("allcategory", it)
            startActivity(intent)
        }


        val musicadapter : ArrayAdapter<AllCategory> = ArrayAdapter(
            activity!!, android.R.layout.simple_list_item_1,
            arraymusic
        )


        searchView.setOnQueryTextListener(object : OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                searchMusic(query)
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                searchMusic(query)
                return false
            }

        })

        return binding.root

    }

    private fun searchMusic(name: String?) {
        val query = database.getReference("Music").orderByChild("namemusic")
        if(name != "") {
            query.addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    arraymusic.clear()
                    if(snapshot.exists()){
                        for (musicSnapshot in snapshot.children){
                            val musiclist = musicSnapshot.getValue(AllCategory::class.java)
                            if(musiclist?.namemusic!!.contains(name!!.toLowerCase())) {
                                arraymusic.add(musiclist!!)
                            }
                        }
                        musicsearchAdapter.notifyDataSetChanged()
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }
    }


    private fun getmusiclistdata() {
        dbref = FirebaseDatabase.getInstance().getReference("Music")
        dbref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for (musicSnapshot in snapshot.children){
                        val musiclist = musicSnapshot.getValue(AllCategory::class.java)
                        arraymusic.add(musiclist!!)
                    }

                    binding.listviewsearchMusic.adapter = musicsearchAdapter
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })

    }



}
