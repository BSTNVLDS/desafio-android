package cl.accenture.githubjavapop.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import cl.accenture.githubjavapop.adapter.PullAdapter
import cl.accenture.githubjavapop.databinding.ActivityRequestpulllistBinding
import cl.accenture.githubjavapop.viewmodel.RequestPullListViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class RequestPullList : AppCompatActivity() {
    private val pullViewModel by viewModel<RequestPullListViewModel>()
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

        pullViewModel.loadList(user,repo)
        pullViewModel.pullList.observe(this){ pullList->
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
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }

    }
}