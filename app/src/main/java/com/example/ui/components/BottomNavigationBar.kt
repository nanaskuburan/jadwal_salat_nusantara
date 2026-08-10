package com.example.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage

@Composable
fun AppBottomNavigationBar(
    selectedTab: Int,
    language: AppLanguage,
    onTabSelected: (Int) -> Unit
) {
    NavigationBar {
        val isId = language == AppLanguage.INDONESIAN

        NavigationBarItem(
            selected = selectedTab == 0,
            onClick = { onTabSelected(0) },
            icon = { Icon(Icons.Default.Home, contentDescription = "Beranda") },
            label = { 
                Text(
                    text = if (isId) "Hari Ini" else "اليوم",
                    maxLines = 1,
                    softWrap = false,
                    fontSize = 11.sp,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                ) 
            },
            modifier = Modifier.testTag("nav_tab_home")
        )

        NavigationBarItem(
            selected = selectedTab == 1,
            onClick = { onTabSelected(1) },
            icon = { Icon(Icons.Default.CalendarMonth, contentDescription = "Kalender") },
            label = { 
                Text(
                    text = if (isId) "Jadwal" else "التقويم",
                    maxLines = 1,
                    softWrap = false,
                    fontSize = 11.sp,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                ) 
            },
            modifier = Modifier.testTag("nav_tab_calendar")
        )

        NavigationBarItem(
            selected = selectedTab == 2,
            onClick = { onTabSelected(2) },
            icon = { Icon(Icons.Default.Explore, contentDescription = "Kiblat") },
            label = { 
                Text(
                    text = if (isId) "Kiblat" else "القبلة",
                    maxLines = 1,
                    softWrap = false,
                    fontSize = 11.sp,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                ) 
            },
            modifier = Modifier.testTag("nav_tab_qibla")
        )

        NavigationBarItem(
            selected = selectedTab == 3,
            onClick = { onTabSelected(3) },
            icon = { Icon(Icons.AutoMirrored.Filled.MenuBook, contentDescription = "Doa & Dzikir") },
            label = { 
                Text(
                    text = if (isId) "Doa & Dzikir" else "الأذكار",
                    maxLines = 1,
                    softWrap = false,
                    fontSize = 10.5.sp,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                ) 
            },
            modifier = Modifier.testTag("nav_tab_duas")
        )

        NavigationBarItem(
            selected = selectedTab == 4,
            onClick = { onTabSelected(4) },
            icon = { Icon(Icons.Default.Settings, contentDescription = "Pengaturan") },
            label = { 
                Text(
                    text = if (isId) "Pengaturan" else "الإعدادات",
                    maxLines = 1,
                    softWrap = false,
                    fontSize = 10.5.sp,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                ) 
            },
            modifier = Modifier.testTag("nav_tab_tracker")
        )
    }
}

@Composable
fun AppNavigationRail(
    selectedTab: Int,
    language: AppLanguage,
    onTabSelected: (Int) -> Unit
) {
    NavigationRail {
        val isId = language == AppLanguage.INDONESIAN

        NavigationRailItem(
            selected = selectedTab == 0,
            onClick = { onTabSelected(0) },
            icon = { Icon(Icons.Default.Home, contentDescription = "Beranda") },
            label = { Text(if (isId) "Hari Ini" else "اليوم") },
            modifier = Modifier.testTag("nav_rail_home")
        )

        NavigationRailItem(
            selected = selectedTab == 1,
            onClick = { onTabSelected(1) },
            icon = { Icon(Icons.Default.CalendarMonth, contentDescription = "Kalender") },
            label = { Text(if (isId) "Jadwal" else "التقويم") },
            modifier = Modifier.testTag("nav_rail_calendar")
        )

        NavigationRailItem(
            selected = selectedTab == 2,
            onClick = { onTabSelected(2) },
            icon = { Icon(Icons.Default.Explore, contentDescription = "Kiblat") },
            label = { Text(if (isId) "Kiblat" else "القبلة") },
            modifier = Modifier.testTag("nav_rail_qibla")
        )

        NavigationRailItem(
            selected = selectedTab == 3,
            onClick = { onTabSelected(3) },
            icon = { Icon(Icons.AutoMirrored.Filled.MenuBook, contentDescription = "Doa & Dzikir") },
            label = { Text(if (isId) "Doa" else "الأدعية") },
            modifier = Modifier.testTag("nav_rail_duas")
        )

        NavigationRailItem(
            selected = selectedTab == 4,
            onClick = { onTabSelected(4) },
            icon = { Icon(Icons.Default.Settings, contentDescription = "Pengaturan") },
            label = { Text(if (isId) "Pengaturan" else "الإعدادات") },
            modifier = Modifier.testTag("nav_rail_tracker")
        )
    }
}

