package com.dicoding.courseschedule.ui.add

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dicoding.courseschedule.R
import com.dicoding.courseschedule.ui.list.ListViewModelFactory
import com.dicoding.courseschedule.util.TimePickerFragment
import java.text.SimpleDateFormat
import java.util.*

class AddCourseActivity : AppCompatActivity(), TimePickerFragment.DialogTimeListener {

    private lateinit var viewModel: AddCourseViewModel
    private var startTime: String = ""
    private var endTime: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_course)

        val factory = ListViewModelFactory.createFactory(this)
        viewModel = ViewModelProvider(this,factory)[AddCourseViewModel::class.java]
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_insert -> {
                val editCourse = findViewById<TextView>(R.id.add_course_name)
                val editLecturer = findViewById<TextView>(R.id.add_lecturer_name)
                val editNote = findViewById<TextView>(R.id.add_note)
                val editDay = findViewById<Spinner>(R.id.dayChooser)

                val course = editCourse.text.toString().trim()
                val lecturer = editLecturer.text.toString().trim()
                val note = editNote.text.toString().trim()
                val day = editDay.selectedItemPosition
                val start = findViewById<TextView>(R.id.add_tv_start_time).text.toString()
                val end = findViewById<TextView>(R.id.add_tv_end_time).text.toString()

                if (course.isNotEmpty() && lecturer.isNotEmpty() && note.isNotEmpty()) {
                    viewModel.insertCourse(
                        courseName = course,
                        day = day,
                        startTime = start,
                        endTime = end,
                        lecturer = lecturer,
                        note = note
                    )
                    finish()
                } else {
                    Toast.makeText(applicationContext, getString(R.string.empty_list_message), Toast.LENGTH_SHORT).show()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun showStartTimePicker(view: View){
        val timePickerFragment = TimePickerFragment()
        timePickerFragment.show(supportFragmentManager, "startPicker")
    }

    fun showEndTimePicker(view: View){
        val timePickerFragment = TimePickerFragment()
        timePickerFragment.show(supportFragmentManager, "endPicker")
    }

    override fun onDialogTimeSet(tag: String?, hour: Int, minute: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        when (tag) {
            "startPicker" -> {
                findViewById<TextView>(R.id.add_tv_start_time).text = timeFormat.format(calendar.time)
                startTime = timeFormat.format(calendar.time)
            }
            "endPicker" -> {
                findViewById<TextView>(R.id.add_tv_end_time).text = timeFormat.format(calendar.time)
                endTime = timeFormat.format(calendar.time)
            }
        }
    }

}