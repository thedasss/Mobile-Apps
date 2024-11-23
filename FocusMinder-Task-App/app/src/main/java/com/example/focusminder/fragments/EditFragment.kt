package com.example.focusminder.fragments

import android.app.AlarmManager
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.focusminder.AlarmManager.ReminderReceiver
import com.example.focusminder.MainActivity
import com.example.focusminder.R
import com.example.focusminder.databinding.FragmentEditBinding
import com.example.focusminder.model.Task
import com.example.focusminder.viewmodel.TaskViewModel
import java.text.SimpleDateFormat
import java.util.*

class EditFragment : Fragment(R.layout.fragment_edit), MenuProvider {

    private var editTaskBinding: FragmentEditBinding? = null
    private val binding get() = editTaskBinding!!

    private lateinit var taskViewModel: TaskViewModel
    private lateinit var currentTask: Task
    private var calendar: Calendar = Calendar.getInstance()

    private val args: EditFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        editTaskBinding = FragmentEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        taskViewModel = (activity as MainActivity).taskViewModel
        currentTask = args.task!!

        // Pre-fill fields with existing task data
        binding.editNoteTitle.setText(currentTask.taskTitle)
        binding.editNoteDesc.setText(currentTask.taskDesc)

        // Display existing reminder if available
        if (currentTask.reminderDate != 0L) {
            binding.reminderDate.text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(currentTask.reminderDate))
        }

        if (currentTask.reminderTime != 0L) {
            binding.reminderTime.text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(currentTask.reminderTime))
        }

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
                binding.reminderTime.text = String.format("%02d:%02d", hourOfDay, minute)
            }
            TimePickerDialog(
                requireContext(),
                timeListener,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            ).show()
        }

        // Save Task and Reminder
        binding.editNoteFab.setOnClickListener {
            val taskTitle = binding.editNoteTitle.text.toString().trim()
            val taskDesc = binding.editNoteDesc.text.toString().trim()

            if (taskTitle.isNotEmpty()) {
                val updatedTask = Task(
                    currentTask.id,
                    taskTitle,
                    taskDesc,
                    calendar.timeInMillis,  // Save reminder date
                    calendar.timeInMillis   // Save reminder time
                )
                taskViewModel.updateTask(updatedTask)

                // Update the reminder
                setReminder(taskTitle, calendar.timeInMillis)

                Toast.makeText(context, "Task Updated!", Toast.LENGTH_SHORT).show()
                view.findNavController().popBackStack(R.id.homeFragment, false)
            } else {
                Toast.makeText(context, "Please Enter The Task Title", Toast.LENGTH_SHORT).show()
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

    private fun deleteTask() {
        AlertDialog.Builder(requireActivity()).apply {
            setTitle("Delete Task")
            setMessage("Do you want to delete this task?")
            setPositiveButton("Delete") { _, _ ->
                taskViewModel.deleteTask(currentTask)
                Toast.makeText(context, "Task Deleted", Toast.LENGTH_SHORT).show()
                view?.findNavController()?.popBackStack(R.id.homeFragment, false)
            }
            setNegativeButton("Cancel", null)
        }.create().show()
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.menu_edit, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.deleteMenu -> {
                deleteTask()
                true
            }
            else -> false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        editTaskBinding = null
    }
}
