package com.example.projectcineya

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle

import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import org.w3c.dom.Text


class ActualizarActivity : AppCompatActivity() {

    private lateinit var actualizarImagen: ImageView
    private lateinit var botonActualizar: Button
    private lateinit var actualizarTitulo: EditText
    private lateinit var actualizarGenero: EditText
    private lateinit var actualizarDirector: EditText
    private lateinit var actualizarCine: EditText

    private var titulo: String? = null
    private var genero: String? = null
    private var director: String? = null
    private var cine:String?=null
    private var imageUrl: String? = null
    private var key = ""
    private var oldImageURL: String? = null
    private var uri: Uri? = null
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actualizar)
        cine = intent.getStringExtra("cineSeleccionado")?:""

        botonActualizar = findViewById(R.id.btn_actualizar)
        actualizarTitulo = findViewById(R.id.actualizatitulo)
        actualizarImagen = findViewById(R.id.actualizaimagen)
        actualizarDirector = findViewById(R.id.actualizaDirector)
        actualizarGenero = findViewById(R.id.actualizaGenero)
        actualizarCine = findViewById(R.id.actualizaCine)




        val activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data = result.data
                    uri = data?.data
                    actualizarImagen.setImageURI(uri)
                } else {
                    Toast.makeText(this@ActualizarActivity, "No hay imagen seleccionada", Toast.LENGTH_SHORT).show()
                }
            }

        val bundle = intent.extras
        if (bundle != null) {
            Glide.with(this@ActualizarActivity).load(bundle.getString("Imagen")).into(actualizarImagen)
            actualizarTitulo.setText(bundle.getString("Titulo"))
            actualizarDirector.setText(bundle.getString("Director"))
            actualizarGenero.setText(bundle.getString("Genero"))
            key = bundle.getString("Key")?:""
            oldImageURL = bundle.getString("Imagen")
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("ProjectCineYa").child(key)

        actualizarImagen.setOnClickListener {
            val photoPicker = Intent(Intent.ACTION_PICK)
            photoPicker.type = "image/*"
            activityResultLauncher.launch(photoPicker)
        }

        botonActualizar.setOnClickListener {
            saveData()
            val intent = Intent(this@ActualizarActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun saveData() {
        storageReference = FirebaseStorage.getInstance().reference
            .child("Task Images")
            .child(uri?.lastPathSegment ?: "")

        val builder = AlertDialog.Builder(this@ActualizarActivity)
        builder.setCancelable(false)
        builder.setView(R.layout.progressbaractualizar_layout)
        val dialog = builder.create()
        dialog.show()

        storageReference.putFile(uri!!)
            .addOnSuccessListener { taskSnapshot ->
                val uriTask: Task<Uri> = taskSnapshot.storage.downloadUrl
                while (!uriTask.isComplete);
                val urlImage = uriTask.result
                imageUrl = urlImage.toString()
                updateData()
                dialog.dismiss()
            }
            .addOnFailureListener { e ->
                dialog.dismiss()
            }
    }

    private fun updateData() {
        titulo = actualizarTitulo.text.toString().trim()
        director = actualizarDirector.text.toString().trim()
        genero = actualizarGenero.text.toString().trim()


        val dataClass = PeliculaClass(titulo, director, genero,imageUrl,cine)

        databaseReference.setValue(dataClass)
            .addOnCompleteListener { task: Task<Void?> ->
                if (task.isSuccessful) {
                    val reference = FirebaseStorage.getInstance().getReferenceFromUrl(oldImageURL!!)
                    reference.delete().addOnSuccessListener {
                        val intent = Intent(this@ActualizarActivity, MainActivity::class.java)
                        intent.putExtra("imageUrl", imageUrl)
                        startActivity(intent)
                        Toast.makeText(this@ActualizarActivity, "Actualizado", Toast.LENGTH_SHORT).show()
                        finish()
                    }

                }
            }
            .addOnFailureListener { e: Exception ->
                Toast.makeText(this@ActualizarActivity, e.message.toString(), Toast.LENGTH_SHORT).show()
            }
    }
}