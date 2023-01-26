package com.example.windowlearn

import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.DialogFragment

class MainActivity : AppCompatActivity() {

    val TAG = "MainActivity"
    lateinit var mViewInActivity: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        WindowFragment(this).show(supportFragmentManager, "window")

        mViewInActivity = findViewById<View>(R.id.activity_view)
    }

    fun adjustActivityView(dy: Int) {
        mViewInActivity.layoutParams.height += dy
        Log.e("MyDialog", "activityView: newHeight = ${mViewInActivity.layoutParams.height}")

        mViewInActivity.layoutParams = mViewInActivity.layoutParams
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        Log.e(TAG, "dispatchTouchEvent: ${ev?.y}")
        return super.dispatchTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Log.e(TAG, "onTouchEvent: " + event?.y)
        return super.onTouchEvent(event)
    }


    class WindowFragment(private val myActivity: MainActivity) : DialogFragment() {

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            return MyDialog(myActivity)
        }
    }

    class MyDialog(private val myActivity: MainActivity) : Dialog(myActivity) {

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.layout_dialog_content)
            configWindow(this)
        }

        private fun configWindow(dialog: Dialog) {
            dialog.window?.apply {
                setBackgroundDrawable(ColorDrawable(R.color.purple_700))
                attributes.height =
                    (this@MyDialog.context.resources.displayMetrics.density * 500).toInt()
                attributes.width = 500
                attributes.gravity = Gravity.BOTTOM
                attributes = attributes
            }
        }

        private var mLastY = 0
        override fun onTouchEvent(event: MotionEvent): Boolean {
            if (event.actionMasked == MotionEvent.ACTION_DOWN) {
                mLastY = event.y.toInt()
            } else {
                val dy = mLastY - event.y.toInt()
                Log.e("MyDialog", "onTouchEvent: dy = $dy")
                adjustDialogWindow(dy)
                adjustActivityView(dy)
                mLastY = event.y.toInt()
                return true
            }


            return super.onTouchEvent(event)
        }

        private fun adjustDialogWindow(dy: Int) {
            window?.apply {
                attributes.height += dy
                Log.e("MyDialog", "dialogWindow: newHeight = ${attributes.height}")
                attributes = attributes
            }
        }

        private fun adjustActivityView(dy: Int) {
            myActivity.adjustActivityView(dy)
        }

    }
}