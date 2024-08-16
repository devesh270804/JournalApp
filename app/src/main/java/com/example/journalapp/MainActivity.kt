package com.example.journalapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.journalapp.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)

//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.emailSignUpButton.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        binding.emailSignInButton.setOnClickListener {
            val email = binding.email.text.toString().trim()
            val password = binding.password.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()){
                auth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful){
                            val intent = Intent(this,JournalList::class.java)
                            startActivity(intent)
                        }
                        else{
                            Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show()
                        }

                    }
            }
            else{
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            }

        }

    }

    override fun onStart() {
        super.onStart()
        auth.currentUser?.let {
            val intent = Intent(this,JournalList::class.java)
            startActivity(intent)
            finish()
        }
    }






//    private lateinit var binding: ActivityMainBinding
//    private lateinit var auth: FirebaseAuth
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
//
//        binding.emailSignUpButton.setOnClickListener {
//            val intent = Intent(this, SignUpActivity::class.java)
//            startActivity(intent)
//        }
//
//        auth = Firebase.auth
//
//        binding.emailSignInButton.setOnClickListener {
//            val email = binding.email.text.toString().trim()
//            val password = binding.password.text.toString().trim()
//            if (email.isNotEmpty() && password.isNotEmpty()) {
//                loginWithEmailPassword(email, password)
//            } else {
//                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
//
//    private fun loginWithEmailPassword(email: String, password: String) {
//        auth.signInWithEmailAndPassword(email, password)
//            .addOnCompleteListener(this) { task ->
//                if (task.isSuccessful) {
//                    auth.currentUser?.let { user ->
//                        val journalUser = applicationContext as JournalUser
//                        journalUser.userId = user.uid
//                        journalUser.username = user.displayName ?: "Anonymous"
//
//                        val intent = Intent(this, JournalList::class.java)
//                        startActivity(intent)
//                        finish()
//                    }
//                } else {
//                    Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show()
//                }
//            }
//    }
//
//    override fun onStart() {
//        super.onStart()
//        auth.currentUser?.let {
//            val intent = Intent(this, JournalList::class.java)
//            startActivity(intent)
//            finish()
//        }
//    }
}
