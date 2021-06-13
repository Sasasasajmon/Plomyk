package szymon.swic.plomyk.features.songs.list.presentation

import android.annotation.SuppressLint
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.songlist_fragment.*
import szymon.swic.plomyk.R
import szymon.swic.plomyk.core.base.BaseFragment
import szymon.swic.plomyk.features.songs.SongBookActivity
import szymon.swic.plomyk.features.songs.details.presentation.SongDetailsFragment
import szymon.swic.plomyk.features.songs.domain.model.Song
import szymon.swic.plomyk.features.tuner.TunerFragment

class SongListFragment : BaseFragment<SongBookViewModel>(R.layout.songlist_fragment) {

    override val viewModel: SongBookViewModel by viewModel()
    private lateinit var songListAdapter: SongListAdapter

    companion object {
        fun newInstance() = SongListFragment()
    }

    override fun initViews() {
        super.initViews()
        setupSongListRecyclerView()
        activity?.title = resources.getString(R.string.title_songs)

        buttonTuner.setOnClickListener {
            (activity as SongBookActivity).replaceFragment(TunerFragment.newInstance(), true)
        }
    }

    override fun initObservers() {
        super.initObservers()
        observeSongs()
    }

    override fun onIdleState() {
        super.onIdleState()
        songlistProgress.isVisible = false
    }

    override fun onPendingState() {
        super.onPendingState()
        songlistProgress.isVisible = true
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_bar_menu, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView: SearchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newQueryText: String?): Boolean {
                songListAdapter.setSongs(viewModel.getFilteredSongs(newQueryText))
                return false
            }
        })
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun setupSongListRecyclerView() {

        songListAdapter = SongListAdapter()
        songListAdapter.onSongClickListener = ::onSongClick

        songlist_recycler_view.apply {
            layoutManager = LinearLayoutManager(this@SongListFragment.context)
            adapter = songListAdapter
            setHasFixedSize(true)
        }
    }

    @SuppressLint("RestrictedApi")
    private fun observeSongs() {
        viewModel.songs.observe(viewLifecycleOwner) {
            if (!hasOptionsMenu()) setHasOptionsMenu(true)
            songListAdapter.setSongs(it)
        }
    }

    private fun onSongClick(target_song: Song) {
        (activity as SongBookActivity).replaceFragment(SongDetailsFragment.newInstance(target_song), true)
    }
}
