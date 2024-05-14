package com.example.codsofttodo

import android.content.Context
import android.content.Intent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties


@Composable
fun CustomDialog(
    showDialog: Boolean,
    onDismissRequest: () -> Unit,
    content: @Composable () -> Unit,
) {
    if (showDialog) {
        Dialog(
            onDismissRequest = onDismissRequest,
            properties = DialogProperties(
                usePlatformDefaultWidth = false
            )
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    Modifier
                        .pointerInput(Unit) { detectTapGestures { } }
                        .shadow(8.dp, shape = RoundedCornerShape(16.dp))
                        .width(300.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            MaterialTheme.colorScheme.surface,
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    content()
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    title: String = "Select Time",
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
    content: @Composable (DisplayMode) -> Unit,
) {
    val displayModeState = remember { mutableStateOf(DisplayMode.Input) }

    Dialog(
        onDismissRequest = onCancel,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            tonalElevation = 6.dp,
            modifier = Modifier
                .width(IntrinsicSize.Min)
                .height(IntrinsicSize.Min)
                .background(
                    shape = MaterialTheme.shapes.extraLarge,
                    color = MaterialTheme.colorScheme.surface
                )
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    text = title,
                    style = MaterialTheme.typography.labelMedium
                )
                content(displayModeState.value)
                Row(
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth()
                ) {
                    DisplayModeToggleButton(displayModeState = displayModeState)

                    Spacer(modifier = Modifier.weight(1F))
                    TextButton(onClick = onCancel) {
                        Text("Cancel")
                    }
                    TextButton(onClick = onConfirm) {
                        Text("OK")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DisplayModeToggleButton(
    displayModeState: MutableState<DisplayMode>,
    modifier: Modifier = Modifier,
) {
    when (displayModeState.value) {
        DisplayMode.Picker -> IconButton(
            modifier = modifier,
            onClick = { displayModeState.value = DisplayMode.Input },
        ) {
            Icon(
                painter = painterResource(R.drawable.clock_outline),
                contentDescription = "time_picker_button_select_input_mode"
            )
        }
        DisplayMode.Input -> IconButton(
            modifier = modifier,
            onClick = { displayModeState.value = DisplayMode.Picker },
        ) {
            Icon(
                painter = painterResource(R.drawable.keyboard),
                contentDescription = "time_picker_button_select_picker_mode"
            )
        }
    }
}

@Composable
fun NewListDialogue(
    showDialogue: Boolean,
    onDismiss: ()-> Unit,
    value: String,
    onValueChange: (String) -> Unit,
    onAdd: () -> Unit,
    onCancel: () -> Unit
) {
    CustomDialog(
        showDialog = showDialogue,
        onDismissRequest = onDismiss) {
        Box(modifier = Modifier.background(Color.White)
            .fillMaxWidth().height(200.dp)
            .padding(20.dp)) {
            Text(text = "New List", modifier = Modifier.align(Alignment.TopStart), color = Color.Blue)
            TextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                ),
                textStyle = TextStyle(color = Color.Black),
                keyboardActions = KeyboardActions(onDone = {onAdd()}),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                label = { Text(text = "Enter new list") })
            Row(modifier = Modifier.align(Alignment.BottomEnd),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalAlignment = Alignment.CenterVertically) {
                Text(text = "CANCEL", modifier = Modifier.clickable { onCancel()}, color = Color.Red)
                Text(text = "ADD", modifier = Modifier.clickable(onClick = {onAdd()}), color = Color.Green)
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDropDownMenu(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    selectedList: String,
    onDismissedRequest: ()->Unit,
    options: MutableList<String>,
    addLeadingIcon: Boolean = true,
    topComposable: @Composable () -> Unit = {},
    bottomComposable: @Composable () -> Unit = {},
    attachedCompose: @Composable () -> Unit = {
        Text(
            text = selectedList,
            modifier = Modifier
                .fillMaxWidth()
        )
    },
    textColor: Color = MaterialTheme.colorScheme.onSecondary,
    onItemClicked: (String)-> Unit
) {
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = onExpandedChange,
        modifier = modifier
    ) {
        Box(modifier = Modifier.menuAnchor()) {
            attachedCompose()
        }
        ExposedDropdownMenu(expanded = expanded,
            onDismissRequest = onDismissedRequest,
            modifier = Modifier
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .width(200.dp)) {
            topComposable()
            options.toSet().forEach { option: String ->
                DropdownMenuItem(
                    text = { Row(horizontalArrangement = Arrangement.spacedBy(5.dp),
                        verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                            .fillMaxSize()
                            .background(color = if (selectedList == option) MaterialTheme.colorScheme.secondary else Color.Transparent)) {
                        if (addLeadingIcon) Icon(painter = painterResource(id = R.drawable.menu), contentDescription =null )
                        Text(text = option, fontWeight = FontWeight.Medium)
                    } },
                    onClick = {
                        onItemClicked(option)
                        onDismissedRequest()
                    },
                    colors =
                    if (selectedList == option)
                        MenuDefaults.itemColors(textColor = textColor)
                    else MenuDefaults.itemColors(MaterialTheme.colorScheme.secondary)
                )
                HorizontalDivider(modifier = Modifier.fillMaxWidth())
            }
            bottomComposable()
        }
    }
}
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TodoItem(
    modifier: Modifier = Modifier,
    title: String,
    timeLine: String,
    listType: String,
    checked: Boolean,
    onDelete: () -> Unit,
    onEdit: () -> Unit,
    hasRepeat: Boolean = false,
    showListType: Boolean = false,
    showtime: Boolean = false,
    isChecked: (Boolean) -> Unit,
    isEdge: Int = 2,
    onLongPress: () -> Unit = {},
    onTap: ()-> Unit = {},
    timeLineColor: Color = Color.Green,
) {
    var expanded by rememberSaveable {mutableStateOf(false) }
    //this state variable is to remember
    // the state change and recompose all composable that they affected by the state variable.
    val color by animateColorAsState( // the animateColorAsState() function that access different
        //color based on another state variable (expanded)
        targetValue = if (expanded) MaterialTheme.colorScheme.surfaceContainerHighest
        else MaterialTheme.colorScheme.surfaceContainerHigh,
        label = "a"
    )
    Card(modifier = modifier.combinedClickable(
        onClick = {
            onTap()
            expanded = !expanded},
        onLongClick = { onLongPress() },
        onLongClickLabel = null),
        shape = when (isEdge) {
            1 -> MaterialTheme.shapes.medium
            3 -> MaterialTheme.shapes.large
            4 -> MaterialTheme.shapes.extraSmall
            else -> MaterialTheme.shapes.small
        }
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .background(color = color)
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessVeryLow
                )
            )) {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically){
                    Checkbox(checked = checked, onCheckedChange = {isChecked(it)})
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically){
                    IconButton(onClick = onEdit) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = null)

                    }

                    IconButton(onClick = onDelete) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = null)

                    }
                }
            }
            Column(
                modifier = modifier.padding(start = 50.dp, bottom = 5.dp, end = 50.dp)
            ) {
                if (showtime) {
                    Row (modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically){
                        Text(
                            text = timeLine,
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .padding(horizontal = 5.dp),
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Justify,
                            color = timeLineColor
                        )
                        if (hasRepeat) {
                            Icon(painter = painterResource(id = R.drawable.autorenew), contentDescription = null, tint = Color.Yellow)
                        }
                    }
                }
                if (showListType) {
                    Text(
                        text = listType,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 5.dp),
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Justify
                    )
                }
            }
        }
    }
}
fun shareTodo(context: Context, subject: String) {
    // Create an ACTION_SEND implicit intent with order details in the intent extras
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, subject)
    }
    context.startActivity(
        Intent.createChooser(
            intent,
            context.getString(R.string.description)
        )
    )
}
