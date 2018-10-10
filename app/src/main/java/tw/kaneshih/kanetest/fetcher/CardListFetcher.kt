package tw.kaneshih.kanetest.fetcher

import tw.kaneshih.kanetest.model.Card
import tw.kaneshih.kanetest.model.toCardList
import tw.kaneshih.kanetest.repo.DataSource

class CardListFetcher(offset: Int, count: Int)
    : PageFetcher<List<Card>>(offset, count) {
    override fun fetch(): List<Card> {
        val jResult = DataSource.getCardList(offset, count)
        return jResult.getJSONArray("result").toCardList()
    }
}