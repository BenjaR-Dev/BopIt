package cl.brodriguez.bopit

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.os.Handler
import android.os.Looper
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val SPLASH_TIMEOUT: Long = 3000 // Duración de la pantalla de bienvenida en milisegundos

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*//Handler().postDelayed({
            // Este código se ejecutará después de SPLASH_TIMEOUT
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish() // Cierra la actividad de la SplashScreen
        }, SPLASH_TIMEOUT)*/

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()

        }, 2000)




    }

}


