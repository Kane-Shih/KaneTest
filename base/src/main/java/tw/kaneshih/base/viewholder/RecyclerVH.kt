package tw.kaneshih.base.viewholder

import android.support.v7.widget.RecyclerView

class RecyclerVH<T : ViewModel>(val viewHolder: ViewHolder<T>)
    : RecyclerView.ViewHolder(viewHolder.getView())