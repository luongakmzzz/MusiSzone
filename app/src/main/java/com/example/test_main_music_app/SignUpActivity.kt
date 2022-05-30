package com.example.test_main_music_app

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.example.test_main_music_app.Model.Users
import com.example.test_main_music_app.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.auth.User
import java.util.regex.Pattern

class SignUpActivity : AppCompatActivity() {

    //ViewBinding
    private lateinit var binding: ActivitySignupBinding
    //Action Bar
    private lateinit var actionbar: ActionBar
    //ProgressDialog
    private lateinit var progressDialog: ProgressDialog
    //FirebaseAuth
    private lateinit var firebaseAuth: FirebaseAuth
    //Realtimedatabase
    private lateinit var databse: DatabaseReference
    //
    private var email = ""
    private var password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

       //Configure Actionbar, enabled back button
//        actionbar = supportActionBar!!
//        actionbar.title ="Sign Up"

//

        //configure progress dialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Pleas wait")
        progressDialog.setMessage("Creating account In...")
        progressDialog.setCanceledOnTouchOutside(false)

        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance()

        //handle click login
        binding.noAccountTvSignup.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        //handle click, begin signup
        binding.Signupbutton.setOnClickListener{
            //validate data
            validateData()
        }
    }

    private fun validateData() {
        //get data
        email = binding.emailetSignup.text.toString().trim()
        password = binding.passwordetsignup.text.toString().trim()

        //validate data
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            //invalid email format
            binding.emailetSignup.error = "Invalid email format"
        }
        else if (TextUtils.isEmpty(password)){
            //password isn't entered
            binding.passwordetsignup.error = "Please enter password"
        }
        else if (password.length <6){
            //password length is less than 6
            binding.passwordetsignup.error = "Password must atleast"
        }
        else{
            //data in valid, continue signup
            firebaseSignUp()
        }
    }

    private fun firebaseSignUp() {
        //show progress
        progressDialog.show()
        //create account
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                //signup success
                progressDialog.dismiss()
                //get current user
                val firebaseUser = firebaseAuth.currentUser
                val email = firebaseUser!!.email
                val uid = firebaseUser!!.uid
                Toast.makeText(this, "Account created with email $email", Toast.LENGTH_SHORT).show()
                createdatausersignup(uid)
                //opent profile
                val intent = Intent(this@SignUpActivity, MainMusicActivity::class.java)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener { e->
                //signup Faild
                progressDialog.dismiss()
                Toast.makeText(this, "SignUp Faild dute to ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun createdatausersignup(uid: String){


        val email = binding.emailetSignup.text.toString()
        val passworduser = binding.passwordetsignup.text.toString()

        databse = FirebaseDatabase.getInstance().getReference("Users")
        val User = Users(email = email, passworduser = passworduser, uid = uid )
        databse.child(uid).setValue(User).addOnSuccessListener {
            Toast.makeText(this, "Succesfully saved data user", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this, "Fail add data user", Toast.LENGTH_SHORT).show()

        }



    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed() //go back to previous activity, when back button of actiobar clicked
        return super.onSupportNavigateUp()
    }
}