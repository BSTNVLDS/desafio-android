package cl.accenture.githubjavapop.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import cl.accenture.githubjavapop.adapter.PullAdapter
import cl.accenture.githubjavapop.conexion.APIService
import cl.accenture.githubjavapop.controller.Conexion
import cl.accenture.githubjavapop.databinding.ActivityRequestpulllistBinding
import cl.accenture.githubjavapop.model.Pull
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RequestPullList : AppCompatActivity() {
    private lateinit var binding :ActivityRequestpulllistBinding
    private val adapter = PullAdapter()
    private var estado = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRequestpulllistBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val extra =intent.extras!!
        val repo = extra.getString("repo")
        val user = extra.getString("user")
        title = repo
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        initRecycleView()
            loadLista("$user/$repo")



    }

    private fun initRecycleView() {
        binding.rcr.layoutManager = LinearLayoutManager(this)
        binding.rcr.adapter = adapter

    }

    private fun loadLista(fullname: String) {
        CoroutineScope(Dispatchers.IO).launch {
            var page =1
            while (estado){
                val call = Conexion.getRetrofit("https://api.github.com/repos/")
                    .create(APIService::class.java).getPullByRepo("$fullname/pulls?per_page=100&state=all&page=$page")
                runOnUiThread {
                    if (call.isSuccessful) {
                        val pullreq = call.body() ?: emptyList()
                        adapter.addList(pullreq.map { Pull(it.title,it.body,it.state,it.user) })
                        val open_count =adapter.open.size
                        val close_count =adapter.close.size
                        binding.opens.text= "$open_count Opens"
                        binding.closed.text= "$close_count Closed"
                        page++
                    } else {
                        estado=false
                        val error_code = call.code()
                        Toast.makeText(applicationContext,"error code: $error_code", Toast.LENGTH_SHORT)
                            .show()
                    }
            }

            }

        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
       return when (item.itemId) {
            android.R.id.home ->{
                finish()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }

    }
}