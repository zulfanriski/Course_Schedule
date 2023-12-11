package com.dicoding.courseschedule.ui.add


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Spinner
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider

import com.dicoding.courseschedule.R
import com.dicoding.courseschedule.util.TimePickerFragment
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddCourseActivity : AppCompatActivity(), TimePickerFragment.DialogTimeListener {
    private lateinit var vM : AddCourseViewModel

    private var mulai = ""
    private var akhir = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_course)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        val fac = AddCourseViewModelFactory.createFactory(this)
        vM = ViewModelProvider(this, fac).get(AddCourseViewModel::class.java)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu_add, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.home -> {
                finish()
                true
            }
            R.id.action_insert -> {
                val Course = findViewById<TextInputEditText>(R.id.ed_course_name).text.toString()
                val spDay = findViewById<Spinner>(R.id.spinner_day).selectedItem.toString()
                val spDayNumber = getDay(spDay)
                val Lecturer = findViewById<TextInputEditText>(R.id.ed_lecturer).text.toString()
                val Note = findViewById<TextInputEditText>(R.id.ed_note).text.toString()

                when {
                    Course.isEmpty() -> false
                    mulai.isEmpty() -> false
                    akhir.isEmpty() -> false
                    spDayNumber == -1 -> false
                    Lecturer.isEmpty() -> false
                    Note.isEmpty() -> false
                    else -> {
                        vM.insertCourse(
                            Course, spDayNumber, mulai, akhir, Lecturer,
                            Note
                        )
                        finish()
                        true
                    }
                }
            }
            else -> super.onOptionsItemSelected(item)
        }
    }



    private fun getDay(spDay: String): Int {
        val hari = resources.getStringArray(R.array.day)
        return hari.indexOf(spDay)
    }

    fun TimePck (view: View){
        val k = when(view.id){
            R.id.ib_start_time -> "start"
            R.id.ib_end_time -> "end"
            else -> {"default"}
        }

        val frg = TimePickerFragment()
        frg.show(supportFragmentManager, k)
    }

    override fun onDialogTimeSet(tag: String?, hour: Int, minute: Int) {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
        }
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        updateTextViewAndTime(tag, R.id.tv_start_time, timeFormat, calendar)
        updateTextViewAndTime(tag, R.id.tv_end_time, timeFormat, calendar)
    }

    private fun updateTextViewAndTime(tag: String?, textViewId: Int, timeFormat: SimpleDateFormat, calendar: Calendar) {
        val textView = findViewById<TextView>(textViewId)

        when (tag) {
            "start" -> {
                textView.text = timeFormat.format(calendar.time)
                mulai = timeFormat.format(calendar.time)
            }
            "end" -> {
                textView.text = timeFormat.format(calendar.time)
                akhir = timeFormat.format(calendar.time)
            }
        }
    }

}