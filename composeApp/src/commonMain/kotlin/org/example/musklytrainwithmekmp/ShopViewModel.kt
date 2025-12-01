package com.example.muskly_trainwithme_kmp

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull

data class Item(
    val id: String = kotlin.random.Random.nextInt().toString(),
    val name: String,
    val price: Int,
    val icon: String,
    val isEquipped: Boolean = false
)

class ShopViewModel {

    private val _coins = MutableStateFlow(500)
    val coins: StateFlow<Int> = _coins.asStateFlow()

    private val _characterName = MutableStateFlow("Musk")
    val characterName: StateFlow<String> = _characterName.asStateFlow()

    private val _selectedTab = MutableStateFlow("Shop")
    val selectedTab: StateFlow<String> = _selectedTab.asStateFlow()

    private val _shopItems = MutableStateFlow(
        listOf(
            Item(name = "Sunglasses", price = 80, icon = "visibility"),
            Item(name = "Watch", price = 120, icon = "watch")
        )
    )
    val shopItems = _shopItems.asStateFlow()

    private val _inventoryItems = MutableStateFlow(
        listOf(
            Item(name = "T-shirt", price = 0, icon = "checkroom"),
            Item(name = "Cap", price = 0, icon = "face"),
        )
    )
    val inventoryItems = _inventoryItems.asStateFlow()

    private val _itemToBuy = MutableStateFlow<Item?>(null)
    val itemToBuy: StateFlow<Item?> = _itemToBuy.asStateFlow()

    // Estado de accesorios equipados
    private val _isSunglassesEquipped = MutableStateFlow(false)
    val isSunglassesEquipped = _isSunglassesEquipped.asStateFlow()

    private val _isCapEquipped = MutableStateFlow(false)
    val isCapEquipped = _isCapEquipped.asStateFlow()

    private val _isTshirtEquipped = MutableStateFlow(false)
    val isTshirtEquipped = _isTshirtEquipped.asStateFlow()

    // -------------------- FUNCIONES ---------------------
    fun selectTab(tab: String) {
        _selectedTab.value = tab
    }

    fun showBuyDialog(item: Item) {
        _itemToBuy.value = item
    }

    fun dismissDialog() {
        _itemToBuy.value = null
    }

    fun confirmPurchase(item: Item) {
        if (_coins.value >= item.price) {
            _coins.value -= item.price

            _shopItems.update { it.filter { it.name != item.name } }
            _inventoryItems.update { it + item }
        }
        _itemToBuy.value = null
    }

    fun toggleEquip(item: Item) {
        _inventoryItems.update { list ->
            list.map {
                if (it.name == item.name) {
                    val updated = it.copy(isEquipped = !it.isEquipped)
                    applyEquipment(updated)
                    updated
                } else it
            }
        }
    }

    private fun applyEquipment(item: Item) {
        when(item.name){
            "Sunglasses" -> _isSunglassesEquipped.value = item.isEquipped
            "Cap"        -> _isCapEquipped.value = item.isEquipped
            "T-shirt"    -> _isTshirtEquipped.value = item.isEquipped
        }
    }

    fun addCoins(amount: Int) { _coins.value += amount }
}
