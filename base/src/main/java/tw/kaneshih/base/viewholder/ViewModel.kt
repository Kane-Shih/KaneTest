package tw.kaneshih.base.viewholder

import android.os.Bundle

interface ViewModel {
    val extra: Bundle
}

abstract class ItemViewModel : ViewModel {
    override val extra by lazy { Bundle() }

    var userData: Any? = null
}