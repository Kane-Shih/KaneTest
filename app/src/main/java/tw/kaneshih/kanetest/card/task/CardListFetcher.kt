package tw.kaneshih.kanetest.card.task

import tw.kaneshih.base.task.CallbackTask
import tw.kaneshih.base.task.Result
import tw.kaneshih.kanetest.card.model.Card
import tw.kaneshih.kanetest.card.model.toCardList
import tw.kaneshih.kanetest.card.repo.DataSource
import tw.kaneshih.kanetest.card.ui.viewholder.ItemViewModel

class CardListFetcher(private val offset: Int,
                      private val count: Int,
                      private val transformer: (card: Card) -> ItemViewModel,
                      callback: (Result<List<ItemViewModel>>) -> Unit)
    : CallbackTask<List<ItemViewModel>>("CardListFetcher[$offset,$count]", callback) {

    override fun doInBackground(): Result<List<ItemViewModel>> {
        val jResult = DataSource.getCardList(offset, count)
        val cardList = jResult.getJSONArray("result").toCardList()

        val list = mutableListOf<ItemViewModel>()
        cardList.forEach {
            list.add(transformer(it))
        }

        return resultSuccess(list)
    }
}