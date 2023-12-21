package cl.brodriguez.bopit

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast

class PantallaFinActivity : AppCompatActivity() {

    private lateinit var textViewPuntajeObtenido: TextView
    private lateinit var textViewPuntajeMayorPrevio: TextView
    private lateinit var textViewResultado: TextView
    private lateinit var textViewConclusion: TextView
    private lateinit var textViewPuntajeMayorNuevo: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pantalla_fin)

        //Pantalla completa
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )

        //Text views
        textViewPuntajeMayorPrevio = findViewById(R.id.TextView2)
        textViewPuntajeObtenido = findViewById(R.id.TextView4)
        textViewResultado = findViewById(R.id.TextView5)
        textViewConclusion = findViewById(R.id.TextView6)
        textViewPuntajeMayorNuevo = findViewById(R.id.TextView7)

        //Recuperar valor
        val puntajeObtenido = intent.getIntExtra("PUNTAJE", 0)
        textViewPuntajeObtenido.text = "$puntajeObtenido"

        //Guardar puntaje máximo
        val sharedPreferences = getSharedPreferences("PuntajeMayor", Context.MODE_PRIVATE)
        val puntajeMayorPrevio = sharedPreferences.getInt("puntaje_mayor", 0)

        if (puntajeObtenido > puntajeMayorPrevio){
            val editor = sharedPreferences.edit()
            editor.putInt("puntaje_mayor", puntajeObtenido)
            editor.apply()
            textViewPuntajeMayorNuevo.text = "$puntajeObtenido"
            textViewResultado.text = getString(R.string.PuntajeMayorSuperado)
            textViewConclusion.text = getString(R.string.PuntajeMayorNuevo)
        }else{
            textViewPuntajeMayorNuevo.text = "$puntajeMayorPrevio"
            textViewResultado.text = getString(R.string.PuntajeMayorNoSuperado)
            textViewConclusion.text = getString(R.string.PuntajeMayorSeMantiene)
        }

        //Mostrar puntajes máximos
        textViewPuntajeMayorPrevio.text = "$puntajeMayorPrevio"


    }

    fun irAMenu(view: View){
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }


}