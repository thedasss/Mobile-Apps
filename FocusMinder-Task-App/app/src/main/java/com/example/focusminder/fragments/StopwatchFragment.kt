package com.example.focusminder.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.focusminder.MainActivity
import com.example.focusminder.databinding.FragmentStopwatchBinding

class StopwatchFragment : Fragment() {

    private var _binding: FragmentStopwatchBinding? = null
    private val binding get() = _binding!!

    private var isRunning = false
    private var timeInSeconds = 0
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var runnable: Runnable

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStopwatchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        runnable = Runnable {
            if (isRunning) {
                timeInSeconds++
                updateStopwatchView()
                handler.postDelayed(runnable, 1000)
            }
        }

        binding.startButton.setOnClickListener {
            startStopwatch()
        }

        binding.stopButton.setOnClickListener {
            stopStopwatch()
        }

        binding.resetButton.setOnClickListener {
            resetStopwatch()
        }

        // Button to navigate back to MainActivity
        binding.backToMainButton.setOnClickListener {
            navigateToMainActivity()
        }
    }

    private fun startStopwatch() {
        isRunning = true
        handler.post(runnable)
        binding.startButton.isEnabled = false
        binding.stopButton.isEnabled = true
        binding.resetButton.isEnabled = true
    }

    private fun stopStopwatch() {
        isRunning = false
        handler.removeCallbacks(runnable)
        binding.startButton.isEnabled = true
        binding.stopButton.isEnabled = false
    }

    private fun resetStopwatch() {
        isRunning = false
        handler.removeCallbacks(runnable)
        timeInSeconds = 0
        updateStopwatchView()
        binding.startButton.isEnabled = true
        binding.stopButton.isEnabled = false
        binding.resetButton.isEnabled = false
    }

    private fun updateStopwatchView() {
        val hours = timeInSeconds / 3600
        val minutes = (timeInSeconds % 3600) / 60
        val seconds = timeInSeconds % 60
        binding.timerText.text = String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    // Function to navigate back to MainActivity
    private fun navigateToMainActivity() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
