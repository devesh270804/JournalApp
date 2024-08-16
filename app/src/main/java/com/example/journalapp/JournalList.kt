package com.example.journalapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.journalapp.databinding.ActivityJournalBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class JournalList : AppCompatActivity() {

    private lateinit var binding: ActivityJournalBinding
    private lateinit var auth: FirebaseAuth

    private val db = FirebaseFirestore.getInstance()
    private val collectionReference = db.collection("Journal")

    private lateinit var journalList: MutableList<Journal>
    private lateinit var adapter: JournalRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJournalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        journalList = mutableListOf()

        val user = auth.currentUser
        if (user != null) {
            collectionReference.whereEqualTo("userId", user.uid).get()
                .addOnSuccessListener { querySnapshot: QuerySnapshot ->
                    val fetchedJournalList = querySnapshot.toObjects(Journal::class.java)
                    journalList.addAll(fetchedJournalList)
                    adapter = JournalRecyclerAdapter(this, journalList)
                    binding.recyclerView.adapter = adapter
                    adapter.notifyDataSetChanged()

                    binding.listNoPosts.visibility = if (journalList.isEmpty()) View.VISIBLE else View.GONE
                }
                .addOnFailureListener { exception ->
                    Log.e("JournalList", "Error getting documents", exception)
                    Toast.makeText(this, "Oops! Something went wrong!", Toast.LENGTH_LONG).show()
                }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_add -> startActivity(Intent(this, AddJournalActivity::class.java))
            R.id.action_signout -> {
                auth.signOut()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
