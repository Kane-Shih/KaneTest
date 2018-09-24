package tw.kaneshih.kanetest.card.repo

import android.os.SystemClock
import org.json.JSONArray
import org.json.JSONObject
import tw.kaneshih.kanetest.card.model.Card
import tw.kaneshih.kanetest.card.model.CardType
import tw.kaneshih.kanetest.card.model.toJSONObject
import java.util.Random

object DataSource { // simulate
    private const val TOTAL_ITEM = 55
    private const val SLEEP_TIME_MS = 1000L

    private val thumbnailList = listOf(
            "https://www.wellnesspetfood.com/sites/default/files/styles/blog_feature/public/media/images/GettyImages-156530690_super.jpg",
            "https://images.pexels.com/photos/104827/cat-pet-animal-domestic-104827.jpeg?auto=compress&cs=tinysrgb&h=350",
            "https://www.freegreatpicture.com/files/157/1562-cute-little-cat.jpg",
            "https://img.buzzfeed.com/buzzfeed-static/static/2014-03/enhanced/webdr02/26/18/original-5460-1395871268-26.jpg?downsize=715:*&output-format=auto&output-quality=auto")

    private val urlList = listOf(
            "https://www.google.com",
            "https://www.youtube.com",
            "https://www.yahoo.com",
            "https://github.com")

    fun getCardList(offset: Int, count: Int): JSONObject {
        SystemClock.sleep(SLEEP_TIME_MS)

        val random = Random()

        val jArray = JSONArray()
        for (i in offset..(offset + count - 1)) {
            if (i >= TOTAL_ITEM) {
                break
            }

            jArray.put(Card(
                    id = "card\$$i",
                    type = if (i % 3 == 0) CardType.LARGE else CardType.MEDIUM,
                    name = "Card [${i + 1}]",
                    description = "Desc for [${i + 1}]".repeat(20),
                    thumbnail = thumbnailList[i % thumbnailList.size],
                    url = urlList[i % urlList.size],
                    subItemCount = random.nextInt(101)
            ).toJSONObject())
        }

        val jResult = JSONObject()
        jResult.put("result", jArray)
        return jResult
    }
}