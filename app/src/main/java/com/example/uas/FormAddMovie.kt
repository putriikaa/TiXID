package com.example.uas

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.uas.databinding.ActivityFormAddMovieBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FormAddMovie : AppCompatActivity() {

    private lateinit var binding: ActivityFormAddMovieBinding
    lateinit var ImageUri: Uri

    companion object {
        const val EXTRA_UPDATE_ID = "extra_update_id"
        const val EXTRA_JUDUL = "extra_judul"
        const val EXTRA_GENRE = "extra_genre"
        const val EXTRA_DESC = "extra_desc"
    }

    // Firebase
    private val firebase = FirebaseFirestore.getInstance()
    private val movieadminCollectionRef = firebase.collection("movieadmin")


    private var updateId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormAddMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.selectImageBtn.setOnClickListener {
            selectImage()
        }

        updateId = intent.getStringExtra(EXTRA_UPDATE_ID) ?: ""

        if (updateId.isNotEmpty()) {
            // If ID is not empty, it's an update mode
            val judul = intent.getStringExtra(EXTRA_JUDUL) ?: ""
            val genre = intent.getStringExtra(EXTRA_GENRE) ?: ""
            val desc = intent.getStringExtra(EXTRA_DESC) ?: ""

            with(binding) {
                txtTitle.setText(judul)
                txtKategori.setText(genre)
                txtDes.setText(desc)
            }
        }

        with(binding) {
            btnAdd.setOnClickListener {
                if (txtTitle.text.isNotEmpty() && txtKategori.text.isNotEmpty() && txtDes.text.isNotEmpty()) {
                    val judul = txtTitle.text.toString()
                    val genre = txtKategori.text.toString()
                    val desc = txtDes.text.toString()
                    val url = uploadImage()
                    val newMovie = Movie(
                        judul = judul,
                        genre = genre,
                        desc = desc,
                        url = url
                    )
                    addData(newMovie)
                    resetForm()
                    setResultAndFinish()
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Please fill the form correctly",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun selectImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == RESULT_OK) {
            ImageUri = data?.data!!
            Log.d("FormActivity", "Error updating data imageeeee: "+ data?.data!!)
            binding.uploadimg.setImageURI(ImageUri)
        }
    }

    private fun addData(movie: Movie) {
        movieadminCollectionRef.add(movie)
            .addOnSuccessListener { docRef ->
                val createMovieId = docRef.id
                movie.id = createMovieId
                docRef.set(movie)
                    .addOnFailureListener {
                        Log.d("FormActivity", "Error updating data ID: ", it)
                    }
                resetForm()
                setResultAndFinish()

            }
            .addOnFailureListener {
                Log.d("FormActivity", "Error adding data: ", it)
            }
    }



    private fun resetForm() {
        with(binding) {
            txtTitle.setText("")
            txtKategori.setText("")
            txtDes.setText("")
        }
    }

    private fun updateData(movie: Movie) {
        movie.id = updateId
        movieadminCollectionRef.document(updateId).set(movie)
            .addOnFailureListener {
                Log.d("FormActivity", "Error updating data:", it)
            }
    }

    private fun uploadImage():String {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Uploading File..")
        progressDialog.setCancelable(false)
        progressDialog.show()

        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
        val now = Date()
        val fileName = formatter.format(now)
        val storageReference = FirebaseStorage.getInstance().getReference("$fileName"+".jpg")
        Log.d("isfdvvdhsjhsbddjsbjsb","storage reference "+storageReference.bucket)


        storageReference.putFile(ImageUri)
            .addOnSuccessListener {
                binding.uploadimg.setImageURI(null)
                Toast.makeText(this@FormAddMovie, "Successfully uploaded", Toast.LENGTH_SHORT).show()
                if (progressDialog.isShowing) progressDialog.dismiss()
            }.addOnFailureListener {
                Log.d("aaaaaaaaaaaaaa", "Error updating img:", it)

                if (progressDialog.isShowing)
                    Toast.makeText(this@FormAddMovie, "Error updating img:"+it, Toast.LENGTH_SHORT).show()
            }
        return storageReference.path
    }

    private fun setResultAndFinish() {
        val resultIntent = Intent().apply {
            setResult(Activity.RESULT_OK, this)
        }
        finish()
    }
}
