package cl.brodriguez.bopit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import android.content.Context
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.preference.SwitchPreferenceCompat


class PreferenciasActivity : AppCompatActivity() {

    private lateinit var textViewMejorPuntaje: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)

        //Pantalla completa
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )

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

        /*
        // Guardar un nuevo valor en SharedPreferences
        val sharedPreferences2 = getSharedPreferences("NoVolverAMostrar", Context.MODE_PRIVATE)
        val editor = sharedPreferences2.edit()
        val valorNoVolverAMostrar = "false"
        editor.putString("NoVolverAMostrar", valorNoVolverAMostrar)
        editor.apply()
        */




        textViewMejorPuntaje = findViewById(R.id.TextView7)

        textViewMejorPuntaje.text = "El mejor puntaje es: $mejorPuntaje"

        //Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //supportActionBar?.title = getString(R.string.toolbar_home_titulo)
        supportActionBar?.title = "Preferencias"
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.basic_toolbar, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed() // Acción al presionar la flecha hacia atrás
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }




    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)

        }
    }
}


class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)


        // referencia del switch
        val switchPreference = findPreference<SwitchPreferenceCompat>("novolveramostrar")
        Toast.makeText(requireContext(), "ME CAGO EN TODO", Toast.LENGTH_SHORT).show()

        val sharedPreferences = requireActivity().getSharedPreferences("NoVolverAMostrar", Context.MODE_PRIVATE)
        val valorNoVolverAMostrar = sharedPreferences.getString("NoVolverAMostrar", "")

        switchPreference?.isChecked = valorNoVolverAMostrar == "true"

        switchPreference?.setOnPreferenceChangeListener { preference, newValue ->
            val switched = newValue as Boolean

            val editor = sharedPreferences.edit()
            editor.putString("NoVolverAMostrar", switched.toString())
            editor.apply()


            true
        }
    }
}