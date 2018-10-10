package tw.kaneshih.kanetest.ui

import android.content.Intent
import android.graphics.Paint
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
                text = "Card List"
                onClick { startActivity(ListActivity.getIntentForCards(context)) }
            }

            addItem {
                text = "Book List"
                onClick { startActivity(ListActivity.getIntentForBooks(context)) }
            }

            addItem {
                text = "Mixed List"
                onClick { startActivity(ListActivity.getIntentForMixed(context)) }
            }

            addItem {
                text = "Non-list"
                onClick { startActivity(Intent(this@MainActivity, NonListActivity::class.java)) }
            }
        })
    }

    private inline fun ViewGroup.addItem(init: TextView.() -> Unit) {
        addView(TextView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT)
            textSize = 32f
            paintFlags = paintFlags or Paint.UNDERLINE_TEXT_FLAG
            setPadding(32)
            setSelectableItemBackground()

            init()
        })
    }

    fun View.onClick(action: View.() -> Unit) = setOnClickListener(action)

}