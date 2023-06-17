package com.example.projectcineya

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import com.example.projectcineya.databinding.ActivityMainBinding
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding
    private lateinit var dataList: ArrayList<PeliculaClass>
    private lateinit var adapter: PeliculaAdapter
    var databaseReference:DatabaseReference?=null
    var eventListener:ValueEventListener?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val gridLayoutManager = GridLayoutManager(this@MainActivity,1)
        binding.recyclerView.layoutManager=gridLayoutManager

        val builder = AlertDialog.Builder(this@MainActivity)
        builder.setCancelable(false)
        builder.setView(R.layout.progressbarcargar_layout)
        val dialog = builder.create()
        dialog.show()

        dataList=ArrayList()
        adapter = PeliculaAdapter(this@MainActivity, dataList)
        binding.recyclerView.adapter=adapter
        databaseReference=FirebaseDatabase.getInstance().getReference("ProjectCineYa")
        dialog.show()

        eventListener= databaseReference!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                dataList.clear()
                for(itemSnapshot in snapshot.children){
                    val peliculaClass = itemSnapshot.getValue(PeliculaClass::class.java)
                    if(peliculaClass!=null){
                        dataList.add(peliculaClass)
                    }
                }
                adapter.notifyDataSetChanged()
                dialog.dismiss()
            }
            override fun onCancelled(error: DatabaseError) {
                dialog.dismiss()
            }
        })

        binding.btnFlotante.setOnClickListener {
            val intent = Intent(this,SubirActivity::class.java)
            startActivity(intent)
        }
        binding.buscar.setOnQueryTextListener(object:SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                buscarLista(newText)
                return true
            }

        })
    }
    fun buscarLista(text:String){
        val buscarLista = ArrayList<PeliculaClass>()
        for(peliculaClass in dataList){
            if(peliculaClass.titulo?.lowercase()?.contains(text.lowercase())==true){
                buscarLista.add(peliculaClass)
            }
        }
        adapter.searchDataList(buscarLista)

    }
}