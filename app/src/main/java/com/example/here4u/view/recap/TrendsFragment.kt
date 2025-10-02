package com.example.here4u.view.recap

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.here4u.databinding.FragmentTrendsBinding
import com.example.here4u.viewmodel.TrendsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TrendsFragment : Fragment() {

    private var _binding: FragmentTrendsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TrendsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTrendsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.recap.observe(viewLifecycleOwner, Observer { recap ->
            binding.highlightsText.text =
                if (recap.highlights.isNotEmpty())
                    recap.highlights.joinToString("\n") { "• $it" }
                else
                    "No highlights available."

            binding.summaryText.text =
                if (recap.summary.isNotBlank())
                    recap.summary
                else
                    "No summary available."
        })

        viewModel.loadWeeklyRecap()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
