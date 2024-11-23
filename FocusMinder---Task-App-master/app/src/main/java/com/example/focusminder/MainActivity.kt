package com.example.focusminder

import android.Manifest
import android.provider.Settings
import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.example.focusminder.fragments.StopwatchFragment
import com.example.focusminder.sharedpreferences.TaskSharedPreferences
import com.example.focusminder.repository.TaskRepository
import com.example.focusminder.viewmodel.TaskViewModel
import com.example.focusminder.viewmodel.ViewModelFactory

class MainActivity : AppCompatActivity() {

    private val REQUEST_NOTIFICATION_PERMISSION = 1001
    lateinit var taskViewModel: TaskViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupViewModel()
        checkNotificationPermission()


        // Simulate login and after successful login, request alarm permission
        simulateLoginAndShowAlarmPermissionPopup()


    }

    // Placeholder for login simulation or call actual login logic here
    private fun simulateLoginAndShowAlarmPermissionPopup() {
        // Simulate a successful login
        onLoginSuccess()
    }

    // This method is called when login is successful
    private fun onLoginSuccess() {
        showAlarmPermissionDialog()
    }

    // Set up your ViewModel
    private fun setupViewModel() {
        val taskSharedPreferences = TaskSharedPreferences(this)
        val taskRepository = TaskRepository(taskSharedPreferences)
        val viewModelProviderFactory = ViewModelFactory(taskRepository)

        taskViewModel = ViewModelProvider(this, viewModelProviderFactory)[TaskViewModel::class.java]
    }

    // Check for notification permission (specific to Android 13+)
    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), REQUEST_NOTIFICATION_PERMISSION
                )
            }
        }
    }

    // Show a dialog pop-up to request alarm permission
    private fun showAlarmPermissionDialog() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            if (!alarmManager.canScheduleExactAlarms()) {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Grant Exact Alarm Permission")
                builder.setMessage("This app needs permission to set exact alarms. Do you want to allow it?")
                builder.setPositiveButton("Yes") { _, _ ->
                    // Navigate user to the settings screen to grant exact alarm permission
                    val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                    startActivity(intent)
                }
                builder.setNegativeButton("No") { dialog, _ ->
                    Toast.makeText(this, "Permission Denied. Some features may not work.", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
                val dialog: AlertDialog = builder.create()
                dialog.show()
            } else {
                // Permission already granted
                Toast.makeText(this, "Exact Alarm Permission already granted", Toast.LENGTH_SHORT).show()
            }
        }
    }





    // Handle the result of the notification permission request
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_NOTIFICATION_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Notification Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Notification Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
