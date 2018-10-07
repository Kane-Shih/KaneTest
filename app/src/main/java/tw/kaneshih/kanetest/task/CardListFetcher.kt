package tw.kaneshih.kanetest.task

import tw.kaneshih.base.task.Result
import tw.kaneshih.kanetest.model.Card
import tw.kaneshih.kanetest.model.toCardList
import tw.kaneshih.kanetest.repo.DataSource
import tw.kaneshih.base.viewholder.ItemViewModel

class CardListFetcher(private val offset: Int,
                      private val count: Int,
                      itemTransformer: (index: Int, card: Card) -> ItemViewModel,
                      listTransformer: ((list: List<ItemViewModel>) -> List<ItemViewModel>)?,
                      callback: (Result<List<ItemViewModel>>) -> Unit)
    : TransformCallbackTask<Card>("CardListFetcher[$offset,$count]", callback,
        itemTransformer, listTransformer) {

    override fun getRawDataList(): List<Card> {
        val jResult = DataSource.getCardList(offset, count)
        return jResult.getJSONArray("result").toCardList()
    }

    override fun convertIndex(index: Int): Int {
        return offset + index
    }
}