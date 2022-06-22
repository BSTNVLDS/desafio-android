package cl.accenture.githubjavapop.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat.getDrawable
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cl.accenture.githubjavapop.R
import cl.accenture.githubjavapop.adapter.PullAdapter
import cl.accenture.githubjavapop.databinding.FragmentPullRequestsBinding
import cl.accenture.githubjavapop.model.ApiState
import cl.accenture.githubjavapop.model.GitHubByPageError
import cl.accenture.githubjavapop.model.Pull
import cl.accenture.githubjavapop.model.Repo
import cl.accenture.githubjavapop.util.*
import cl.accenture.githubjavapop.viewmodel.RequestPullListViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class PullRequestsFragment : Fragment() {
    private val pullViewModel by viewModel<RequestPullListViewModel>()
    private val binding by lazy { FragmentPullRequestsBinding.inflate(layoutInflater) }
    private val adapter = PullAdapter()
    private var page = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repoBundle = arguments ?: Bundle()
        val fullRepo :Repo = repoBundle.getParcelable("full_repo") ?: Repo()
        val repo = fullRepo.name
        val user = fullRepo.owner.login
        addNavigation(repo)
        binding.rcr.layoutManager = LinearLayoutManager(context)
        binding.rcr.adapter = adapter
        pullViewModel.statePullList.observe(this, ::pullListObserver)
        pullViewModel.loadListPullByPage(user, repo,page)
        binding.rcr.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    page++
                    pullViewModel.loadListPullByPage(user, repo,page)
                }
            }
        })
    }

    private fun addNavigation(repoName :String) {
        binding.toolbarPull.title = repoName
        binding.toolbarPull.navigationIcon = getDrawable(resources, R.drawable.ico_back, null)
        binding.toolbarPull.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    private fun pullListObserver(statePullList: ApiState<List<Pull>, GitHubByPageError>) {
        when (statePullList) {
            is ApiState.Error -> {
                binding.txtConnection.text =repoListErrorHandler(statePullList.error)
                setViewState(CONTENT_STATE_ERROR)
            }
            is ApiState.Loading -> setViewState(CONTENT_STATE_LOADING)

            is ApiState.Success -> {
                if (statePullList.value.isEmpty()) {
                    binding.txtConnection.text = EMPTY
                    setViewState(CONTENT_STATE_ERROR)
                } else {
                    adapter.addList(statePullList.value)
                    binding.opens.text = adapter.returnOpenPullCount()
                    binding.closed.text = adapter.returnClosePullCount()
                    setViewState(CONTENT_STATE_CONTENT)
                }
            }
        }
    }

    private fun setViewState(state: Int) {
        binding.viewFliper.displayedChild = state
    }

}