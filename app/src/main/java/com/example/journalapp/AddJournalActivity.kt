package com.example.journalapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.journalapp.databinding.ActivityAddJournalBinding
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.Date

class AddJournalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddJournalBinding

    private var currentUserId: String = ""
    private var currentUserName: String = ""

    private lateinit var auth: FirebaseAuth
    private var db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var storageRef: StorageReference

    private var collectionReference: CollectionReference = db.collection("Journal")
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_journal)

        storageRef = FirebaseStorage.getInstance().reference
        auth = FirebaseAuth.getInstance()
        currentUserId = auth.currentUser?.uid ?: ""
        currentUserName = auth.currentUser?.displayName ?: ""

        binding.postUsernameTextView.text = currentUserName
        binding.postCameraButton.setOnClickListener { openGallery() }
        binding.postSaveJournalButton.setOnClickListener { saveJournal() }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, 1)
    }

    private fun saveJournal() {
        val title = binding.postTitleEt.text.toString().trim()
        val thoughts = binding.postDescriptionEt.text.toString().trim()

        if (title.isNotEmpty() && thoughts.isNotEmpty() && imageUri != null) {
            binding.postProgressBar.visibility = View.VISIBLE

            val filePath = storageRef.child("journal_images/my_image_${Timestamp.now().seconds}")

            filePath.putFile(imageUri!!).addOnSuccessListener {
                filePath.downloadUrl.addOnSuccessListener { uri ->
                    val journal = Journal(
                        title,
                        thoughts,
                        uri.toString(),
                        currentUserId,
                        Timestamp(Date()),
                        currentUserName
                    )

                    collectionReference.add(journal).addOnSuccessListener {
                        binding.postProgressBar.visibility = View.INVISIBLE
                        startActivity(Intent(this, JournalList::class.java))
                        finish()
                    }.addOnFailureListener {
                        binding.postProgressBar.visibility = View.INVISIBLE
                        Toast.makeText(this, "Failed to save journal", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } else {
            Toast.makeText(this, "Please fill all fields and select an image", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK) {
            data?.data?.let {
                imageUri = it
                binding.addJournalImage.setImageURI(imageUri)
            }
        }
    }
}
