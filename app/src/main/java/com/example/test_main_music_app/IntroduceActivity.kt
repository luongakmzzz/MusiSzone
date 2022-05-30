package com.example.test_main_music_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.test_main_music_app.databinding.ActivityIntroduceBinding
import com.example.test_main_music_app.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class IntroduceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIntroduceBinding

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroduceBinding.inflate(layoutInflater)
        setContentView(binding.root)


        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()


        binding.introLogin.setOnClickListener{
            startActivity(Intent(this, LoginActivity::class.java))
        }
        binding.introRegister.setOnClickListener{
            startActivity(Intent(this, SignUpActivity::class.java))
        }

    }
    private fun checkUser(){
        //if user already logged in to go profile activity
        //get current user
        val firebaseUser = firebaseAuth.currentUser
        if(firebaseUser != null){
            //user is already logged in
            startActivity(Intent(this, MainMusicActivity::class.java))

//              supportFragmentManager.beginTransaction().add(R.id.mainrecyclerviewmusic, HomeFragment()).commit()
            finish()
        }
    }
}