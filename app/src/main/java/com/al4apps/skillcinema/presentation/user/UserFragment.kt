package com.al4apps.skillcinema.presentation.user

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.al4apps.skillcinema.R
import com.al4apps.skillcinema.databinding.DialogLayoutBinding
import com.al4apps.skillcinema.databinding.FragmentUserBinding
import com.al4apps.skillcinema.domain.Constants
import com.al4apps.skillcinema.presentation.adapters.collections.CollectionsAdapter
import com.al4apps.skillcinema.presentation.adapters.movies.DbItemsDelegationAdapter
import com.al4apps.skillcinema.presentation.decor.ItemOffsetDecoration
import com.al4apps.skillcinema.presentation.movie.MovieFragment
import com.al4apps.skillcinema.presentation.staff.PersonFragment
import com.al4apps.skillcinema.utils.AbstractFragment
import com.al4apps.skillcinema.utils.autoCleared
import com.al4apps.skillcinema.utils.onTextChangedListener
import com.al4apps.skillcinema.utils.updateLayoutParamsByWindowInsetsTop
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class UserFragment : AbstractFragment<FragmentUserBinding>(FragmentUserBinding::inflate) {

    @Inject
    lateinit var userViewModelFactory: UserViewModelFactory
    private val viewModel: UserViewModel by viewModels { userViewModelFactory }
    private var watchedFilmsAdapter: DbItemsDelegationAdapter by autoCleared()
    private var interestingFilmsAdapter: DbItemsDelegationAdapter by autoCleared()
    private var collectionsAdapter: CollectionsAdapter by autoCleared()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.root.updateLayoutParamsByWindowInsetsTop()
        binding.collectionsRecyclerView.addItemDecoration(ItemOffsetDecoration(requireContext()))
        initAdapters()
        bindViewModel()
        binding.addNewCollectionLayout.setOnClickListener { showDialogNewCollection() }
        binding.watchedMoviesBlock.allTextView.setOnClickListener {
            navigateToMovies(Constants.MOVIES_COLLECTION_WATCHED_ID)
        }
    }

    private fun initAdapters() {
        watchedFilmsAdapter = getAdapter()
        interestingFilmsAdapter = getAdapter()
        collectionsAdapter =
            CollectionsAdapter(onItemClick = { collectionId -> navigateToMovies(collectionId) },
                onDeleteItemClick = { collectionId -> showDialogDeleteCollection(collectionId) })

        binding.watchedMoviesBlock.setDbItemsAdapter(watchedFilmsAdapter)
        binding.interestingMoviesBlock.setDbItemsAdapter(interestingFilmsAdapter)
        binding.collectionsRecyclerView.adapter = collectionsAdapter
        binding.collectionsRecyclerView.addItemDecoration(ItemOffsetDecoration(requireContext()))
    }

    private fun getAdapter(): DbItemsDelegationAdapter {
        return DbItemsDelegationAdapter(onItemClick = { kinopoiskId, personId, view ->
            kinopoiskId?.let { navigateToMovie(kinopoiskId, view) }
            personId?.let { navigateToPerson(personId, view) }
        })
    }

    private fun navigateToMovie(id: Int, view: View) {
        val transitionName = getString(R.string.movie_transition_name)
        val extras = FragmentNavigatorExtras(view to transitionName)
        val action = R.id.navigation_in_movie3
        val argsBundle = bundleOf(MovieFragment.KINOPOISK_ID_KEY to id)
        findNavController().navigate(action, argsBundle, null, extras)
    }

    private fun navigateToPerson(id: Int, itemView: View) {
        val transitionName = getString(R.string.person_transition_name)
        val extras = FragmentNavigatorExtras(itemView to transitionName)
        val action = R.id.nav_in_person
        val argsBundle = bundleOf(PersonFragment.PERSON_ID_KEY to id)
        findNavController().navigate(action, argsBundle, null, extras)
    }

    private fun navigateToMovies(collectionId: Int) {
        val action = UserFragmentDirections.actionUserFragmentToMoviesDbFragment(collectionId)
        findNavController().navigate(action)
    }

    private fun showDialogDeleteCollection(collectionId: Int) {
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(R.string.profile_delete_collection_dialog_title)
            .setNegativeButton(R.string.profile_delete_collection_dialog_no_button) { dialog, _ ->
                dialog.cancel()
            }.setPositiveButton(R.string.profile_delete_collection_dialog_yes_button) { _, _ ->
                viewModel.deleteCollection(collectionId)
            }.create()
        dialog.show()
    }

    private fun showDialogNewCollection() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_layout, null)
        val dialogBinding = DialogLayoutBinding.bind(dialogView)
        val dialog = AlertDialog.Builder(requireContext()).setView(dialogView).create()
        dialog.show()
        dialogBinding.closeImageView.setOnClickListener { dialog.dismiss() }
        dialogBinding.nameEditText.onTextChangedListener { text ->
            dialogBinding.okButton.isEnabled = text.isNotBlank()
        }
        dialogBinding.okButton.isEnabled = dialogBinding.nameEditText.text.toString().isNotBlank()
        dialogBinding.okButton.setOnClickListener {
            val name = dialogBinding.nameEditText.text.toString()
            viewModel.addNewCollection(name)
            dialog.dismiss()
        }
    }

    private fun bindViewModel() {
        viewModel.watchedMoviesLiveData.observe(viewLifecycleOwner) { collection ->
            binding.watchedMoviesBlock.setMoviesAndPersonsCollection(collection)
            binding.watchedMoviesBlock.allTextView.isVisible =
                collection.list.size > Constants.MOVIES_HORIZONTAL_BLOCK_SIZE
            binding.watchedMoviesBlock.allTextView.text = resources.getString(
                R.string.movie_info_item_count, collection.list.size
            )
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.lastViewsFlow.collectLatest { collection ->
                binding.interestingMoviesBlock.setMoviesAndPersonsCollection(collection)
                binding.interestingMoviesBlock.allTextView.setText(R.string.profile_clear_text)
                binding.interestingMoviesBlock.allTextView.isVisible = collection.list.isNotEmpty()
                binding.interestingMoviesBlock.allTextView.setOnClickListener {
                    viewModel.cleanHistory()
                }
            }
        }
        viewModel.collectionsLiveData.observe(viewLifecycleOwner) { collections ->
            collectionsAdapter.submitList(collections)
        }
    }
}