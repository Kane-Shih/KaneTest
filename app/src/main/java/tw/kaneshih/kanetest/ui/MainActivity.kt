package tw.kaneshih.kanetest.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.setPadding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL

            layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT)

            addView(TextView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT)
                text = "Click for Cards"
                textSize = 48f
                setPadding(32)
                setOnClickListener {
                    startActivity(ListActivity.getIntentForCards(context))
                }
            })

            addView(TextView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT)
                text = "Click for Books"
                textSize = 48f
                setPadding(32)
                setOnClickListener {
                    startActivity(ListActivity.getIntentForBooks(context))
                }
            })
        })
    }
}