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

class AddCourseActivity : AppCompatActivity(),TimePickerFragment.DialogTimeListener {

    private lateinit var viewModel:AddCourseViewModel

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
                val editCourse = findViewById<TextView>(R.id.add_ed_course)
                val editLecturer = findViewById<TextView>(R.id.add_ed_lecturer)
                val editNote = findViewById<TextView>(R.id.add_ed_note)
                val editDay = findViewById<Spinner>(R.id.daySpinnerChooser)

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
                    showMessage(getString(R.string.empty_list_message))
                }

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun showTimePickerStartTime(view: View){
        val dialogFragment = TimePickerFragment()
        dialogFragment.show(supportFragmentManager,"start")
    }

    fun showTimePickerEndTime(view: View){
        val dialogFragment = TimePickerFragment()
        dialogFragment.show(supportFragmentManager,"end")
    }

    private fun showMessage(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDialogTimeSet(tag:String?,hour:Int,minute:Int){
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        if (tag == "start") {
            findViewById<TextView>(R.id.add_tv_start_time).text = dateFormat.format(calendar.time)
        } else {
            findViewById<TextView>(R.id.add_tv_end_time).text = dateFormat.format(calendar.time)
        }
    }
}


