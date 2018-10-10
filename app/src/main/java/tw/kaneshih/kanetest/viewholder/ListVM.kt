package tw.kaneshih.kanetest.viewholder

import tw.kaneshih.base.viewholder.BasicVM

class ListVM<out E : BasicVM>(val list: List<E>) : BasicVM()