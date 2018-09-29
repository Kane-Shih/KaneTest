package tw.kaneshih.kanetest.ui

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_list.*
import tw.kaneshih.kanetest.R

class ListActivity : AppCompatActivity(), ListFragment.Host {

    companion object {

        const val EXTRA_ARGS = "extra_args"

        fun getIntentForCards(context: Context) =
                Intent(context, ListActivity::class.java).apply {
                    putExtra(EXTRA_ARGS, ListFragment.createArgsForCards())
                }

        fun getIntentForBooks(context: Context) =
                Intent(context, ListActivity::class.java).apply {
                    putExtra(EXTRA_ARGS, ListFragment.createArgsForBooks())
                }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .add(R.id.container,
                            ListFragment.getInstance(intent.getBundleExtra(EXTRA_ARGS)))
                    .commit()
        }
    }

    override fun onUpdateTitle(title: String) {
        appTitle.text = title
    }
}
