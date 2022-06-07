package cl.accenture.githubjavapop.view

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import cl.accenture.githubjavapop.R
import cl.accenture.githubjavapop.adapter.PullAdapter
import cl.accenture.githubjavapop.databinding.ActivityRequestpulllistBinding
import cl.accenture.githubjavapop.model.ApiState
import cl.accenture.githubjavapop.model.Pull
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
        if (isNetworkAvailable()) {
            pullViewModel.statePullList.observe(this,::pullListObserver)

        }else{
            binding.progressbar.visibility= View.INVISIBLE
            binding.viewFliper.showNext()
            binding.txtConnection.setText(R.string.noInternetMessage)
        }
        pullViewModel.loadList(user,repo)
    }

    private fun pullListObserver(statePullList: ApiState<List<Pull>>){
        when(statePullList){
            is ApiState.Error -> {
                //si es 403 supero e limite, si es 200 es que esta vacio
                    messageViewFlipper(R.string.connectServerMessage)
            }
            is ApiState.Loading -> {
                binding.progressbar.visibility= View.VISIBLE
            }
            is ApiState.Success -> {
                adapter.addList(statePullList.value)
                val openCount = adapter.open.size
                val closeCount = adapter.close.size
                val opensText = "$openCount Opens"
                val closedText = "$closeCount Closed"
                binding.opens.text = opensText
                binding.closed.text = closedText
                binding.progressbar.visibility = View.INVISIBLE
            }
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
    private fun messageViewFlipper(message :Int){
        binding.progressbar.visibility= View.INVISIBLE
        binding.viewFliper.showNext()
        binding.txtConnection.setText(message)
    }
}