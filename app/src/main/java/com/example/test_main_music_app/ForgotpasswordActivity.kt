package com.example.test_main_music_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.test_main_music_app.databinding.ActivityForgotpasswordBinding
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class ForgotpasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityForgotpasswordBinding

    private lateinit var etmail : EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotpasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.SendSMSbutton.setOnClickListener {
            val email: String = binding.emailet.text.toString().trim{ it <= ' '}
            if (email.isEmpty()){
                Toast.makeText(
                    this,
                    "please enter email address",
                    Toast.LENGTH_SHORT
                ).show()
            }else{
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener{ task ->
                        if(task.isSuccessful){
                            Toast.makeText(
                                this,
                                "Email sent succesfully to reset your password",
                                Toast.LENGTH_SHORT
                            ).show()

                            finish()
                        }else{
                            Toast.makeText(
                                this,
                                task.exception!!.message.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }
    }


}