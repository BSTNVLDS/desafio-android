package cl.accenture.githubjavapop.view.fragment

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
import cl.accenture.githubjavapop.model.GitHubByPageError
import cl.accenture.githubjavapop.model.Repo
import cl.accenture.githubjavapop.viewmodel.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val CONTENT_STATE_CONTENT = 0
private const val CONTENT_STATE_LOADING = 1
private const val CONTENT_STATE_ERROR = 2

class HomeFragment : Fragment() {
    private val homeViewModel by viewModel<HomeViewModel>()
    private val binding by lazy { FragmentHomeBinding.inflate(layoutInflater) }
    private val adapter = RepoAdapter()
    private var page = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.rc.layoutManager = LinearLayoutManager(context)
        binding.rc.adapter = adapter
        homeViewModel.stateRepoList.observe(this, ::repoListObserver)
        loadRepoList()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
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
                .navigate(R.id.action_homeFragment_to_pullRequestsFragment, bundle)

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


    private fun repoListObserver(stateRepoList: ApiState<List<Repo>, GitHubByPageError>) {
        when (stateRepoList) {
            is ApiState.Error -> {
                repoListErrorHandler(stateRepoList.error)
                setViewState(CONTENT_STATE_ERROR)
            }
            is ApiState.Loading -> setViewState(CONTENT_STATE_LOADING)

            is ApiState.Success -> {
                if (stateRepoList.value.isEmpty()) {
                    binding.txtConnection.setText(R.string.connectServerMessage)
                    setViewState(CONTENT_STATE_ERROR)
                } else {
                    setViewState(CONTENT_STATE_CONTENT)
                    adapter.addList(stateRepoList.value)
                }
            }
        }
    }

    private fun repoListErrorHandler(error: GitHubByPageError) {
        when (error) {
            is GitHubByPageError.UnprocessableEntity ->
                binding.txtConnection.setText(R.string.parametersNoCompleted)
            is GitHubByPageError.TooManyRequest ->
                binding.txtConnection.setText(R.string.exceededLimit)
            is GitHubByPageError.Unknown ->
                binding.txtConnection.setText(R.string.genericError)
            is GitHubByPageError.NoConnection ->
                binding.txtConnection.setText(R.string.noInternetMessage)
        }
    }

    private fun setViewState(state: Int) {
        binding.viewFliper.displayedChild = state
    }
}

