package com.abelsuviri.viewmodel.ui

import android.app.Dialog
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.abelsuviri.viewmodel.R
import com.abelsuviri.viewmodel.PREFERENCE_TIMEOUT
import kotlinx.android.synthetic.main.settings_dialog.*
import org.koin.android.ext.android.inject

/**
 * @author Abel Suviri
 */

class MainActivity : AppCompatActivity() {

    private val preferences: SharedPreferences by inject()

    private lateinit var settingsDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        createDialog()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.menu_settings -> {
                settingsDialog.show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    //Dialog used to change the pins timeout
    private fun createDialog() {
        settingsDialog = Dialog(this)
        settingsDialog.setTitle(R.string.settings_menu)
        settingsDialog.setContentView(R.layout.settings_dialog)
        settingsDialog.positiveButton.setOnClickListener {
            val editor = preferences.edit()
            editor.putLong(PREFERENCE_TIMEOUT, settingsDialog.timeoutEditText.text.toString().toLong() * 1000)
            editor.apply()
            settingsDialog.dismiss()
        }
    }
}
