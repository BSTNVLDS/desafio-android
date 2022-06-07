package cl.accenture.githubjavapop.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cl.accenture.githubjavapop.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = binding.root
        setContentView(view)
    }
}

