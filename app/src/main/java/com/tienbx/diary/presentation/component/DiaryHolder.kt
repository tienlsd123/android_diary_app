package com.tienbx.diary.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tienbx.diary.model.Diary
import com.tienbx.diary.model.Mood
import com.tienbx.diary.ui.theme.Elevation
import com.tienbx.diary.util.toInstance
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Date
import java.util.Locale

@Composable
fun DiaryHolder(
    diary: Diary, onclick: (String) -> Unit
) {
    val localDensity = LocalDensity.current
    var componentHeight by remember { mutableStateOf(0.dp) }

    Row(
        modifier = Modifier.clickable(
            indication = null,
            interactionSource = remember { MutableInteractionSource() },
            onClick = { onclick(diary._id.toString()) }
        )
    ) {
        Spacer(modifier = Modifier.width(14.dp))
        Surface(
            modifier = Modifier
                .width(2.dp)
                .height(componentHeight + 14.dp),
            tonalElevation = Elevation.Level1
        ) {}
        Spacer(modifier = Modifier.width(20.dp))

        Surface(
            modifier = Modifier
                .clip(shape = Shapes().small)
                .onGloballyPositioned {
                    componentHeight = with(localDensity) { it.size.height.toDp() }
                }, tonalElevation = Elevation.Level1
        ) {
            Column {
                DiaryHeader(moodName = diary.mood, time = diary.date.toInstance())
                Text(
                    modifier = Modifier.padding(all = 14.dp),
                    text = diary.description,
                    style = TextStyle(fontSize = MaterialTheme.typography.bodyLarge.fontSize),
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun DiaryHeader(moodName: String, time: Instant) {
    val mood = Mood.valueOf(moodName)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(mood.containerColor)
            .padding(horizontal = 14.dp, vertical = 7.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row {
            Image(
                modifier = Modifier.size(18.dp),
                painter = painterResource(id = mood.icon),
                contentDescription = "Mood Icon"
            )
            Spacer(modifier = Modifier.width(7.dp))
            Text(
                text = mood.name,
                color = mood.contentColor,
                style = TextStyle(fontSize = MaterialTheme.typography.bodyMedium.fontSize)
            )
        }
        Text(
            text = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date.from(time)),
            color = mood.contentColor,
            style = TextStyle(fontSize = MaterialTheme.typography.bodyMedium.fontSize)
        )

    }
}

@Preview
@Composable
fun DiaryHolderPreview() {
    DiaryHolder(diary = Diary().apply {
        title = "My diary"
        description =
            "Lorem Ipsum is simply dummy text of the printing and typesetting industry. " +
                    "Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, " +
                    "when an unknown printer took a galley of type and scrambled it to make a type specimen book."
        mood = Mood.Happy.name
    }, onclick = {})
}
