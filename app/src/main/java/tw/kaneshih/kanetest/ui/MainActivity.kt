package tw.kaneshih.kanetest.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.setPadding
import tw.kaneshih.base.setSelectableItemBackground

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL

            layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT)

            addItem {
                text = "Click for Cards"
                onClick { startActivity(ListActivity.getIntentForCards(context)) }
            }

            addItem {
                text = "Click for Books"
                onClick { startActivity(ListActivity.getIntentForBooks(context)) }
            }
        })
    }

    private inline fun ViewGroup.addItem(init: TextView.() -> Unit) {
        addView(TextView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT)
            textSize = 48f
            setPadding(32)
            setSelectableItemBackground()

            init()
        })
    }

    fun View.onClick(action: View.() -> Unit) = setOnClickListener(action)

}