package com.example.projectcineya

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.example.projectcineya.databinding.ActivityMainBinding
import com.example.projectcineya.databinding.ActivityMapsBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding:ActivityMainBinding
    private lateinit var dataList: ArrayList<PeliculaClass>
    private lateinit var adapter: PeliculaAdapter
    var databaseReference:DatabaseReference?=null
    var eventListener:ValueEventListener?=null
    private lateinit var drawerLayout:DrawerLayout

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
                    peliculaClass?.key=itemSnapshot.key
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
        drawerLayout=findViewById<DrawerLayout>(R.id.drawerLayout)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        val toggle=ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.abrirnav,R.string.cerrarnav)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()


    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.nav_cines -> {
                val intent = Intent(this, MapsActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_inicio -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }

            R.id.nav_salir-> Toast.makeText(this, "Saliste de la sesión!", Toast.LENGTH_SHORT).show()
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            onBackPressedDispatcher.onBackPressed()
        }
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