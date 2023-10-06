package cl.brodriguez.bopit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
    }

    fun irAPantallaJuego(view: View){
        val intent = Intent(this, PantallaJuegoActivity::class.java)
        startActivity(intent)
    }

    fun irAAcercaDe(view: View){
        val intent = Intent(this, AcercaDeActivity::class.java)
        startActivity(intent)
    }

    fun irAPreferencias(view: View){
        val intent = Intent(this, PreferenciasActivity::class.java)
        startActivity(intent)
    }

}