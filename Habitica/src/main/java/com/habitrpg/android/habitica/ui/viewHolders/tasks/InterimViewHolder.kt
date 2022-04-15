package com.habitrpg.android.habitica.ui.viewHolders.tasks

import android.view.View
import com.habitrpg.android.habitica.models.tasks.Task
import com.habitrpg.android.habitica.ui.viewHolders.BindableViewHolder
import io.reactivex.rxjava3.functions.Action

abstract class InterimViewHolder constructor(itemView: View): BindableViewHolder<Task>(itemView) {
    abstract var errorButtonClicked: Action?
    abstract var isLocked: Boolean
}