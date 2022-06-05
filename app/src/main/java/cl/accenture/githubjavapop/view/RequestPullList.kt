package cl.accenture.githubjavapop.view

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.view.size
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
        val connectServerMessage =" Could not connect to the server, please try again later"
        val noInternetMessage =" Ups!! Please turn on mobile data or WiFi"
        if (isNetworkAvailable()) {
            pullViewModel.pullList.observe(this) { pullList ->
                if (!pullList.isNullOrEmpty()) {
                    adapter.addList(pullList)
                    val opencount = adapter.open.size
                    val closecount = adapter.close.size
                    val openstext = "$opencount Opens"
                    val closedtext = "$closecount Closed"
                    binding.opens.text = openstext
                    binding.closed.text = closedtext
                    binding.progressbar.visibility = View.INVISIBLE
                } else {
                    binding.progressbar.visibility= View.INVISIBLE
                    binding.viewFliper.showNext()
                    binding.txtConnection.text=connectServerMessage
                }
            }
        }else{
            binding.progressbar.visibility= View.INVISIBLE
            binding.viewFliper.showNext()
            binding.txtConnection.text=noInternetMessage
        }
    }
    private fun isNetworkAvailable():Boolean {
        val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
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