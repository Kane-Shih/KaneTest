package tw.kaneshih.kanetest.model

import org.json.JSONArray
import org.json.JSONObject

data class Card(
        val id: String,
        val type: CardType,
        val name: String,
        val url: String,
        val thumbnail: String,
        val description: String,
        val subItemCount: Int
)

enum class CardType {
    MEDIUM, LARGE
}

fun Card.toJSONObject(): JSONObject {
    val json = JSONObject()
    json.put("id", id)
    json.put("type", type.name.toLowerCase())
    json.put("name", name)
    json.put("url", url)
    json.put("thumbnail", thumbnail)
    json.put("description", description)
    json.put("subItemCount", subItemCount)
    return json
}

fun JSONObject.toCard(): Card {
    return Card(id = this.getString("id"),
            type = CardType.valueOf(this.getString("type").toUpperCase()),
            name = this.getString("name"),
            url = this.getString("url"),
            thumbnail = this.getString("thumbnail"),
            description = this.getString("description"),
            subItemCount = this.getInt("subItemCount"))
}

fun JSONArray.toCardList(): List<Card> {
    val list = mutableListOf<Card>()
    for (i in 0 until length()) {
        list.add(getJSONObject(i).toCard())
    }
    return list
}