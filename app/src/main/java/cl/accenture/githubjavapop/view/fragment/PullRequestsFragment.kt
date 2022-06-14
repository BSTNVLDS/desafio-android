package cl.accenture.githubjavapop.view.fragment

import android.os.Bundle
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
import cl.accenture.githubjavapop.model.GitHubByPageError
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
        addNavegation(repo)
        binding.rcr.layoutManager = LinearLayoutManager(context)
        binding.rcr.adapter = adapter
        pullViewModel.statePullList.observe(this, ::pullListObserver)
        pullViewModel.loadList(user, repo)
    }

    private fun addNavegation(repoName :String) {
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
                repoListErrorHandler(statePullList.error)
                setViewState(CONTENT_STATE_ERROR)
            }
            is ApiState.Loading -> setViewState(CONTENT_STATE_LOADING)

            is ApiState.Success -> {
                if (statePullList.value.isEmpty()) {
                    binding.txtConnection.setText(R.string.connectServerMessage)
                    setViewState(CONTENT_STATE_ERROR)
                } else {
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