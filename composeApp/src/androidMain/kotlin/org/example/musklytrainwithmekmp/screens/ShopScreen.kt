package org.example.musklytrainwithmekmp.screens


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.musklytrainwithmekmp.Item
import org.example.musklytrainwithmekmp.ShopViewModel

import muskly_trainwithme_kmp.composeapp.generated.resources.Res
import muskly_trainwithme_kmp.composeapp.generated.resources.chiguicoin_png
import muskly_trainwithme_kmp.composeapp.generated.resources.default_shop_png
import muskly_trainwithme_kmp.composeapp.generated.resources.img17
import muskly_trainwithme_kmp.composeapp.generated.resources.img18
import muskly_trainwithme_kmp.composeapp.generated.resources.img19
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource


@Composable
fun ShopScreen(viewModel: ShopViewModel = remember {  ShopViewModel()}) {

    val coins by viewModel.coins.collectAsState()
    val characterName by viewModel.characterName.collectAsState()
    val selectedTab by viewModel.selectedTab.collectAsState()
    val shopItems by viewModel.shopItems.collectAsState()
    val inventoryItems by viewModel.inventoryItems.collectAsState()
    val itemToBuy by viewModel.itemToBuy.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .padding(16.dp)
    ) {

        Spacer(Modifier.height(12.dp))
        // --- Monedas ---
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(Res.drawable.chiguicoin_png),
                contentDescription = "Coins",
                modifier = Modifier.size(45.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                coins.toString(),
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Yellow
            )
            Spacer(Modifier.weight(1f))
        }

        Spacer(Modifier.height(12.dp))

        // --- Nombre del personaje ---
        Text(
            text = characterName,
            fontSize = 35.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(Modifier.height(16.dp))

        // --- Personaje con accesorios superpuestos (comoSwiftUI) ---
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(Res.drawable.default_shop_png),
                contentDescription = "",
                modifier = Modifier.size(160.dp)
            )
            val sunglassesEquipped by viewModel.isSunglassesEquipped.collectAsState()
            val capEquipped by viewModel.isCapEquipped.collectAsState()
            val tshirtEquipped by viewModel.isTshirtEquipped.collectAsState()
            // Gafas
            if (sunglassesEquipped) {
                AccessoryImage(Res.drawable.img17, xOffset = -5, yOffset = -20)
            }
            // Gorra
            if (capEquipped) {
                AccessoryImage(Res.drawable.img18, xOffset = -2, yOffset = -52)
            }
            // Camiseta
            if (tshirtEquipped) {
                AccessoryImage(Res.drawable.img19, xOffset = -2, yOffset = 36)
            }
        }

        Spacer(Modifier.height(16.dp))

        // ----------- Tabs Mejorados -----------
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ShopTab(
                title = "Inventory",
                selected = selectedTab == "Inventory",
                modifier = Modifier.weight(1f)
            ) { viewModel.selectTab("Inventory") }

            ShopTab(
                title = "Shop",
                selected = selectedTab == "Shop",
                modifier = Modifier.weight(1f)
            ) { viewModel.selectTab("Shop") }
        }

        Spacer(Modifier.height(10.dp))

        // --- Contenido dinámico ---
        when (selectedTab) {
            "Shop" -> ShopSection(shopItems) { viewModel.showBuyDialog(it) }
            "Inventory" -> InventorySection(inventoryItems) { viewModel.toggleEquip(it) }
        }
    }

    // --- AlertDialog de compra ---
    itemToBuy?.let { item ->
        AlertDialog(
            onDismissRequest = viewModel::dismissDialog,
            title = { Text("Confirm Purchase") },
            text = { Text("Are you sure you want to buy ${item.name}?") },
            confirmButton = {
                Button(onClick = { viewModel.confirmPurchase(item) }) { Text("Yes") }
            },
            dismissButton = {
                OutlinedButton(onClick = viewModel::dismissDialog) { Text("Cancel") }
            }
        )
    }
}

@Composable
fun ShopTab(
    title: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    ) {
    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(vertical = 6.dp),  // separacion visual
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            fontSize = 30.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
            color = MaterialTheme.colorScheme.secondary
        )
        Spacer(Modifier.height(4.dp))


        AnimatedVisibility(visible = selected) {
            Box(
                modifier = Modifier
                    .height(4.dp)
                    .fillMaxWidth(0.5f) // longitud controlada de la barra
                    .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(40))
            )
        }
    }
}

@Composable
fun AccessoryImage(res: DrawableResource, xOffset: Int, yOffset: Int) {
    Image(
        painter = painterResource(res),
        contentDescription = "",
        modifier = Modifier
            .size(80.dp)
            .offset(x = xOffset.dp, y = yOffset.dp)
    )
}
@Composable
fun ShopSection(items: List<Item>, onBuy: (Item) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(items) { item ->
            ShopItemCard(item, onBuy)
        }
    }
}

@Composable
fun ShopItemCard(item: Item, onBuy: (Item) -> Unit) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.secondary, RoundedCornerShape(12.dp))
            .padding(12.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ItemIcon(item)
        Text(item.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("${item.price}", color = Color.Yellow)
            Image(painterResource(Res.drawable.chiguicoin_png), "", Modifier.size(18.dp))
        }
        Button(onClick = { onBuy(item) }) { Text("Buy") }
    }
}

@Composable
fun ItemIcon(item: Item) {
    val icon = when(item.icon){
        "visibility" ->  Icons.Default.Favorite

        "style"      -> Icons.Default.Favorite

        "checkroom"     -> Icons.Default.Favorite
        "face"        -> Icons.Default.Favorite
        else         -> Icons.Default.Favorite
    }

    Icon(
        imageVector = icon, contentDescription = null, Modifier.size(42.dp))
}

@Composable
fun InventorySection(items: List<Item>, onToggle: (Item) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(items) { item ->
            InventoryCard(item) { onToggle(item) }
        }
    }
}

@Composable
fun InventoryCard(item: Item, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.secondary, RoundedCornerShape(12.dp))
            .padding(12.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        ItemIcon(item)

        Text(item.name, fontWeight = FontWeight.Bold)
        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = if (item.isEquipped) Color.Red else MaterialTheme.colorScheme.primary
            )
        ) {
            Text(if (item.isEquipped) "Quit" else "Equip")
        }
    }
}

