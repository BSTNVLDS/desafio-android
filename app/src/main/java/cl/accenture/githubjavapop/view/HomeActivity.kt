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
import pagerest

class HomeActivity : AppCompatActivity() {
    private val adapter = RepoAdapter()
    private val viewModel by lazy { ViewModelProvider(this).get<HomeViewModel>() }
    private val binding by lazy { ActivityHomeBinding.inflate(layoutInflater) }
    private var page =1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = binding.root
        setContentView(view)
        binding.rc.layoutManager = LinearLayoutManager(this)
        binding.rc.adapter = adapter
        viewModel.repoList.observe(this) { repolist ->
            adapter.addList(repolist)
        }
        loadRepoList()

    }

    private fun loadRepoList() {
        viewModel.loadListByPage(page)
        adapter.setOnclickListener {
            val positionTap = binding.rc.getChildAdapterPosition(it)
            val selectTap = adapter.list[positionTap]
            val intentPRL = Intent(this, RequestPullList::class.java)
            intentPRL.putExtra("repo", selectTap.name)
            intentPRL.putExtra("user", selectTap.owner.login)
            startActivity(intentPRL)

        }
        binding.rc.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    page++
                    viewModel.loadListByPage(page)
                }
            }

        })
    }
}

