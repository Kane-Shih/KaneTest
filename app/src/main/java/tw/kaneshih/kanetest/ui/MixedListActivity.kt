package tw.kaneshih.kanetest.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import tw.kaneshih.kanetest.R

class MixedListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mixed_list)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .add(R.id.container, MixedListFragment.getInstance())
                    .commit()
        }
    }
}
