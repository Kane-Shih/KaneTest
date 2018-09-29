package tw.kaneshih.kanetest.task

import tw.kaneshih.base.task.CallbackTask
import tw.kaneshih.base.task.Result
import tw.kaneshih.kanetest.model.Card
import tw.kaneshih.kanetest.model.toCardList
import tw.kaneshih.kanetest.repo.DataSource
import tw.kaneshih.kanetest.viewholder.ItemViewModel

class CardListFetcher(private val offset: Int,
                      private val count: Int,
                      private val transformer: (index: Int, card: Card) -> ItemViewModel,
                      callback: (Result<List<ItemViewModel>>) -> Unit)
    : CallbackTask<List<ItemViewModel>>("CardListFetcher[$offset,$count]", callback) {

    override fun doInBackground(): Result<List<ItemViewModel>> {
        val jResult = DataSource.getCardList(offset, count)
        val cardList = jResult.getJSONArray("result").toCardList()
        return resultSuccess(
                cardList.mapIndexed { index, card -> transformer(offset + index, card) })
    }
}