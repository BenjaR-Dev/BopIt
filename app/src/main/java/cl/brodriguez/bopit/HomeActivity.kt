package cl.brodriguez.bopit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        //Pantalla completa
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_IMMERSIVE or
                        View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                )

        //Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //supportActionBar?.title = getString(R.string.Inicio)
        supportActionBar?.title = "Inicio"
        supportActionBar?.setDisplayHomeAsUpEnabled(false)


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.home_toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed() // Acción al presionar la flecha hacia atrás
                true
            }

            R.id.enviar_a_acerca_de -> {
                irAAcercaDe()
                true
            }

            R.id.enviar_a_preferencias -> {
                irAPreferencias()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }


    fun irAPantallaJuego(view: View){
        val intent = Intent(this, PantallaJuegoActivity::class.java)
        startActivity(intent)
    }

    fun irAAcercaDe(){
        val intent = Intent(this, AcercaDeActivity::class.java)
        startActivity(intent)
    }

    fun irAPreferencias(){
        val intent = Intent(this, PreferenciasActivity::class.java)
        startActivity(intent)
    }

}