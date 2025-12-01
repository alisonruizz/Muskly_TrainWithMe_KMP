package com.example.muskly_trainwithme_kmp.android.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.muskly_trainwithme_kmp.TipsViewModel
import com.example.muskly_trainwithme_kmp.ui.theme.Muskly_TrainWithMeTheme
import muskly_trainwithme_kmp.composeapp.generated.resources.Res
import muskly_trainwithme_kmp.composeapp.generated.resources.tips
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TipsScreen(viewModel: TipsViewModel = TipsViewModel()) {

    val categories by viewModel.categoriesWithTips.collectAsState()
    val expanded by viewModel.expandedIndex.collectAsState()

    Scaffold(
    ) { padding ->

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(Res.drawable.tips),
                contentDescription = null,
                modifier = Modifier
                    .size(110.dp)
                    .padding(bottom = 8.dp),
                contentScale = ContentScale.Fit
            )

            Text(
                "Tips and advices",
                fontSize = 30.sp,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(Modifier.height(18.dp))

            categories.forEach { (categoryName, tips) ->

                Text(
                    categoryName,
                    fontSize = 22.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                )

                tips.forEachIndexed { index, tip ->
                    val tipIndex = "$categoryName-$index"

                    Column(
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                            .background(
                                color = MaterialTheme.colorScheme.secondary
                                ,shape = RoundedCornerShape(12.dp))
                            .padding(12.dp)
                            .fillMaxWidth()

                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(64.dp))) {
                            Text(
                                tip.short,
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.background,
                                modifier = Modifier.weight(1f)
                            )

                            IconButton(
                                onClick = { viewModel.toggleExpanded(tipIndex) },
                                modifier = Modifier
                                    .background(MaterialTheme.colorScheme.primaryContainer, CircleShape)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Expand",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }

                        if (expanded == tipIndex) {
                            Text(
                                tip.details,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.background,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TipsScreenPreview() {
    Muskly_TrainWithMeTheme {
        TipsScreen()
    }
}
