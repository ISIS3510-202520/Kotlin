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
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint   // 👈 Add this
class TrendsFragment : Fragment() {

    private var _binding: FragmentTrendsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TrendsViewModel by viewModels() // Hilt provides it

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
            binding.highlightsText.text = recap.highlights.joinToString("\n") { "• $it" }
            binding.summaryText.text = recap.summary

            val entries = recap.trendPoints.mapIndexed { index, point ->
                Entry(index.toFloat(), point.score)
            }

            val dateLabels = recap.trendPoints.map { point ->
                val sdf = SimpleDateFormat("MM/dd", Locale.getDefault())
                sdf.format(Date(point.date))
            }

            val dataSet = LineDataSet(entries, "Emotion Trend").apply {
                color = android.graphics.Color.BLUE
                valueTextSize = 10f
                setDrawCircles(true)
                setDrawValues(false)
                lineWidth = 2f
            }

            binding.trendsChart.apply {
                data = LineData(dataSet)
                description.isEnabled = false
                axisRight.isEnabled = false

                xAxis.apply {
                    granularity = 1f
                    valueFormatter = IndexAxisValueFormatter(dateLabels)
                    setDrawGridLines(false)
                    position = com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM
                }

                axisLeft.axisMinimum = -5f
                axisLeft.axisMaximum = 5f

                invalidate()
            }
        })

        viewModel.loadWeeklyRecap()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
