package com.dicoding.courseschedule.ui.add

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dicoding.courseschedule.R
import com.dicoding.courseschedule.util.DayName
import com.dicoding.courseschedule.util.TimePickerFragment
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddCourseActivity : AppCompatActivity(), TimePickerFragment.DialogTimeListener {

    private lateinit var viewModel: AddCourseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_course)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val factory = AddCourseViewModelFactory.createFactory(this)
        viewModel = ViewModelProvider(this, factory).get(AddCourseViewModel::class.java)

        setupSpinner()
        setupButton()

    }

    private fun setupSpinner() {
        val spinner: Spinner = findViewById(R.id.add_spinner)
        ArrayAdapter.createFromResource(
            this,
            R.array.day,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
    }

    private fun setupButton() {

        findViewById<Button>(R.id.btn_start_time).setOnClickListener {
            val timePickerFragmentOne = TimePickerFragment()
            timePickerFragmentOne.show(supportFragmentManager, TIME_PICKER_START_TAG)
        }

        findViewById<Button>(R.id.btn_end_time).setOnClickListener {
            val timePickerFragmentOne = TimePickerFragment()
            timePickerFragmentOne.show(supportFragmentManager, TIME_PICKER_END_TAG)
        }

        findViewById<Button>(R.id.add_btn).setOnClickListener {
            viewModel.insertCourse(
                courseName = findViewById<TextInputEditText>(R.id.add_ed_name).text.toString(),
                day = DayName.getDayNumberByValue(findViewById<Spinner>(R.id.add_spinner).selectedItem.toString()),
                startTime = findViewById<TextView>(R.id.tv_start_time).text.toString(),
                endTime = findViewById<TextView>(R.id.tv_end_time).text.toString(),
                lecturer = findViewById<TextInputEditText>(R.id.add_ed_lecturer).text.toString(),
                note = findViewById<TextInputEditText>(R.id.add_ed_note).text.toString()
            )
            finish()
        }
    }

    override fun onDialogTimeSet(tag: String?, hourOfDay: Int, minute: Int) {

        // Siapkan time formatter-nya terlebih dahulu
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)

        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        // Set text dari textview berdasarkan tag
        when (tag) {
            TIME_PICKER_START_TAG -> findViewById<TextView>(R.id.tv_start_time).text =
                dateFormat.format(calendar.time)

            TIME_PICKER_END_TAG -> findViewById<TextView>(R.id.tv_end_time).text =
                dateFormat.format(calendar.time)

            else -> {
            }
        }
    }

    companion object {
        private const val TIME_PICKER_START_TAG = "TimePickerStart"
        private const val TIME_PICKER_END_TAG = "TimePickerEnd"
    }

}