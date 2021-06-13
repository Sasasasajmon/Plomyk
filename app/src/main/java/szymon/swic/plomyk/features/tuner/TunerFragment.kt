package szymon.swic.plomyk.features.tuner

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.tuner_fragment.*
import szymon.swic.plomyk.R
import szymon.swic.plomyk.features.songs.SongBookActivity
import szymon.swic.plomyk.features.songs.list.presentation.SongListFragment


class TunerFragment : Fragment() {

    private var permissionToRecordGranted = false
    private val REQUEST_RECORD_AUDIO_PERMISSION = 200

    private lateinit var viewModel: TunerViewModel

    companion object {
        fun newInstance() = TunerFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.tuner_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        handlePermission()

        setupViewModel()
        setupView()

        setFrequencyObserver()
    }

    private fun handlePermission() {
        if (ContextCompat.checkSelfPermission(this.requireContext(), Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.RECORD_AUDIO), REQUEST_RECORD_AUDIO_PERMISSION
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionToRecordGranted = if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        } else {
            false
        }
        if (!permissionToRecordGranted) (activity as SongBookActivity).replaceFragment(
            SongListFragment.newInstance(), addToBackStack = true
        )
    }

    private fun setupViewModel() {
        activity?.let {
            viewModel = ViewModelProviders.of(it)
                .get(TunerViewModel::class.java)
        }
    }

    private fun setupView() {
        button_start.setOnClickListener { viewModel.startAnalysing() }
        button_stop.setOnClickListener { viewModel.stopAnalysing() }
    }

    private fun setFrequencyObserver() {

        val frequencyObserver = Observer<Double> {
            text_curr_frequency.text = "$it Hz"
        }

        viewModel.frequency.observe(viewLifecycleOwner, frequencyObserver)
    }

}
