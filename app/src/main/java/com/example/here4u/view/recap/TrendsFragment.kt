package com.example.here4u.view.recap

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.here4u.databinding.FragmentTrendsBinding
import com.example.here4u.utils.FileUtils.openPdf
import com.example.here4u.utils.NetworkUtils
import com.example.here4u.viewmodel.TrendsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TrendsFragment : Fragment() {

    private var _binding: FragmentTrendsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TrendsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTrendsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!NetworkUtils.isNetworkAvailable(requireContext())){
            Log.d("TrendsFragment", "No hay Internet — verificando PDF local...")
            viewModel.getDocument()
            viewModel.lastPdf.observe(viewLifecycleOwner){ lastPdf ->
                if (lastPdf != null) {
                    AlertDialog.Builder(requireContext())
                        .setTitle("No internet Conection")
                        .setMessage(" Would you like to see your last saved  weekly recap?")
                        .setPositiveButton("Yes") { _, _ ->
                            openPdf(requireContext(), lastPdf)
                        }
                        .setNegativeButton("No", null)
                        .show()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "There are no recaps saved locally.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        else{viewModel.loadWeeklyRecap()}

        viewModel.recap.observe(viewLifecycleOwner, Observer { recap ->
            binding.highlightsText.text = if (recap.highlights.isNotEmpty()) {
                recap.highlights.joinToString("\n") { "• $it" }
            } else {
                "No highlights available."
            }

            binding.summaryText.text = if (recap.summary.isNotBlank()) {
                recap.summary
            } else {
                "No summary available."
            }
        })


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
