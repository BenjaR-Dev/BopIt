package cl.brodriguez.bopit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import android.content.Context
import androidx.preference.EditTextPreference
import android.content.SharedPreferences
import android.widget.TextView


class PreferenciasActivity : AppCompatActivity() {

    private lateinit var textViewMejorPuntaje: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //Persistencia
        val sharedPreferences = getSharedPreferences("PuntajeMayor", Context.MODE_PRIVATE)
        val mejorPuntaje = sharedPreferences.getInt("puntaje_mayor", 0)

        textViewMejorPuntaje = findViewById(R.id.textViewMejorPuntaje)

        textViewMejorPuntaje.text = "El mejor puntaje es: $mejorPuntaje"



    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)

        }
    }
}