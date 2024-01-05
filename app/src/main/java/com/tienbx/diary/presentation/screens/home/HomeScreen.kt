package com.tienbx.diary.presentation.screens.home

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
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
