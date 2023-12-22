package com.example.uas

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.uas.database.Movie
import com.example.uas.databinding.ActivityFormAddMovieBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.text.SimpleDateFormat
import java.util.*

class FormAddMovie : AppCompatActivity() {

    private lateinit var binding: ActivityFormAddMovieBinding
    private lateinit var storageReference: StorageReference
    private lateinit var imageUri: Uri

    private val firebase = FirebaseFirestore.getInstance()
    private val movieadminCollectionRef = firebase.collection("movieadmin")

    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                imageUri = uri
                binding.uploadimg.setImageURI(uri)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormAddMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.btnAdd.setOnClickListener {
            uploadData(imageUri)
        }

        binding.selectImageBtn.setOnClickListener {
            getContent.launch("image/*")
        }
    }

    private fun uploadData(imageUri: Uri? = null) {
        val judul: String = binding.txtTitleEdit.text.toString()
        val genre: String = binding.txtKategoriEdit.text.toString()
        val desc: String = binding.txtDesEdit.text.toString()

        if (judul.isNotEmpty() && genre.isNotEmpty() && desc.isNotEmpty() && imageUri != null) {
            val progressDialog = ProgressDialog(this)
            progressDialog.setMessage("Sedang Mengunggah Data...")
            progressDialog.setCancelable(false)
            progressDialog.show()
            // Generate a unique ID for the document
            val documentId = UUID.randomUUID().toString()

            // Upload image to Firebase Storage with the generated ID
            storageReference = FirebaseStorage.getInstance().reference.child("images/$documentId")
            val uploadTask: UploadTask = storageReference.putFile(imageUri)

            uploadTask.addOnSuccessListener { taskSnapshot ->
                storageReference.downloadUrl.addOnSuccessListener { imageUrl ->
                    val movie = Movie(judul, genre, desc, imageUrl.toString(),documentId)
                    // Add the movie data to Firestore
                    movieadminCollectionRef.document(documentId)
                        .set(movie, SetOptions.merge())
                        .addOnSuccessListener {
                            progressDialog.dismiss()
                            Toast.makeText(this, "Data Berhasil Diupload", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                        .addOnFailureListener { e ->
                            progressDialog.dismiss()
                            Toast.makeText(this, "Gagal: $e", Toast.LENGTH_SHORT).show()
                        }
                }
            }.addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(this, "Gambar gagal diupload: $e", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Tolong lengkapi form dan pilih gambar", Toast.LENGTH_SHORT).show()
        }
    }
}
