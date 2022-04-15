package com.habitrpg.android.habitica.ui.viewHolders.tasks

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.dp
import com.habitrpg.android.habitica.compose.ui.component.TodoCard
import com.habitrpg.android.habitica.compose.ui.theme.HabiticaTheme
import com.habitrpg.android.habitica.models.responses.TaskDirection
import com.habitrpg.android.habitica.models.tasks.ChecklistItem
import com.habitrpg.android.habitica.models.tasks.Task
import io.reactivex.rxjava3.functions.Action

class TodoCardViewHolder(
    val composeView: ComposeView,
    scoreTaskFunc: ((Task, TaskDirection) -> Unit),
    scoreChecklistItemFunc: ((Task, ChecklistItem) -> Unit),
    openTaskFunc: ((Task) -> Unit),
    brokenTaskFunc: ((Task) -> Unit)
) : InterimViewHolder(
    composeView
) {
    init {
        composeView.setViewCompositionStrategy(
            ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
        )
    }

    override var errorButtonClicked: Action? = null
    override var isLocked: Boolean = false

    override fun bind(data: Task, position: Int, displayMode: String) {
        composeView.setContent {
            HabiticaTheme {
                TodoCard(
                    modifier = Modifier.padding(8.dp),
                    taskItem = data.toTaskItem()
                )
            }
        }
    }

}