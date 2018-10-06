package tw.kaneshih.base.viewholder

import tw.kaneshih.base.log.logWarning

const val USERDATA_KEY_DATA = "tw.kaneshih.base.viewholder.ItemViewModel_data"

abstract class ItemViewModel : ViewModel {
    override val userData: MutableMap<String, Any?> = mutableMapOf()

    var data: Any?
        set(value) {
            userData[USERDATA_KEY_DATA] = value
        }
        get() {
            val data = userData[USERDATA_KEY_DATA]
            if (data == null) {
                "Did not put in data before at ${this::class.java.simpleName}!".logWarning()
            }
            return data
        }
}