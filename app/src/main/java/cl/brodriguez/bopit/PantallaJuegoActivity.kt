package cl.brodriguez.bopit

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.Toast
import kotlin.math.abs

class PantallaJuegoActivity : AppCompatActivity() {

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var mediaPlayer2: MediaPlayer
    private lateinit var mediaPlayer3: MediaPlayer


    private lateinit var gestureDetector: GestureDetector


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pantalla_juego)

        gestureDetector = GestureDetector(this, MyGestureListener())



        mediaPlayer = MediaPlayer.create(this, R.raw.win)
        mediaPlayer2 = MediaPlayer.create(this, R.raw.defeat)
        mediaPlayer3 = MediaPlayer.create(this, R.raw.background)

        mediaPlayer3.isLooping = true
        mediaPlayer3.start()
    }

    fun sonidoVictoria(view: View){
        mediaPlayer.start()
    }

    fun sonidoDerrota(view: View){
        mediaPlayer2.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        mediaPlayer2.release()
        mediaPlayer3.release()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        gestureDetector.onTouchEvent(event)
        return super.onTouchEvent(event)
    }

    inner class MyGestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
            //Manejo del evento
            if(abs(e1.x - e2.x) > abs(e1.y - e2.y)){
                if (e1.x < e2.x) {
                    //hacia la derecha
                    mostrarMensajeToast("Deslizamiento hacia la derecha")
                } else {
                    //hacia la izquierda
                    mostrarMensajeToast("Deslizamiento hacia la izquierda")
                }
            }else{
                if (e1.y > e2.y) {
                    //hacia arriba
                    mostrarMensajeToast("Deslizamiento hacia arriba")
                } else {
                    //hacia abajo
                    mostrarMensajeToast("Deslizamiento hacia abajo")
                }
            }

            return true
        }
    }

    private fun mostrarMensajeToast(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }


}