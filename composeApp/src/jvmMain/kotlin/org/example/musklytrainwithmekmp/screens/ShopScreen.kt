package org.example.musklytrainwithmekmp.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import muskly_trainwithme_kmp.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.painterResource
import org.example.musklytrainwithmekmp.Item
import org.example.musklytrainwithmekmp.ShopViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.zIndex
import androidx.compose.foundation.layout.offset
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.ui.graphics.vector.ImageVector
//import androidx.compose.ui.res.vectorResource
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.ui.platform.LocalGraphicsContext
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.EmojiPeople
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Headset
import androidx.compose.ui.text.style.TextAlign
import muskly_trainwithme_kmp.composeapp.generated.resources.chiguicoin_png
import muskly_trainwithme_kmp.composeapp.generated.resources.default_shop_png
import muskly_trainwithme_kmp.composeapp.generated.resources.img17
import muskly_trainwithme_kmp.composeapp.generated.resources.img18
import muskly_trainwithme_kmp.composeapp.generated.resources.img19
//import androidx.compose.foundation.lazy.grid.GridItemSpan.Companion as GridItemSpanCompanion // unused but harmless

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ShopScreen(viewModel: ShopViewModel? = null) {
    // keep single instance across recompositions if not injected
    val vm = viewModel ?: remember { ShopViewModel() }

    val coins by vm.coins.collectAsState()
    val characterName by vm.characterName.collectAsState()
    val selectedTab by vm.selectedTab.collectAsState()
    val shopItems by vm.shopItems.collectAsState()
    val inventoryItems by vm.inventoryItems.collectAsState()
    val itemToBuy by vm.itemToBuy.collectAsState()
    val isSunglassesEquipped by vm.isSunglassesEquipped.collectAsState()
    val isCapEquipped by vm.isCapEquipped.collectAsState()
    val isTshirtEquipped by vm.isTshirtEquipped.collectAsState()

    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.secondaryContainer),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // Monedas
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(Res.drawable.chiguicoin_png),
                contentDescription = "coin",
                modifier = Modifier.size(45.dp),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "$coins",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFFF00)
            )
            Spacer(modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(8.dp))

        //Nombre del personaje
        Text(
            text = characterName,
            fontSize = 35.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Imagen del personaje con accesorios
        Box(
            modifier = Modifier
                .wrapContentSize()
                .padding(top = 8.dp)
        ) {
            // personaje base
            Image(
                painter = painterResource(Res.drawable.default_shop_png),
                contentDescription = "character",
                modifier = Modifier
                    .size(160.dp)
                    .align(Alignment.TopCenter),
                contentScale = ContentScale.Fit
            )

            // gafas
            this@Column.AnimatedVisibility(
                visible = isSunglassesEquipped,
                enter = fadeIn(animationSpec = tween(300)),
                exit = fadeOut(animationSpec = tween(300))
            ) {
                Image(
                    painter = painterResource(Res.drawable.img17),
                    contentDescription = "sunglasses",
                    modifier = Modifier
                        .size(80.dp)
                        .offset(x = 35.dp, y = (15).dp)
                        .align(Alignment.Center),
                    contentScale = ContentScale.Fit
                )
            }

            // gorra
            this@Column.AnimatedVisibility(
                visible = isCapEquipped,
                enter = fadeIn(animationSpec = tween(300)),
                exit = fadeOut(animationSpec = tween(300))
            ) {
                Image(
                    painter = painterResource(Res.drawable.img18),
                    contentDescription = "cap",
                    modifier = Modifier
                        .size(80.dp)
                        .offset(x = (35).dp, y = (-10).dp)
                        .align(Alignment.Center),
                    contentScale = ContentScale.Fit
                )
            }

            // camiseta
            this@Column.AnimatedVisibility(
                visible = isTshirtEquipped,
                enter = fadeIn(animationSpec = tween(300)),
                exit = fadeOut(animationSpec = tween(300))
            ) {
                Image(
                    painter = painterResource(Res.drawable.img19),
                    contentDescription = "tshirt",
                    modifier = Modifier
                        .size(80.dp)
                        .offset(x = (38).dp, y = 72.dp)
                        .align(Alignment.Center),
                    contentScale = ContentScale.Fit
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Tabs
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Box(modifier = Modifier.weight(1f)) {
                TabItem(
                    title = "Inventory",
                    isSelected = selectedTab == "Inventory"
                ) { vm.selectTab("Inventory") }
            }

            Box(modifier = Modifier.weight(1f)) {
                TabItem(
                    title = "Shop",
                    isSelected = selectedTab == "Shop"
                ) { vm.selectTab("Shop") }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Contenido dinámico
        if (selectedTab == "Shop") {
            ShopSection(items = shopItems) { item ->
                vm.showBuyDialog(item)
            }
        } else {
            InventorySection(items = inventoryItems) { item ->
                vm.toggleEquip(item)
            }
        }
    }

    // --- Alert dialog para confirmar compra
    if (itemToBuy != null) {
        val item = itemToBuy!!
        AlertDialog(
            onDismissRequest = { vm.dismissDialog() },
            title = { Text("Confirm Purchase") },
            text = { Text("Are you sure you want to buy ${item.name}?") },
            confirmButton = {
                TextButton(onClick = {
                    vm.confirmPurchase(item)
                }) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = { vm.dismissDialog() }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun TabItem(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = title,
            fontSize = 30.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.height(6.dp))

        if (isSelected) {
            Box(
                modifier = Modifier
                    .height(6.dp)
                    .fillMaxWidth()        // mismo ancho para ambos tabs
                    .clip(RoundedCornerShape(3.dp))
                    .background(Color(0xFF00C853)) // verde bonito
            )
        } else {
            Spacer(modifier = Modifier.height(6.dp))
        }
    }
}


@Composable
fun ShopSection(items: List<Item>, onBuy: (Item) -> Unit) {
    // two columns like LazyVGrid 2 flexible
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        content = {
            items(items) { item ->
                ShopItem(item = item, onBuy = { onBuy(item) })
            }
        }
    )
}

@Composable
fun ShopItem(item: Item, onBuy: () -> Unit) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.secondary, RoundedCornerShape(12.dp))
            .padding(12.dp)
            .fillMaxWidth()
    ) {

        val iconPainter = when (item.icon.lowercase()) {
            "visibility" -> null
            "watch" -> null
            else -> null
        }


        when (item.icon.lowercase()) {
            "visibility" -> {
                Icon(
                    imageVector = Icons.Default.Visibility,
                    contentDescription = item.icon,
                    modifier = Modifier.size(52.dp),
                    tint = MaterialTheme.colorScheme.secondaryContainer
                )
            }
            "watch" -> {

                Icon(
                    imageVector = Icons.Default.Schedule,
                    contentDescription = item.icon,
                    modifier = Modifier.size(52.dp),
                    tint = MaterialTheme.colorScheme.secondaryContainer
                )
            }
            else -> {

                Image(
                    painter = painterResource(Res.drawable.chiguicoin_png),
                    contentDescription = item.icon,
                    modifier = Modifier.size(52.dp),
                    contentScale = ContentScale.Fit
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = item.name,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.secondaryContainer
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${item.price}",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFFFFFF00) // yellow
            )
            Spacer(modifier = Modifier.width(6.dp))
            Image(
                painter = painterResource(Res.drawable.chiguicoin_png),
                contentDescription = "coin",
                modifier = Modifier.size(18.dp),
                contentScale = ContentScale.Fit
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = onBuy,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Text(
                text = "Buy",
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

@Composable
fun InventorySection(items: List<Item>, onToggleEquip: (Item) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        content = {
            items(items) { item ->
                InventoryItem(item = item, onToggleEquip = { onToggleEquip(item) })
            }
        }
    )
}

@Composable
fun InventoryItem(item: Item, onToggleEquip: () -> Unit) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.secondary, RoundedCornerShape(12.dp))
            .padding(12.dp)
            .fillMaxWidth()
    ) {

        //ICONOS
        when {

            // Gafas
            item.name.equals("sunglasses", true)
                    || item.icon.equals("visibility", true)
                    || item.icon.contains("glass", true) -> {

                Icon(
                    imageVector = Icons.Default.Visibility,
                    contentDescription = "sunglasses",
                    modifier = Modifier.size(52.dp),
                    tint = MaterialTheme.colorScheme.secondaryContainer
                )
            }

            // Camiseta (Checkroom)
            item.name.equals("t-shirt", true)
                    || item.name.equals("tshirt", true)
                    || item.name.equals("shirt", true)
                    || item.icon.contains("img19", true) -> {

                Icon(
                    imageVector = Icons.Default.Checkroom,
                    contentDescription = "tshirt",
                    modifier = Modifier.size(52.dp),
                    tint = MaterialTheme.colorScheme.secondaryContainer
                )
            }

            // Gorra
            item.name.equals("cap", true)
                    || item.name.equals("hat", true)
                    || item.name.equals("gorra", true)
                    || item.icon.contains("img18", true) -> {

                Icon(
                    imageVector = Icons.Default.Headphones,
                    contentDescription = "cap",
                    modifier = Modifier.size(52.dp),
                    tint = MaterialTheme.colorScheme.secondaryContainer
                )
            }

            // Watch
            item.icon.equals("watch", true) -> {
                Icon(
                    imageVector = Icons.Default.Schedule,
                    contentDescription = "watch",
                    modifier = Modifier.size(52.dp),
                    tint = MaterialTheme.colorScheme.secondaryContainer
                )
            }

            else -> {
                Image(
                    painter = painterResource(Res.drawable.chiguicoin_png),
                    contentDescription = item.icon,
                    modifier = Modifier.size(52.dp),
                    contentScale = ContentScale.Fit
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = item.name,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(8.dp))

        val btnText = if (item.isEquipped) "Quit" else "Equip"
        val btnColor = if (item.isEquipped) Color.Red else MaterialTheme.colorScheme.primaryContainer

        Button(
            onClick = onToggleEquip,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = btnColor)
        ) {
            Text(
                text = btnText,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

