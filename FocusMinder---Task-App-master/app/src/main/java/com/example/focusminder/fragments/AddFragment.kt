package com.example.focusminder.fragments

import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.provider.Settings
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import com.example.focusminder.AlarmManager.ReminderReceiver
import com.example.focusminder.MainActivity
import com.example.focusminder.R
import com.example.focusminder.databinding.FragmentAddBinding
import com.example.focusminder.model.Task
import com.example.focusminder.viewmodel.TaskViewModel
import java.util.*

class AddFragment : Fragment(R.layout.fragment_add), MenuProvider {

    private var addTaskBinding: FragmentAddBinding? = null
    private val binding get() = addTaskBinding!!

    private lateinit var taskViewModel: TaskViewModel
    private lateinit var addTask: View
    private var calendar: Calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        addTaskBinding = FragmentAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        taskViewModel = (activity as MainActivity).taskViewModel
        addTask = view

        // Date Picker
        binding.pickDateButton.setOnClickListener {
            val dateListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                binding.reminderDate.text = "$dayOfMonth/${month + 1}/$year"
            }
            DatePickerDialog(
                requireContext(),
                dateListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        // Time Picker
        binding.pickTimeButton.setOnClickListener {
            val timeListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)
                binding.reminderTime.text = "$hourOfDay:$minute"
            }
            TimePickerDialog(
                requireContext(),
                timeListener,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            ).show()
        }

        // Create task button click
        binding.addNoteFab.setOnClickListener {
            saveTask(view)
        }
    }

    private fun saveTask(view: View) {
        val taskTitle = binding.addNoteTitle.text.toString().trim()
        val taskDesc = binding.addNoteDesc.text.toString().trim()

        if (taskTitle.isEmpty()) {
            Toast.makeText(requireContext(), "Please enter a task title", Toast.LENGTH_SHORT).show()
            return
        }

        if (taskDesc.isEmpty()) {
            Toast.makeText(requireContext(), "Please enter a task description", Toast.LENGTH_SHORT).show()
            return
        }

        // Check for exact alarm permission before setting the alarm
        checkExactAlarmPermission()

        // Create and save task with reminder date and time
        val task = Task(
            0,
            taskTitle,
            taskDesc,
            calendar.timeInMillis,
            calendar.timeInMillis
        )

        taskViewModel.addTask(task)

        // Set Reminder
        setReminder(taskTitle, calendar.timeInMillis)

        // Show a success message and navigate back to HomeFragment
        Toast.makeText(addTask.context, "Task Saved", Toast.LENGTH_SHORT).show()
        view.findNavController().popBackStack(R.id.homeFragment, false)
    }

    // Function to check for exact alarm permission
    private fun checkExactAlarmPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
            if (!alarmManager.canScheduleExactAlarms()) {
                // Send the user to the system settings to grant this permission
                val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                startActivity(intent)
            }
        }
    }
    private fun setReminder(taskTitle: String, triggerAtMillis: Long) {
        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireContext(), ReminderReceiver::class.java).apply {
            putExtra("taskTitle", taskTitle)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent)
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.menu_add, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.saveMenu -> {
                saveTask(addTask)
                true
            }
            else -> false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        addTaskBinding = null
    }
}
