package com.example.smart_diary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.smart_diary.database.NoteDatabase
import com.example.smart_diary.repository.NoteRepository
import com.example.smart_diary.viewmodel.NoteViewModel
import com.example.smart_diary.viewmodel.NoteViewModelFactory

class MainActivity : AppCompatActivity() {

    lateinit var noteViewModel: NoteViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupViewModel()
    }

    private fun setupViewModel(){
        val noteRepository = NoteRepository(NoteDatabase(this))
        val viewModelProviderFactory = NoteViewModelFactory(application,noteRepository)
        noteViewModel=ViewModelProvider(this,viewModelProviderFactory)[NoteViewModel::class.java]
    }
}