package com.example.projectcineya

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.projectcineya.databinding.ActivityLogeoBinding
import com.google.firebase.auth.FirebaseAuth

class LogeoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLogeoBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogeoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth= FirebaseAuth.getInstance()
        binding.btnIniciarsesion.setOnClickListener {
            val email = binding.logeoEmail.text.toString()
            val password = binding.logeoPassword.text.toString()
            if(email.isNotEmpty() && password.isNotEmpty()){
                firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
                    if(it.isSuccessful){
                        val intent = Intent(this,MainActivity::class.java)
                        startActivity(intent)
                    }else{
                        Toast.makeText(this,it.exception.toString(),Toast.LENGTH_SHORT).show()
                    }
                }
            }else{
                Toast.makeText(this,"Los campos no pueden estar vacíos",Toast.LENGTH_SHORT).show()
            }
        }
        binding.olvidePassword.setOnClickListener{
            val builder = AlertDialog.Builder(this)
            val view = layoutInflater.inflate(R.layout.dialog_olvidepassword,null)
            val correoUsuario = view.findViewById<EditText>(R.id.editBox)
            builder.setView(view)
            val dialog = builder.create()

            view.findViewById<Button>(R.id.btnResetear).setOnClickListener {
                compararCorreo(correoUsuario)
                dialog.dismiss()
            }

            view.findViewById<Button>(R.id.btnCancelar).setOnClickListener {
                dialog.dismiss()
            }
            if(dialog.window!=null){
                dialog.window!!.setBackgroundDrawable(ColorDrawable(0))
            }
            dialog.show()

        }


        binding.signupRedirectText.setOnClickListener{
            val registroIntent = Intent(this,RegistroActivity::class.java)
            startActivity(registroIntent)
        }
    }
    private fun compararCorreo(correo:EditText){
        if(correo.text.toString().isEmpty()){
            return
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(correo.text.toString()).matches()){
            return
        }
        firebaseAuth.sendPasswordResetEmail(correo.text.toString()).addOnCompleteListener{task ->
            if(task.isSuccessful){
                Toast.makeText(this,"Verifica tu correo :)",Toast.LENGTH_SHORT).show()
            }
        }
    }
}