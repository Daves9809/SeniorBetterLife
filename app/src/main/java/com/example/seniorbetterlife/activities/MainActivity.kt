package com.example.seniorbetterlife.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.seniorbetterlife.R
import com.example.seniorbetterlife.data.repositories.FirebaseRepository
import com.example.seniorbetterlife.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val fbRepository = FirebaseRepository()
    private val cloud = FirebaseFirestore.getInstance()
    private val MAIN_DEBUG = "LOG_DEBUG"

    private lateinit var binding: ActivityMainBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        /*
        konfiguracja nawigacji
         */
        val navMenu = binding.bottomNavigationView // przypisanie menuNawigacji(dolny panel)
        val navController = findNavController(R.id.navFragment) // przypisanie navControllera
        navMenu.setupWithNavController(navController) // "połączenie dolnej nawigacji z górną
        //val appBarConfiguration = AppBarConfiguration(setOf(R.id.homeFragment,R.id.profileFragment))
        setupActionBarWithNavController(navController) // przypisuje nazwy "labeli" do górnego paska


    }

    // zmiana fragmentu
    /*private fun setCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment,fragment)
            commit()
        }
    }

     */

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.app_bar_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.miPower -> signOut()
        }
        return true
    }

    /*private fun getUserData() {
        val docRef = cloud.collection("users").document(auth.currentUser?.uid!!)
        docRef.get().addOnSuccessListener { user ->
            val city = user.toObject<User>()
        }
    }

     */

    /*
    wylogowanie użytkownika
     */
    private fun signOut() {
        auth.signOut()
        signOutUser()
    }

    private fun signOutUser() {
        val intent = Intent(this.applicationContext, LoginActivity::class.java).apply {
            flags = (Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK) // flag activity zabezpieczaja przed powrotem
        }
        startActivity(intent)
        finish()
    }


}