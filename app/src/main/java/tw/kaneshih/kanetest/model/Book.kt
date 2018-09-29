package tw.kaneshih.kanetest.model

import org.json.JSONArray
import org.json.JSONObject

data class Book(
        val id: String,
        val title: String,
        val thumbnail: String,
        val description: String,
        val url: String,
        val authorId: String,
        val authorName: String,
        val publishYear: Int
)

fun Book.toJSONObject(): JSONObject {
    val json = JSONObject()
    json.put("id", id)
    json.put("title", title)
    json.put("thumbnail", thumbnail)
    json.put("description", description)
    json.put("url", url)
    json.put("author_id", authorId)
    json.put("author_name", authorName)
    json.put("publish_year", publishYear)
    return json
}

fun JSONObject.toBook(): Book {
    return Book(id = this.getString("id"),
            title = this.getString("title"),
            thumbnail = this.getString("thumbnail"),
            description = this.getString("description"),
            url = this.getString("url"),
            authorId = this.getString("author_id"),
            authorName = this.getString("author_name"),
            publishYear = this.getInt("publish_year"))
}

fun JSONArray.toBookList(): List<Book> {
    val list = mutableListOf<Book>()
    for (i in 0 until length()) {
        list.add(getJSONObject(i).toBook())
    }
    return list
}