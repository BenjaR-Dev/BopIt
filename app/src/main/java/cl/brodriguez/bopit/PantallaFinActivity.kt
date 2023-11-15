package cl.brodriguez.bopit

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView

class PantallaFinActivity : AppCompatActivity() {

    private lateinit var textViewPuntajeObtenido: TextView
    private lateinit var textViewPuntajeMayor: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pantalla_fin)

        //Text views
        textViewPuntajeObtenido = findViewById(R.id.textViewPuntajeObtenido)
        textViewPuntajeMayor = findViewById(R.id.textViewMejorPuntaje)

        //Recuperar valor
        val puntajeObtenido = intent.getIntExtra("PUNTAJE", 0)
        textViewPuntajeObtenido.text = "$puntajeObtenido"

        //Guardar puntaje máximo si es necesario
        val sharedPreferences = getSharedPreferences("PuntajeMayor", Context.MODE_PRIVATE)
        val puntajeMayor = sharedPreferences.getInt("puntaje_mayor", 0)

        if (puntajeObtenido > puntajeMayor){
            val editor = sharedPreferences.edit()
            editor.putInt("puntaje_mayor", puntajeObtenido)
            editor.apply()
        }

        //Mostrar puntaje máximo
        textViewPuntajeMayor.text = "$puntajeMayor"

    }

    fun irAMenu(view: View){
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }


}