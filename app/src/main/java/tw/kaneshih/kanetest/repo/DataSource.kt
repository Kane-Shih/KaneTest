package tw.kaneshih.kanetest.repo

import android.os.Looper
import android.os.NetworkOnMainThreadException
import android.os.SystemClock
import org.json.JSONArray
import org.json.JSONObject
import tw.kaneshih.kanetest.model.Book
import tw.kaneshih.kanetest.model.Card
import tw.kaneshih.kanetest.model.CardType
import tw.kaneshih.kanetest.model.toJSONObject
import java.util.Random

object DataSource { // simulate
    private const val TOTAL_CARDS_ITEM = 55
    private const val TOTAL_BOOKS_ITEM = 32

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
        requireBackgroundThread()
        SystemClock.sleep(SLEEP_TIME_MS)

        val random = Random()

        val jArray = JSONArray()
        for (i in offset..(offset + count - 1)) {
            if (i >= TOTAL_CARDS_ITEM) {
                break
            }

            jArray.put(Card(
                    id = "card_id_$i",
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

    fun getBookList(offset: Int, count: Int): JSONObject {
        requireBackgroundThread()
        SystemClock.sleep(SLEEP_TIME_MS)

        val jArray = JSONArray()
        for (i in offset..(offset + count - 1)) {
            if (i >= TOTAL_BOOKS_ITEM) {
                break
            }

            jArray.put(Book(
                    id = "book_id_$i",
                    title = "Book [${i + 1}]",
                    description = "This book [${i + 1}] is not a food.".repeat(20),
                    thumbnail = thumbnailList[i % thumbnailList.size],
                    url = urlList[i % urlList.size],
                    authorId = "author_id_$i",
                    authorName = "Author$i",
                    publishYear = 1900 + i
            ).toJSONObject())
        }

        val jResult = JSONObject()
        jResult.put("result", jArray)
        return jResult
    }

    private fun requireBackgroundThread() {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw NetworkOnMainThreadException()
        }
    }
}