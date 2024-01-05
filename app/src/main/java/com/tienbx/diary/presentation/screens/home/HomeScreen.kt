package com.tienbx.diary.presentation.screens.home

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.DrawerState
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.tienbx.diary.R
import com.tienbx.diary.model.Diary
import com.tienbx.diary.model.Mood
import com.tienbx.diary.presentation.component.DiaryHolder
import io.realm.kotlin.ext.realmListOf

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    drawerState: DrawerState,
    onMenuClicked: () -> Unit,
    onSignOutClicked: () -> Unit,
    navigateToWrite: () -> Unit
) {
    NavigationDrawer(
        drawerState = drawerState,
        onSignOutClicked = onSignOutClicked
    ) {
        Scaffold(
            topBar = { HomeTopBar(onMenuClicked = onMenuClicked) },
            floatingActionButton = {
                FloatingActionButton(onClick = navigateToWrite) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "New Diary Icon"
                    )
                }
            },
            content = {

                Column (modifier = Modifier.padding(20.dp)){
                    Spacer(modifier = Modifier.height(20.dp))
                    DiaryHolder(diary = Diary().apply {
                        title = "My diary"
                        description =
                            "Lorem Ipsum is simply dummy text of the printing and typesetting industry. " +
                                    "Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, " +
                                    "when an unknown printer took a galley of type and scrambled it to make a type specimen book."
                        mood = Mood.Happy.name
                        images = realmListOf(
                            "https://images.unsplash.com/photo-1682687220801-eef408f95d71?q=80&w=2574&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDF8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                            "https://images.unsplash.com/photo-1701743806568-4ce6e19500c1?q=80&w=2572&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                            "https://images.unsplash.com/photo-1704426882813-8acfff020487?q=80&w=2574&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                        )
                    }, onclick = {})

                    Spacer(modifier = Modifier.height(20.dp))
                    DiaryHolder(diary = Diary().apply {
                        title = "My diary"
                        description =
                            "Lorem Ipsum is simply dummy text of the printing and typesetting industry. " +
                                    "Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, " +
                                    "when an unknown printer took a galley of type and scrambled it to make a type specimen book."
                        mood = Mood.Happy.name
                        images = realmListOf(
                            "https://images.unsplash.com/photo-1682687220801-eef408f95d71?q=80&w=2574&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDF8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                            "https://images.unsplash.com/photo-1701743806568-4ce6e19500c1?q=80&w=2572&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                            "https://images.unsplash.com/photo-1704426882813-8acfff020487?q=80&w=2574&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                        )
                    }, onclick = {})
                }
            }
        )
    }
}

@Composable
fun NavigationDrawer(
    drawerState: DrawerState,
    onSignOutClicked: () -> Unit,
    content: @Composable () -> Unit
) {
    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        modifier = Modifier.size(150.dp),
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "Logo Image"
                    )
                }
                NavigationDrawerItem(
                    label = {
                        Row(modifier = Modifier.padding(12.dp)) {
                            Image(painter = painterResource(R.drawable.google_logo), contentDescription = "Google Logo")
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Sign out",
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    },
                    selected = false,
                    onClick = onSignOutClicked
                )
            }
        },
        drawerState = drawerState,
        content = content
    )
}
