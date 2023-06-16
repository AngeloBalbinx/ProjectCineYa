package com.example.projectcineya

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
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
        binding.signupRedirectText.setOnClickListener{
            val registroIntent = Intent(this,RegistroActivity::class.java)
            startActivity(registroIntent)
        }
    }
}