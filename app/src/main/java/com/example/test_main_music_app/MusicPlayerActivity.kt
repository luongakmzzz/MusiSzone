package com.example.test_main_music_app


import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.*
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.test_main_music_app.Model.AllCategory
import com.example.test_main_music_app.Model.Users
import com.example.test_main_music_app.databinding.ActivityMusicPlayerBinding
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.firebase.database.*
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Runnable
import java.time.Duration
import java.util.concurrent.TimeUnit


class MusicPlayerActivity : AppCompatActivity(), MediaPlayer.OnPreparedListener {

    private lateinit var playbtn: ImageButton

    private lateinit var pausebtn: Button

    private lateinit var prev: Button

    private lateinit var nexts: Button

    private lateinit var mediaPlayer: MediaPlayer

    private lateinit var allCategory: AllCategory

    private lateinit var firebaseDatabase: FirebaseDatabase

    private lateinit var databaseReference: DatabaseReference

    private lateinit var binding: ActivityMusicPlayerBinding

    private lateinit var seekbar: SeekBar

    private lateinit var playerposition: TextView

    private lateinit var playerduration: TextView

    private lateinit var arraymusic : ArrayList<AllCategory>

    private var duration: Int = 0

    private var currentIdex: Int = 0

    private lateinit var songExtra: Bundle

    private lateinit var uri: Uri

    private lateinit var mStore: StorageReference

    private lateinit var favouritesbtn : ImageButton




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMusicPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mediaPlayer = MediaPlayer()
        playbtn = binding.playBtn
        seekbar = binding.seekBar
//        var audioUrl = allCategory.musicUrl.toString()
//        totalTime = mediaPlayer.duration
//        playerposition = binding.playPosition
//        playerduration = binding.playDuration
//        pausebtn = binding.pauseBtn
//        prev = binding.prevBtn
//        nexts = binding.nextBtn



        val music = intent.extras?.getParcelable<AllCategory>("allcategory")

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.getReference("Music")


        mediaPlayer = MediaPlayer()
        if (music != null) {

            binding.titleMusicDetails.text = music.namemusic
            binding.authorMusicDetails.text = music.author
            Glide.with(this).load(music.imageUrl).into(binding.imageMusicDetails)
            mediaPlayer.setDataSource(music.musicUrl)

        }
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
        mediaPlayer.prepareAsync()
        //Play music
        playbtn.setOnClickListener {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
//                Toast.makeText(this@MusicPlayerActivity, "Audio is pause", Toast.LENGTH_LONG)
//                    .show()
                intilialiseSeekbar()
            } else {
                mediaPlayer.start()
//                Toast.makeText(this@MusicPlayerActivity, "Audio start", Toast.LENGTH_SHORT)
//                    .show()
            }
        }
        //Seekbar music
        seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) mediaPlayer?.seekTo(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })
        //Favorites music
        binding.favouritesBtn.setOnClickListener{
            favourite(music!!.musicid.toString())

        }

    }

    override fun onPrepared(mp: MediaPlayer?) {
        TODO("Not yet implemented")
    }

    private fun intilialiseSeekbar() {
        seekbar.max = mediaPlayer.duration

        val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                try {
                    seekbar.progress = mediaPlayer.currentPosition
                    handler.postDelayed(this, 0)
                } catch (e: Exception) {
                    seekbar.progress = 0
                }
            }
        }, 0)
    }

    private fun favourite(musicid: String){

        val preferences  = this.getSharedPreferences("myPref", Context.MODE_PRIVATE)
        val uid = preferences.getString("uid", "")
        firebaseDatabase = FirebaseDatabase.getInstance()
        firebaseDatabase.getReference("Favorite/$uid").child(musicid).get()
            .addOnSuccessListener {
                if(!it.exists()){
                    firebaseDatabase.getReference("Music").child(musicid).get()
                        .addOnSuccessListener {
                            val music = it.getValue(AllCategory::class.java)!!
                            firebaseDatabase.getReference("Favorite/$uid").child(musicid).setValue(music)
                            binding.favouritesBtn.setImageResource(R.drawable.favorite_full_empty_icon)

                        }
                }else{
                    firebaseDatabase.getReference("Favorite/$uid").child(musicid).removeValue()
                    binding.favouritesBtn.setImageResource(R.drawable.favourite_empty_icon)
                }
            }
    }
}