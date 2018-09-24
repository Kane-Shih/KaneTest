package tw.kaneshih.kanetest.card.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import tw.kaneshih.kanetest.R

class CardListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_list)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .add(R.id.container, CardListFragment.getInstance())
                    .commit()
        }
    }
}
