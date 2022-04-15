package com.habitrpg.android.habitica.ui.adapter.tasks

import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import com.habitrpg.android.habitica.helpers.TaskFilterHelper
import com.habitrpg.android.habitica.ui.viewHolders.tasks.TodoCardViewHolder

class TodosRecyclerViewAdapter(layoutResource: Int, taskFilterHelper: TaskFilterHelper) :
    RealmBaseTasksRecyclerViewAdapter<TodoCardViewHolder>(layoutResource, taskFilterHelper) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoCardViewHolder =
        TodoCardViewHolder(
            ComposeView(parent.context),
            { task, direction -> taskScoreEventsSubject.onNext(Pair(task, direction)) },
            { task, item -> checklistItemScoreSubject.onNext(Pair(task, item)) },
            { task -> taskOpenEventsSubject.onNext(task) }
        ) { task ->
            brokenTaskEventsSubject.onNext(task)
        }

    override fun onViewRecycled(holder: TodoCardViewHolder) {
        // Dispose the underlying Composition of the ComposeView
        // when RecyclerView has recycled this ViewHolder
        holder.composeView.disposeComposition()
    }
}
