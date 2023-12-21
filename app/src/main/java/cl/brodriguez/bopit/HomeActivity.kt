package cl.brodriguez.bopit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import android.app.AlertDialog
import android.content.Context
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        //Crear el Alert Dialog
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.instrucciones_dialog_layout, null)
        dialogBuilder.setView(dialogView)

        // Obtener las referencias de los elementos del diálogo
        val dialogMessage = dialogView.findViewById<TextView>(R.id.dialog_message)
        val buttonNoMostrar = dialogView.findViewById<Button>(R.id.button_no_mostrar)
        val buttonCerrar = dialogView.findViewById<Button>(R.id.button_cerrar)

        //Texto que mostrará el dialogo
        dialogMessage.text = getString(R.string.TextoInstrucciones)

        val alertDialog = dialogBuilder.create()

        //BOTON NO VOLVER A MOSTRAR
        buttonNoMostrar.setOnClickListener {
            val sharedPreferences = getSharedPreferences("NoVolverAMostrar", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            val valorNoVolverAMostrar = "true"
            editor.putString("NoVolverAMostrar", valorNoVolverAMostrar)
            editor.apply()
            alertDialog.dismiss()
        } //BOTON CERRAR
        buttonCerrar.setOnClickListener {
            alertDialog.dismiss()
        }



        // Mostrar el diálogo
        val sharedPreferences = getSharedPreferences("NoVolverAMostrar", Context.MODE_PRIVATE)
        val noVolverAMostrar = sharedPreferences.getString("NoVolverAMostrar", "")

        alertDialog.show()


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