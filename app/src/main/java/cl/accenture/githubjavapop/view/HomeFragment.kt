package cl.accenture.githubjavapop.view

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cl.accenture.githubjavapop.R
import cl.accenture.githubjavapop.adapter.RepoAdapter
import cl.accenture.githubjavapop.databinding.FragmentHomeBinding
import cl.accenture.githubjavapop.model.ApiState
import cl.accenture.githubjavapop.model.Repo
import cl.accenture.githubjavapop.viewmodel.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class HomeFragment : Fragment() {
    private val homeViewModel by viewModel<HomeViewModel>()
    private val binding by lazy { FragmentHomeBinding.inflate(layoutInflater) }
    private val adapter = RepoAdapter()
    private var page = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.rc.layoutManager = LinearLayoutManager(context)
        binding.rc.adapter = adapter
        if (isNetworkAvailable()) {
            homeViewModel.stateRepoList.observe(this, ::repoListObserver)
            loadRepoList()
        } else {
            messageViewFlipper(R.string.noInternetMessage)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }


    private fun repoListObserver(stateRepoList: ApiState<List<Repo>>) {
        when (stateRepoList) {
            is ApiState.Error -> {
                stateRepoList.error
                messageViewFlipper(R.string.connectServerMessage)
            }
            is ApiState.Loading -> {
                binding.progressBar.visibility = View.VISIBLE
            }
            is ApiState.Success -> {
                binding.progressBar.visibility = View.INVISIBLE
                adapter.addList(stateRepoList.value)
            }
        }

    }

    private fun isNetworkAvailable(): Boolean {
        val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }

    private fun loadRepoList() {
        homeViewModel.loadListByPage(page)
        adapter.setOnclickListener { selectedChild ->
            val positionInList = binding.rc.getChildAdapterPosition(selectedChild)
            val selectRepo = adapter.list[positionInList]
            val bundle = Bundle()
            bundle.putString("repo", selectRepo.name)
            bundle.putString("user", selectRepo.owner.login)
            binding.root.findNavController()
                .navigate(R.id.action_homeFragment_to_pullRequestsFragment,bundle)

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

    private fun messageViewFlipper(message: Int) {
        binding.progressBar.visibility = View.INVISIBLE
        binding.viewFliper.showNext()
        binding.txtConnection.setText(message)
    }
}

