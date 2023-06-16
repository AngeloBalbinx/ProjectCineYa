package com.example.projectcineya


import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.example.projectcineya.databinding.ActivitySubirBinding
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.text.DateFormat
import java.util.*


class SubirActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySubirBinding
    var imagenURL:String?=null
    var uri: Uri?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubirBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val activityResultLauncher=registerForActivityResult<Intent,ActivityResult>(
        ActivityResultContracts.StartActivityForResult()){ result ->
            if(result.resultCode == RESULT_OK){
                val data = result.data
                uri = data!!.data
                binding.subirimagen.setImageURI(uri)
            }else{
                Toast.makeText(this@SubirActivity,"No hay imagen seleccionada",Toast.LENGTH_SHORT).show()
            }
        }
        binding.subirimagen.setOnClickListener{
            val photoPicker = Intent(Intent.ACTION_PICK)
            photoPicker.type= "image/*"
            activityResultLauncher.launch(photoPicker)
        }
        binding.btnGuardar.setOnClickListener {
            guardarPelicula()
        }

    }
    private fun guardarPelicula(){
        val storageReference = FirebaseStorage.getInstance().reference.child("Task Images")
            .child(uri!!.lastPathSegment!!)
        val builder = AlertDialog.Builder(this@SubirActivity)
        builder.setCancelable(false)
        builder.setView(R.layout.progressbar_layout)
        val dialog= builder.create()
        dialog.show()

        storageReference.putFile(uri!!).addOnSuccessListener { taskSnapshot ->
            val uriTask = taskSnapshot.storage.downloadUrl
            while (!uriTask.isComplete);
            val urlImage = uriTask.result
            imagenURL = urlImage.toString()
            uploadData()
            dialog.dismiss()
        }.addOnFailureListener{
            dialog.dismiss()
        }
    }
    private fun uploadData(){
        val titulo = binding.subirtitulo.text.toString()
        val director = binding.subirDirector.text.toString()
        val genero = binding.subirGenero.text.toString()
        val pelicula = PeliculaClass(titulo,director,genero,imagenURL)
        val currentDate=DateFormat.getTimeInstance().format(Calendar.getInstance().time)
        FirebaseDatabase.getInstance().getReference("ProjectCineYa").child(currentDate)
            .setValue(pelicula).addOnCompleteListener{task ->
                if(task.isSuccessful){
                    Toast.makeText(this@SubirActivity,"Guardado",Toast.LENGTH_SHORT).show()
                    finish()
                }
            }.addOnFailureListener{e ->
                Toast.makeText(this@SubirActivity,e.message.toString(),Toast.LENGTH_SHORT).show()

            }
    }
}