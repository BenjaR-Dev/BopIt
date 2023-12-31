package cl.brodriguez.bopit

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.Toast
import kotlin.math.abs
import android.os.Handler
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.PlaybackParams
import android.os.Looper
import android.widget.Switch
import android.widget.TextView
import kotlin.random.Random
import android.os.CountDownTimer
import android.widget.ImageView

enum class TipoAccion {
    Deslizamiento_arriba,
    Deslizamiento_abajo,
    Deslizamiento_izquierda,
    Deslizamiento_derecha,
    Agitacion,
    Reposo,
}

class PantallaJuegoActivity : AppCompatActivity(), SensorEventListener  {

    //Reproductores de audio
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var mediaPlayer2: MediaPlayer
    private lateinit var mediaPlayer3: MediaPlayer

    //Detector de gestos
    private lateinit var gestureDetector: GestureDetector

    //Gestor del acelerómetro
    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null

    //Variables usadas para detectar la agitación
    private val umbralAgitacion = 1700f
    private var lastX: Float = 0f
    private var lastY: Float = 0f
    private var lastZ: Float = 0f
    private var lastTime: Long = 0

    //
    private var dif1: Boolean = false
    private var dif2: Boolean = false
    private var dif3: Boolean = false
    private var dif4: Boolean = false
    private var dif5: Boolean = false

    //Variables para verificar qué acción se está realizando y cual se solicita
    private var ultimaAccionRealizada: TipoAccion = TipoAccion.Reposo
    private var accionSolicitada: TipoAccion = TipoAccion.Reposo

    //Manejo del puntaje y textos
    private var puntaje: Int = 0
    private var dificultad: Int = 0
    private lateinit var textViewPuntuacion: TextView
    private lateinit var textViewSolicitud: TextView
    private lateinit var textViewTiempo: TextView
    private lateinit var textViewVidas: TextView
    private lateinit var imageViewAccion: ImageView

    //Handler y Runnable
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var actualizadorPuntaje: Runnable

    //Tiempo
    private var tiempoCuentaRegresiva: Long = 5000
    private var countDownTimer: CountDownTimer? = null

    //Vidas restantes
    private var vidasRestantes: Int = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pantalla_juego)

        //Pantalla completa
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )

        //Se encuentran todos los text view
        textViewPuntuacion = findViewById(R.id.textViewPuntuacion)
        textViewSolicitud = findViewById(R.id.textViewSolicitud)
        textViewTiempo = findViewById(R.id.textViewTiempo)
        textViewVidas = findViewById(R.id.textViewVidas)

        //Se encuentra la imagen del gesto a realizar
        imageViewAccion = findViewById(R.id.iconoAccion)

        gestureDetector = GestureDetector(this, MyGestureListener())
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)



        //Definición de los audios a reproducir
        mediaPlayer = MediaPlayer.create(this, R.raw.win)
        mediaPlayer2 = MediaPlayer.create(this, R.raw.defeat)
        mediaPlayer3 = MediaPlayer.create(this, R.raw.background)

        //Se define el audio de fondo en bucle y se inicia
        mediaPlayer3.isLooping = true
        mediaPlayer3.start()


        aleatorizarAccion()

        iniciarCuentaRegresiva()

        actualizadorPuntaje = object : Runnable {
            override fun run() {
                //Comprobar si alguna acción se está realizando
                comprobarAccion()

                handler.postDelayed(this, 1)
            }
        }

    }

    private fun aumentarDificultad(){
        //Valor del puntaje actual + la dificultad definida en preferencias, esto para calcular la dificultad correspondiente
        var total = puntaje + dificultad

        //Esta variable define cada cuanto puntaje hay un cambio de dificultad
        var saltoDeNivel = 7


        if (total < saltoDeNivel){
            tiempoCuentaRegresiva = 5000
            aumentarVelocidadAudio(1f)
            dif1 = true

        }else if (total < saltoDeNivel*2){
            tiempoCuentaRegresiva = 4000
            aumentarVelocidadAudio(1.2f)
            dif2 = true

        }else if (total < saltoDeNivel*3){
            tiempoCuentaRegresiva = 3000
            aumentarVelocidadAudio(1.4f)
            dif3 = true

        }else if (total < saltoDeNivel*4){
            tiempoCuentaRegresiva = 2000
            aumentarVelocidadAudio(1.7f)
            dif4 = true

        }else if (total < saltoDeNivel*5){
            tiempoCuentaRegresiva = 1000
            aumentarVelocidadAudio(2f)
            dif5 = true

        }

    }

    private fun aumentarVelocidadAudio(velocidad: Float){

        if (vidasRestantes > 0){
            val playbackParams = PlaybackParams()
            playbackParams.speed = velocidad
            mediaPlayer3.playbackParams = playbackParams
        }
    }

    private fun comprobarAccion(){

        //Si el juego queda sin vidas
        if (vidasRestantes == 0){
            vidasRestantes = -1
            mediaPlayer.stop()
            mediaPlayer2.stop()
            mediaPlayer3.stop()
            mediaPlayer.release()
            mediaPlayer2.release()
            mediaPlayer3.release()
            val intent = Intent(this, PantallaFinActivity::class.java)
            intent.putExtra("PUNTAJE", puntaje)
            startActivity(intent)
            finish()
        }

        if(ultimaAccionRealizada != TipoAccion.Reposo){

            if (ultimaAccionRealizada == accionSolicitada) {

                puntaje += 1
                textViewPuntuacion.text = "$puntaje"
                sonidoVictoria()
                ultimaAccionRealizada = TipoAccion.Reposo
                aleatorizarAccion()
                iniciarCuentaRegresiva()

            } else {
                vidasRestantes -= 1
                textViewVidas.text = "Vidas restantes: $vidasRestantes"
                sonidoDerrota()
                ultimaAccionRealizada = TipoAccion.Reposo
            }

        }

        //Aumentar dificultad
        aumentarDificultad()

    }

    private fun iniciarCuentaRegresiva() {
        // Cancelar cualquier cuenta regresiva en curso antes de iniciar una nueva
        countDownTimer?.cancel()

        countDownTimer = object : CountDownTimer(tiempoCuentaRegresiva, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                //Cada segundo
                val segundosRestantes = millisUntilFinished / 1000 + 1
                textViewTiempo.text = "Tiempo restante: $segundosRestantes segundos"

                if (vidasRestantes <= 0){
                    countDownTimer?.cancel()
                }
            }

            override fun onFinish() {
                //Al finalizar la cuenta
                vidasRestantes -= 1
                textViewVidas.text = "Vidas restantes: $vidasRestantes"
                sonidoDerrota()
                ultimaAccionRealizada = TipoAccion.Reposo
                aleatorizarAccion()

                // Iniciar una nueva cuenta regresiva
                iniciarCuentaRegresiva()
            }
        }

        // Iniciar la cuenta regresiva
        countDownTimer?.start()
    }

    private fun aleatorizarAccion() {
        var numeroRandom: Int = Random.nextInt(0, 5)

        if (numeroRandom == 0) {
            accionSolicitada = TipoAccion.Deslizamiento_arriba
            textViewSolicitud.text = "Desliza el dedo hacia arriba"
            imageViewAccion.setImageResource(R.drawable.arriba)
        }

        if (numeroRandom == 1) {
            accionSolicitada = TipoAccion.Deslizamiento_abajo
            textViewSolicitud.text = "Desliza el dedo hacia abajo"
            imageViewAccion.setImageResource(R.drawable.abajo)
        }

        if (numeroRandom == 2) {
            accionSolicitada = TipoAccion.Deslizamiento_izquierda
            textViewSolicitud.text = "Desliza el dedo hacia la izquierda"
            imageViewAccion.setImageResource(R.drawable.izquierda)
        }

        if (numeroRandom == 3) {
            accionSolicitada = TipoAccion.Deslizamiento_derecha
            textViewSolicitud.text = "Desliza el dedo hacia la derecha"
            imageViewAccion.setImageResource(R.drawable.derecha)
        }

        if (numeroRandom == 4){
            accionSolicitada = TipoAccion.Agitacion
            textViewSolicitud.text = "Agita tu celular"
            imageViewAccion.setImageResource(R.drawable.agitar)
        }
    }

    override fun onResume() {
        super.onResume()
        accelerometer?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }

        handler.postDelayed(actualizadorPuntaje, 1)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        val currentTime = System.currentTimeMillis()

        if (currentTime - lastTime > 100) {
            val deltaTime = currentTime - lastTime
            lastTime = currentTime

            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            val deltaX = x - lastX
            val deltaY = y - lastY
            val deltaZ = z - lastZ

            val speed = abs(deltaX + deltaY + deltaZ) / deltaTime * 10000

            if (speed > umbralAgitacion) {
                // Se detectó una agitación
                ultimaAccionRealizada = TipoAccion.Agitacion
                //mostrarMensajeToast("¡Agitación detectada!")
            }

            lastX = x
            lastY = y
            lastZ = z
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    fun sonidoVictoria(){
        mediaPlayer.stop()
        mediaPlayer = MediaPlayer.create(this, R.raw.win)
        mediaPlayer.start()
    }

    fun sonidoDerrota(){

        if (vidasRestantes > 0){
            mediaPlayer2.stop()
            mediaPlayer2 = MediaPlayer.create(this, R.raw.defeat)
            mediaPlayer2.start()
        }
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        gestureDetector.onTouchEvent(event)
        return super.onTouchEvent(event)
    }

    inner class MyGestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
            //Manejo del evento onFling
            if(abs(e1.x - e2.x) > abs(e1.y - e2.y)){
                if (e1.x < e2.x) {
                    //hacia la derecha
                    ultimaAccionRealizada = TipoAccion.Deslizamiento_derecha
                    //mostrarMensajeToast("Deslizamiento hacia la derecha")
                } else {
                    //hacia la izquierda
                    ultimaAccionRealizada = TipoAccion.Deslizamiento_izquierda
                    //mostrarMensajeToast("Deslizamiento hacia la izquierda")
                }
            }else{
                if (e1.y > e2.y) {
                    //hacia arriba
                    ultimaAccionRealizada = TipoAccion.Deslizamiento_arriba
                    //mostrarMensajeToast("Deslizamiento hacia arriba")
                } else {
                    //hacia abajo
                    ultimaAccionRealizada = TipoAccion.Deslizamiento_abajo
                    //mostrarMensajeToast("Deslizamiento hacia abajo")
                }
            }

            return true
        }
    }

    private fun mostrarMensajeToast(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }



}