package com.example.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.DuaDataProvider
import com.example.model.DuaItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DuaScreen(
    language: AppLanguage
) {
    val isId = language == AppLanguage.INDONESIAN

    var selectedCategoryIndex by remember { mutableIntStateOf(0) }
    val categories = listOf("Zikir Bakda Salat Fardhu", "Doa Sunnah Harian", "Dzikir Pagi", "Dzikir Petang")

    val filteredDuas = DuaDataProvider.ALL_DUAS.filter {
        it.category == categories[selectedCategoryIndex]
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        // CATEGORY CHIPS (RTL Safe)
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(categories) { index, _ ->
                val labelText = when (index) {
                    0 -> if (isId) "Zikir Salat" else "أذكار الصلاة"
                    1 -> if (isId) "Doa Sunnah" else "أدعية السنة"
                    2 -> if (isId) "Zikir Pagi" else "أذكار الصباح"
                    else -> if (isId) "Zikir Petang" else "أذكار المساء"
                }

                val isSelected = selectedCategoryIndex == index
                FilterChip(
                    selected = isSelected,
                    onClick = { selectedCategoryIndex = index },
                    label = {
                        Text(
                            text = labelText,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    modifier = Modifier.testTag("dua_tab_$index")
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            itemsIndexed(filteredDuas, key = { index, dua -> "${dua.id}_$index" }) { _, dua ->
                DuaCardItem(
                    dua = dua,
                    isId = isId
                )
            }
            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
fun DuaCardItem(
    dua: DuaItem,
    isId: Boolean
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("dua_card_${dua.id}")
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.MenuBook,
                    contentDescription = "Dua",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (isId) dua.titleId else dua.titleAr,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = if (isId) dua.reference else dua.referenceAr,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Arabic Text
            Text(
                text = dua.arabic,
                style = MaterialTheme.typography.headlineSmall,
                fontFamily = com.example.ui.theme.QuranFontFamily,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Start,
                lineHeight = 40.sp,
                modifier = Modifier.fillMaxWidth()
            )

            if (isId) {
                Spacer(modifier = Modifier.height(12.dp))

                // Latin Transliteration
                Text(
                    text = dua.latin,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Translation / Explanation
            Text(
                text = if (isId) dua.translationId else dua.translationAr,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
