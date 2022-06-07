package cl.accenture.githubjavapop.view

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import cl.accenture.githubjavapop.R
import cl.accenture.githubjavapop.adapter.PullAdapter
import cl.accenture.githubjavapop.databinding.FragmentPullRequestsBinding
import cl.accenture.githubjavapop.model.ApiState
import cl.accenture.githubjavapop.model.Pull
import cl.accenture.githubjavapop.viewmodel.RequestPullListViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PullRequestsFragment : Fragment() {
    private val pullViewModel by viewModel<RequestPullListViewModel>()
    private val binding by lazy { FragmentPullRequestsBinding.inflate(layoutInflater) }
    private val adapter = PullAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val extra =context.intent.extras
        val repo = extra?.getString("repo").toString()
        val user = extra?.getString("user").toString()
        binding.rcr.layoutManager = LinearLayoutManager(context)
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return binding.root
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
        val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }


    private fun messageViewFlipper(message :Int){
        binding.progressbar.visibility= View.INVISIBLE
        binding.viewFliper.showNext()
        binding.txtConnection.setText(message)
    }

}