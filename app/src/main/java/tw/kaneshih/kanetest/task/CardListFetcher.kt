package tw.kaneshih.kanetest.task

import tw.kaneshih.base.task.Result
import tw.kaneshih.kanetest.model.Card
import tw.kaneshih.kanetest.model.toCardList
import tw.kaneshih.kanetest.repo.DataSource
import tw.kaneshih.base.viewholder.BasicVM

class CardListFetcher(private val offset: Int,
                      private val count: Int,
                      itemTransformer: (index: Int, card: Card) -> BasicVM,
                      listTransformer: ((list: List<BasicVM>) -> List<BasicVM>)?,
                      callback: (Result<List<BasicVM>>) -> Unit)
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