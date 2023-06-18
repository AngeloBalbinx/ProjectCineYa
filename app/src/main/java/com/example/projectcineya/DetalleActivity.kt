package com.example.projectcineya

import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.github.clans.fab.FloatingActionButton
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class DetalleActivity : AppCompatActivity() {
    private lateinit var detalleGenero: TextView
    private lateinit var detalleTitulo: TextView
    private lateinit var detalleDirector: TextView
    private lateinit var detalleImagen: ImageView
    private lateinit var detalleCine:TextView
    private lateinit var botonEliminar: FloatingActionButton
    private lateinit var botonEditar: FloatingActionButton
    private var key = ""
    private var imageUrl = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle)

        detalleGenero = findViewById(R.id.detalleGenero)
        detalleTitulo = findViewById(R.id.detalleTitulo)
        detalleDirector = findViewById(R.id.detalleDirector)
        detalleCine= findViewById(R.id.detalleCine)
        detalleImagen = findViewById(R.id.detalleImagen)
        botonEditar = findViewById(R.id.btnEditar)
        botonEliminar = findViewById(R.id.btnEliminar)


        val bundle = intent.extras
        if (bundle != null) {
            detalleGenero.text = bundle.getString("Genero")
            detalleTitulo.text = bundle.getString("Titulo")
            detalleDirector.text = bundle.getString("Director")
            detalleCine.text=bundle.getString("Cine")
            key = bundle.getString("Key")!!
            imageUrl = bundle.getString("Imagen")!!
            Glide.with(this).load(bundle.getString("Imagen")).into(detalleImagen)
        }
        botonEliminar.setOnClickListener {
            val reference: DatabaseReference = FirebaseDatabase.getInstance().getReference("ProjectCineYa")
            val storage: FirebaseStorage = FirebaseStorage.getInstance()

            val storageReference: StorageReference = storage.getReferenceFromUrl(imageUrl)
            storageReference.delete().addOnSuccessListener {
                reference.child(key).removeValue()
                Toast.makeText(this@DetalleActivity, "Eliminado", Toast.LENGTH_SHORT).show()
                startActivity(Intent(applicationContext, MainActivity::class.java))
                finish()
            }
        }
        botonEditar.setOnClickListener {
            val intent = Intent(this@DetalleActivity, ActualizarActivity::class.java)
                .putExtra("Genero", detalleGenero.text.toString())
                .putExtra("Titulo", detalleTitulo.text.toString())
                .putExtra("Director", detalleDirector.text.toString())
                .putExtra("Cine",detalleCine.text.toString())
                .putExtra("Imagen", imageUrl)
                .putExtra("Key", key)
            startActivity(intent)
        }
    }
}