package com.habitrpg.android.habitica.compose.ui.component

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Checkbox
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.habitrpg.android.habitica.compose.ui.theme.HabiticaTheme
import com.habitrpg.android.habitica.compose.ui.theme.blue_50
import com.habitrpg.android.habitica.compose.ui.theme.blue_500
import com.habitrpg.android.habitica.compose.ui.theme.gray_100
import com.habitrpg.android.habitica.compose.ui.theme.gray_200
import com.habitrpg.android.habitica.compose.ui.theme.green_50
import com.habitrpg.android.habitica.compose.ui.theme.green_500
import com.habitrpg.android.habitica.compose.ui.theme.maroon_50
import com.habitrpg.android.habitica.compose.ui.theme.maroon_500
import com.habitrpg.android.habitica.compose.ui.theme.orange_50
import com.habitrpg.android.habitica.compose.ui.theme.orange_500
import com.habitrpg.android.habitica.compose.ui.theme.red_50
import com.habitrpg.android.habitica.compose.ui.theme.red_500
import com.habitrpg.android.habitica.compose.ui.theme.teal_50
import com.habitrpg.android.habitica.compose.ui.theme.teal_500
import com.habitrpg.android.habitica.compose.ui.theme.yellow_10
import com.habitrpg.android.habitica.compose.ui.theme.yellow_100

@Composable
fun TodoCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    taskItem: TaskItem
) {

    val shape = RoundedCornerShape(8.dp)
    Box(
        modifier = modifier
            .heightIn(min = 60.dp)
            .wrapContentHeight()
            .fillMaxWidth()
            .clip(shape)
            .background(HabiticaTheme.colors.windowBackground)
    ) {
        var checkListExpanded by remember { mutableStateOf(false) }
        TodoBody(
            modifier = Modifier.animateContentSize(animationSpec = tween(durationMillis = 3000)),
            checkListExpanded = checkListExpanded,
            taskItem = taskItem,
            onClick = { checkListExpanded = !checkListExpanded })

    }
}

//Modifier.animateContentSize(animationSpec = tween(durationMillis = 3000))
@Composable
fun TodoBody(
    modifier: Modifier = Modifier,
    checkListExpanded: Boolean,
    taskItem: TaskItem,
    onClick: () -> Unit
) {
    Column(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            TaskCheckBox(isChecked = taskItem.isChecked, value = taskItem.value)
            TaskMainContent(modifier = Modifier.weight(1f), taskItem)
            if (taskItem.hasChecklist) {
                val checked = taskItem.checkList.filter { it.completed }.count()
                ChecklistIndicator(
                    checked = checked,
                    total = taskItem.checkList.size,
                    onClick = onClick,
                )
            }
        }
        if (checkListExpanded) {
            CheckList(taskItem.checkList, value = taskItem.value)
        }
    }
}

@Composable
fun TaskCheckBox(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    isChecked: Boolean = false,
    value: Double = 1.0
) {
    Box(
        modifier = modifier
            .width(48.dp)
            .heightIn(min = 60.dp)
            .fillMaxHeight()
            .background(taskColorFromValue(value))
    ) {
        Checkbox(checked = isChecked, onCheckedChange = { onClick() })
    }

}

fun taskColorFromValue(value: Double): Color {
    return when {
        value < -20 -> maroon_50
        value < -10 -> red_50
        value < -1 -> orange_50
        value < 1 -> yellow_10
        value < 5 -> green_50
        value < 10 -> teal_50
        else -> blue_50
    }
}

fun taskColorLightFromValue(value: Double): Color {
    return when {
        value < -20 -> maroon_500
        value < -10 -> red_500
        value < -1 -> orange_500
        value < 1 -> yellow_100
        value < 5 -> green_500
        value < 10 -> teal_500
        else -> blue_500
    }
}


@Composable
fun TaskMainContent(modifier: Modifier = Modifier, taskItem: TaskItem) {
    Column(modifier = modifier.padding(8.dp)) {
        Text(text = taskItem.title, style = MaterialTheme.typography.h6)
        Text(text = taskItem.notes, style = MaterialTheme.typography.overline)
//        if(taskItem.hasReadMore) {
//            TextButton(onClick = { /*TODO*/ }) {
//                Text("READ MORE")
//            }
//        }
        // if (taskItem.isPending) { Text("pending approval") }
        // Text("special")
    }
}

@Composable
fun ChecklistIndicator(
    modifier: Modifier = Modifier,
    checked: Int,
    total: Int,
    onClick: () -> Unit = {}
) {
    val shape = RoundedCornerShape(4.dp)
    Box(
        modifier = modifier
            .wrapContentWidth()
            .clip(shape)
            .background(gray_200)
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .clickable(onClick = onClick)
        ) {
            Text(
                text = "$checked",
                style = MaterialTheme.typography.overline,
                color = Color.White
            )
            Text(
                text = "-",
                style = MaterialTheme.typography.overline,
                color = Color.White
            )
            Text(
                text = "$total",
                style = MaterialTheme.typography.overline,
                color = Color.White
            )
        }
    }
}

@Composable
fun CheckList(
    checkList: List<CheckListItem>,
    onItemClick: (id: String) -> Unit = {},
    value: Double = 0.0
) {

    Column {
        for (item in checkList) {
            Row {
                Box(
                    modifier = Modifier
                        .wrapContentHeight()
                        .width(48.dp)
                        .background(taskColorLightFromValue(value))
                ) {
                    Checkbox(checked = item.completed, onCheckedChange = { onItemClick(item.id) })
                }
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = item.text,
                    style = MaterialTheme.typography.caption
                )
            }
        }
    }
}

@Preview
@Composable
private fun DefaultPreview() {
    HabiticaTheme {
        TodoCard(
            taskItem = TaskItem(
                checkList = listOf(
                    CheckListItem(completed = true, text = "one", position = 0),
                    CheckListItem(completed = false, text = "two", position = 1),
                    CheckListItem(completed = false, text = "three", position = 2),
                )
            )
        )
    }

}

data class TaskItem(
    val isChecked: Boolean = false,
    val isSynchronising: Boolean = true,
    val value: Double = 5.0,
    val title: String = "Habit Title",
    val notes: String = "random notes",
    val hasReadMore: Boolean = true,
    val isPending: Boolean = false,
    val checkList: List<CheckListItem> = emptyList()
) {
    val hasChecklist: Boolean
        get() = checkList.isNotEmpty()
}

data class CheckListItem(
    val id: String = "",
    val completed: Boolean = false,
    val text: String = "",
    val position: Int
)