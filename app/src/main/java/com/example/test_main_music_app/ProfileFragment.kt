package com.example.test_main_music_app

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.test_main_music_app.databinding.FragmentHomeBinding
import com.example.test_main_music_app.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth


class ProfileFragment : Fragment() {
    //ViewBinding
    private lateinit var binding: FragmentProfileBinding
    //FirebaseAuth
    private lateinit var firebaseAuth: FirebaseAuth
//    //SharedPref
//    private lateinit var preferences: SharedPreferences
//    //SharedPref
//    private lateinit var preferences: SharedPreferences



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        firebaseAuth = FirebaseAuth.getInstance()

        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()
        //handle click logout
        binding.logoutBtn.setOnClickListener{
            firebaseAuth.signOut()
            checkUser()
            val settings: SharedPreferences = this.activity!!.getSharedPreferences("myPref",
                AppCompatActivity.MODE_PRIVATE
            )
            settings.edit().clear().commit()
        }


        return binding.root

    }
    private fun checkUser() {
        //check user is logged in  or not
        val firebaseAuth = firebaseAuth.currentUser
        if(firebaseAuth != null){
            //user not null, user is logged in
            var email = firebaseAuth.email
            val preferences  = this.activity!!.getSharedPreferences("myPref", Context.MODE_PRIVATE)
            var uid = preferences.getString("uid", "")

            //set to text view
//            binding.emailTv.text = email
//            binding.uidTv.text = uid
            //set to edit view
              binding.etemail.setText(email)
              binding.etUid.setText(uid)


        }
        else{
            //user is null, user is not logged in
            val intent = Intent(activity, IntroduceActivity::class.java)
            startActivity(intent)
        }
    }

}