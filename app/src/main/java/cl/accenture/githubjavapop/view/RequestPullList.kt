package cl.accenture.githubjavapop.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.recyclerview.widget.LinearLayoutManager
import cl.accenture.githubjavapop.adapter.PullAdapter
import cl.accenture.githubjavapop.databinding.ActivityRequestpulllistBinding
import cl.accenture.githubjavapop.viewmodel.RequestPullListViewModel

class RequestPullList : AppCompatActivity() {
    private var _binding: ActivityRequestpulllistBinding? = null
    private val binding get() = this._binding
    private var _viewModel: RequestPullListViewModel? = null
    private val viewModel get() = this._viewModel
    private val adapter = PullAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRequestpulllistBinding.inflate(layoutInflater)
        val view = binding?.root
        setContentView(view)
        val extra =intent.extras
        val repo = extra?.getString("repo")
        val user = extra?.getString("user")
        title = repo
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        _viewModel = ViewModelProvider(this).get()
        binding?.rcr?.layoutManager = LinearLayoutManager(this)
        binding?.rcr?.adapter = adapter

        viewModel?.loadList("$user/$repo",this)
        viewModel?.pullList?.observe(this){ pullList->
            adapter.addList(pullList)
            val opencount =adapter.open.size
            val closecount =adapter.close.size
            val openstext="$opencount Opens"
            val closedtext ="$closecount Closed"
            binding?.opens?.text= openstext
            binding?.closed?.text= closedtext
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