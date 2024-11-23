package com.example.focusminder.fragments

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.focusminder.MainActivity
import com.example.focusminder.R
import com.example.focusminder.adapter.TaskAdapter
import com.example.focusminder.databinding.FragmentHomeBinding
import com.example.focusminder.model.Task
import com.example.focusminder.viewmodel.TaskViewModel

class HomeFragment : Fragment(R.layout.fragment_home), SearchView.OnQueryTextListener, MenuProvider {
    private var homeBinding: FragmentHomeBinding? = null
    private val binding get() = homeBinding!!
    private lateinit var taskViewModel: TaskViewModel
    private lateinit var taskAdapter: TaskAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        homeBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up Menu
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        taskViewModel = (activity as MainActivity).taskViewModel
        setupHomeRecyclerView()

        // Floating button click to navigate to AddFragment
        binding.addNoteFab.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addFragment)
        }

        // Get the registered user's name from shared preferences
        val sharedPreferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val userName = sharedPreferences.getString("name", "")

        // Display the user's name in the toolbar
        (activity as MainActivity).supportActionBar?.title = "Welcome, $userName"

        // Button to navigate to StopwatchFragment
        binding.stopwatch.setOnClickListener {
            navigateToStopwatch()
        }
    }

    private fun updateUI(task: List<Task>?) {
        if (task != null) {
            if (task.isNotEmpty()) {
                binding.emptyNotesImage.visibility = View.GONE
                binding.homeRecyclerView.visibility = View.VISIBLE
            } else {
                binding.emptyNotesImage.visibility = View.VISIBLE
                binding.homeRecyclerView.visibility = View.GONE
            }
        }
    }

    private fun setupHomeRecyclerView() {
        taskAdapter = TaskAdapter()
        binding.homeRecyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 2) // Set number of columns to 2
            setHasFixedSize(true)
            adapter = taskAdapter
        }
        // Get the tasks directly
        val tasks = taskViewModel.getAllTasks()
        taskAdapter.differ.submitList(tasks)
        updateUI(tasks)
    }

    private fun searchTask(query: String?) {
        val searchResults = taskViewModel.searchTask(query)
        taskAdapter.differ.submitList(searchResults)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText != null) {
            searchTask(newText)
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        homeBinding = null
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.home_menu, menu)
        val menuSearch = menu.findItem(R.id.searchMenu).actionView as SearchView
        menuSearch.setOnQueryTextListener(this)
    }

    private fun navigateToStopwatch() {
        findNavController().navigate(R.id.action_homeFragment_to_stopwatchFragment)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return false
    }
}
