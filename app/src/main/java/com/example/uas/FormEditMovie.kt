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
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.uas.databinding.ActivityFormEditMovieBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class FormEditMovie : AppCompatActivity() {
    private lateinit var binding: ActivityFormEditMovieBinding
    private lateinit var storageReference: StorageReference
    private var imageUri: Uri? = null

    private val firebase = FirebaseFirestore.getInstance()
    private val movieadminCollectionRef = firebase.collection("movieadmin")

    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                imageUri = uri
                binding.uploadimgEdit.setImageURI(uri)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormEditMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.selectImageBtn.setOnClickListener {
            getContent.launch("image/*")
        }

        val updatedTitle = binding.txtTitleUpdate
        val updatedGenre = binding.txtKategoriUpdate
        val updatedDesc = binding.txtDesUpdate

        updatedTitle.setText(intent.getStringExtra("title"))
        updatedGenre.setText(intent.getStringExtra("genre"))
        updatedDesc.setText(intent.getStringExtra("description"))

        val originalImageUrl = intent.getStringExtra("imgId")
        Glide.with(this)
            .load(originalImageUrl)
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(binding.uploadimgEdit)

        binding.btnUpdate.setOnClickListener {
            uploadData(imageUri, updatedTitle.text.toString(), updatedGenre.text.toString(), updatedDesc.text.toString())
        }
    }

    private fun uploadData(imageUri: Uri?, updatedTitle: String, updatedGenre: String, updatedDesc: String) {
        val documentId = Uri.parse(intent.getStringExtra("imgId")).lastPathSegment?.removePrefix("images/")

        if (imageUri != null) {
            // Upload new image and update data
            storageReference = FirebaseStorage.getInstance().reference.child("images/$documentId")
            val uploadTask = storageReference.putFile(imageUri)

            uploadTask.addOnSuccessListener { taskSnapshot ->
                storageReference.downloadUrl.addOnSuccessListener { imageUrl ->
                    val updatedMovie = Movie(updatedTitle, updatedGenre, updatedDesc, imageUrl.toString(), documentId!!)
                    // Update data in Firestore
                    movieadminCollectionRef.document(documentId!!)
                        .set(updatedMovie)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Data Updated Successfully", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Updating Data Failed!", Toast.LENGTH_SHORT).show()
                        }
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Image Upload Failed!", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Update data without uploading a new image
            val originalImageUrl = intent.getStringExtra("imgId")
            val updatedMovie = Movie(updatedTitle, updatedGenre, updatedDesc, originalImageUrl!!, documentId!!)
            movieadminCollectionRef.document(documentId!!)
                .set(updatedMovie)
                .addOnSuccessListener {
                    Toast.makeText(this, "Data Updated Successfully", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Updating Data Failed!", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
