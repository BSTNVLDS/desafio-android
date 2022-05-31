package cl.accenture.githubjavapop.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.recyclerview.widget.LinearLayoutManager
import cl.accenture.githubjavapop.adapter.PullAdapter
import cl.accenture.githubjavapop.databinding.ActivityHomeBinding
import cl.accenture.githubjavapop.databinding.ActivityRequestpulllistBinding
import cl.accenture.githubjavapop.viewmodel.HomeViewModel
import cl.accenture.githubjavapop.viewmodel.RequestPullListViewModel
import pagerest

class RequestPullList : AppCompatActivity() {
    private val viewModel by lazy { ViewModelProvider(this).get<RequestPullListViewModel>() }
    private val binding by lazy { ActivityRequestpulllistBinding.inflate(layoutInflater) }
    private val adapter = PullAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = binding.root
        setContentView(view)
        val extra =intent.extras
        val repo = extra?.getString("repo").toString()
        val user = extra?.getString("user").toString()
        title = repo
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.rcr.layoutManager = LinearLayoutManager(this)
        binding.rcr.adapter = adapter

        viewModel.loadList(user,repo)
        viewModel.pullList.observe(this){ pullList->
            adapter.addList(pullList)
            val opencount =adapter.open.size
            val closecount =adapter.close.size
            val openstext="$opencount Opens"
            val closedtext ="$closecount Closed"
            binding.opens.text= openstext
            binding.closed.text= closedtext
        }

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
       return when (item.itemId) {
            android.R.id.home ->{
                finish()
                viewModel
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }

    }
}