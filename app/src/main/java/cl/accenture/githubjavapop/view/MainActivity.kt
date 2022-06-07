package cl.accenture.githubjavapop.view

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import cl.accenture.githubjavapop.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = binding.root
        setContentView(view)
      //cambiar cuando este en el pull
    // supportActionBar?.setDisplayHomeAsUpEnabled(true)
       // title = repo
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // finish()algo
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }

    }
}

