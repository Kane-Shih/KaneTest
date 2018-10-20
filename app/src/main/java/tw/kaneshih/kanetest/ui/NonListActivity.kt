package tw.kaneshih.kanetest.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.toast
import kotlinx.android.synthetic.main.activity_non_list.*
import tw.kaneshih.base.viewholder.BasicVM
import tw.kaneshih.kanetest.R
import tw.kaneshih.kanetest.fetcher.CardListFetcher
import tw.kaneshih.kanetest.model.Card
import tw.kaneshih.base.task.FetcherTask
import tw.kaneshih.kanetest.task.resolveError
import tw.kaneshih.kanetest.viewholder.LargeItemVH
import tw.kaneshih.kanetest.viewholder.LargeItemVM
import tw.kaneshih.kanetest.viewholder.MediumItemVH
import tw.kaneshih.kanetest.viewholder.MediumItemVM
import tw.kaneshih.kanetest.viewholder.SmallItemVH
import tw.kaneshih.kanetest.viewholder.SmallItemVM
import tw.kaneshih.kanetest.viewholder.toLargeItemVM
import tw.kaneshih.kanetest.viewholder.toMediumItemVM
import tw.kaneshih.kanetest.viewholder.toSmallItemVM

class NonListActivity : AppCompatActivity() {
    private lateinit var largeItemVH: LargeItemVH
    private lateinit var mediumItemVH: MediumItemVH
    private lateinit var smallItemVH: SmallItemVH

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_non_list)

        largeItemVH = LargeItemVH(largeItemView,
                { _, vm -> toast("large item clicked: ${vm.cardName}") },
                { _, vm -> toast("large thumbnail clicked: ${vm.cardName}") })
        mediumItemVH = MediumItemVH(mediumItemView,
                { _, vm -> toast("medium item clicked: ${vm.cardName}") },
                { _, vm -> toast("medium thumbnail clicked: ${vm.cardName}") })
        smallItemVH = SmallItemVH(smallItemView) { _, vm -> toast("small item clicked: ${vm.cardName}") }

        FetcherTask(CardListFetcher(0, 3))
        { index, data ->
            when (index) {
                0 -> data.toLargeItemVM()
                1 -> data.toMediumItemVM(this)
                else -> data.toSmallItemVM()
            }
        }.callback {
            loadingView.isVisible = false
            if (!it.isSuccess) {
                toast("Error: ${it.resolveError()} - ${it.exception}")
                return@callback
            }
            it.data!!.forEachIndexed { index, basicVM ->
                when (index) {
                    0 -> largeItemVH.apply {
                        bind(basicVM as LargeItemVM)
                    }
                    1 -> mediumItemVH.apply {
                        bind(basicVM as MediumItemVM)
                    }
                    else -> smallItemVH.apply {
                        bind(basicVM as SmallItemVM)
                    }
                }.getView().isVisible = true
            }
        }.execute()
    }

    private val BasicVM.cardName: String
        get() = (userData as Card).name
}