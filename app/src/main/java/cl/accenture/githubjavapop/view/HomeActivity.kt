package cl.accenture.githubjavapop.view

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cl.accenture.githubjavapop.adapter.RepoAdapter
import cl.accenture.githubjavapop.databinding.ActivityHomeBinding
import cl.accenture.githubjavapop.viewmodel.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel



class HomeActivity : AppCompatActivity() {
    private val homeViewModel by viewModel<HomeViewModel>()
    private val binding by lazy { ActivityHomeBinding.inflate(layoutInflater) }
    private val adapter = RepoAdapter()
    private var page =1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = binding.root
        setContentView(view)
        binding.rc.layoutManager = LinearLayoutManager(this)
        binding.rc.adapter = adapter
        val connectServerMessage =" Could not connect to the server, please try again later"
        val noInternetMessage =" Ups!! Please turn on mobile data or WiFi"
        if (isNetworkAvailable()){
            homeViewModel.repoList.observe(this) { repoList ->
                if(!repoList.isNullOrEmpty()){
                    adapter.addList(repoList)
                    binding.progressBar.visibility= View.INVISIBLE
                }else{
                    binding.progressBar.visibility= View.INVISIBLE
                    binding.viewFliper.showNext()
                    binding.txtConnection.text=connectServerMessage
                }

            }
            loadRepoList()
        }else{
            binding.progressBar.visibility= View.INVISIBLE
            binding.viewFliper.showNext()
            binding.txtConnection.text=noInternetMessage
        }
    }

    private fun isNetworkAvailable():Boolean {
        val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }

    private fun loadRepoList() {
        homeViewModel.loadListByPage(page)
        adapter.setOnclickListener {selectedChild->
            val positionInList = binding.rc.getChildAdapterPosition(selectedChild)
            val selectRepo = adapter.list[positionInList]
            val intentPullR = Intent(this, RequestPullList::class.java)
            intentPullR.putExtra("repo", selectRepo.name)
            intentPullR.putExtra("user", selectRepo.owner.login)
            startActivity(intentPullR)
        }
        binding.rc.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    page++
                    homeViewModel.loadListByPage(page)
                }
            }

        })
    }
}

