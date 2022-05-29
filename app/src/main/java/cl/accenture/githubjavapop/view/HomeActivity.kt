package cl.accenture.githubjavapop.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cl.accenture.githubjavapop.adapter.RepoAdapter
import cl.accenture.githubjavapop.databinding.ActivityHomeBinding
import cl.accenture.githubjavapop.viewmodel.HomeViewModel

class HomeActivity : AppCompatActivity() {
    private var _viewModel: HomeViewModel? = null
    private val viewModel get() = this._viewModel
    private var _binding: ActivityHomeBinding? = null
    private val binding get() = this._binding
    private val adapter = RepoAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding?.root
        setContentView(view)
        binding?.rc?.layoutManager = LinearLayoutManager(this)
        binding?.rc?.adapter = adapter
        _viewModel = ViewModelProvider(this).get()
        viewModel?.repoList?.observe(this) { repolist ->
            adapter.addList(repolist)
        }
        load()
    }

    private fun load() {
        viewModel?.pagination(this)
        adapter.setOnclickListener {
            val positionTap = binding?.rc?.getChildAdapterPosition(it)
            val selectTap = adapter.list[positionTap ?: 0]
            val intentPRL = Intent(this, RequestPullList::class.java)
            intentPRL.putExtra("repo", selectTap.name)
            intentPRL.putExtra("user", selectTap.owner.login)
            startActivity(intentPRL)

        }
        binding?.rc?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    viewModel?.pagination(applicationContext)
                }
            }

        })
    }
}

