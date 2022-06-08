package cl.accenture.githubjavapop.view

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat.getDrawable
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import cl.accenture.githubjavapop.R
import cl.accenture.githubjavapop.adapter.PullAdapter
import cl.accenture.githubjavapop.databinding.FragmentPullRequestsBinding
import cl.accenture.githubjavapop.model.ApiState
import cl.accenture.githubjavapop.model.Pull
import cl.accenture.githubjavapop.viewmodel.RequestPullListViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val CONTENT_STATE_CONTENT = 0
private const val CONTENT_STATE_LOADING = 1
private const val CONTENT_STATE_ERROR = 2

class PullRequestsFragment : Fragment() {
    private val pullViewModel by viewModel<RequestPullListViewModel>()
    private val binding by lazy { FragmentPullRequestsBinding.inflate(layoutInflater) }
    private val adapter = PullAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repo = arguments?.getString("repo").toString()
        val user = arguments?.getString("user").toString()
        binding.toolbarPull.title = repo
        binding.toolbarPull.navigationIcon =  getDrawable(resources, R.drawable.ico_back,null)
        binding.toolbarPull.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.rcr.layoutManager = LinearLayoutManager(context)
        binding.rcr.adapter = adapter
        if (isNetworkAvailable()) {
            pullViewModel.statePullList.observe(this, ::pullListObserver)
        } else {
            binding.progressbar.visibility = View.INVISIBLE
            binding.viewFliper.showNext()
            binding.txtConnection.setText(R.string.noInternetMessage)
        }
        pullViewModel.loadList(user, repo)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    private fun pullListObserver(statePullList: ApiState<List<Pull>>) {
        when (statePullList) {
            is ApiState.Error -> {
                if(statePullList.error.message.toString().equals("HTTP 403 ")){
                    binding.txtConnection.setText(R.string.exceededLimit)
                }else if(statePullList.error.message.toString().equals("HTTP 422 ")){
                    binding.txtConnection.setText(R.string.parametersNoCompleted)
                }
                setViewState(CONTENT_STATE_ERROR)
            }
            is ApiState.Loading -> setViewState(CONTENT_STATE_LOADING)

            is ApiState.Success -> {
                if(statePullList.value.isEmpty()){
                    binding.txtConnection.setText(R.string.connectServerMessage)
                    setViewState(CONTENT_STATE_ERROR)
                }else{
                    adapter.addList(statePullList.value)
                    val openCount = adapter.open.size
                    val closeCount = adapter.close.size
                    val opensText = "$openCount Opens"
                    val closedText = "$closeCount Closed"
                    binding.opens.text = opensText
                    binding.closed.text = closedText
                    setViewState(CONTENT_STATE_CONTENT)
                }
            }
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }

    private fun setViewState(state: Int) {
        binding.viewFliper.displayedChild = state
    }
}