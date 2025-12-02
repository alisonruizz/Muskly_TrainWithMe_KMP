package org.example.musklytrainwithmekmp

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.asStateFlow
import kotlin.random.Random

import com.google.firebase.Firebase
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore


@kotlinx.serialization.Serializable
data class ShopData(
    val coins: Long? = null,
    val inventory: List<Map<String, Any>>? = null,
    val shopItems: List<Map<String, Any>>? = null
)

@kotlinx.serialization.Serializable
data class Item(
    val id: String = "",
    val name: String = "",
    val price: Int = 0,
    val icon: String = "",
    val isEquipped: Boolean = false
)



class ShopViewModel : ViewModel() {

    private val _coins = MutableStateFlow(500)
    val coins: StateFlow<Int> = _coins.asStateFlow()

    val characterName = MutableStateFlow("Musk")

    private val _selectedTab = MutableStateFlow("Shop")
    val selectedTab: StateFlow<String> = _selectedTab.asStateFlow()

    private val firestore = Firebase.firestore

    lateinit var userId: String
    lateinit var petName: String

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

    private fun mapToItem(map: Map<String, Any>): Item {
        return Item(
            id = map["id"] as? String ?: "",
            name = map["name"] as? String ?: "",
            price = (map["price"] as? Long)?.toInt() ?: 0,
            icon = map["icon"] as? String ?: "",
            isEquipped = map["isEquipped"] as? Boolean ?: false
        )
    }
    private fun itemToMap(item: Item): Map<String, Any> {
        return mapOf(
            "id" to item.id,
            "name" to item.name,
            "price" to item.price.toLong(),   // Firestore almacena números como Long
            "icon" to item.icon,
            "isEquipped" to item.isEquipped
        )
    }
    fun loadShop(userId: String, petName: String) {
        this.userId = userId
        this.petName = petName

        firestore.collection("users")
            .document(userId)
            .collection("mascotas")
            .document(petName)
            .get()
            .addOnSuccessListener { doc ->

                val data = doc.get("shop") as? Map<String, Any> ?: return@addOnSuccessListener

                val coins = data["coins"] as? Long
                val inventoryList = data["inventory"] as? List<Map<String, Any>>
                val shopList = data["shopItems"] as? List<Map<String, Any>>

                if (coins != null)
                    _coins.value = coins.toInt()

                if (inventoryList != null)
                    _inventoryItems.value = inventoryList.map { mapToItem(it) }

                if (shopList != null)
                    _shopItems.value = shopList.map { mapToItem(it) }

                updateEquipmentStates(_inventoryItems.value)
            }
    }

    private fun saveShopToFirebase() {
        val data = mapOf(
            "coins" to _coins.value,
            "inventory" to _inventoryItems.value.map { itemToMap(it) },
            "shopItems" to _shopItems.value.map { itemToMap(it) }
        )

        firestore.collection("users")
            .document(userId)
            .collection("mascotas")
            .document(petName)
            .set(mapOf("shop" to data), SetOptions.merge())
    }

    private fun updateEquipmentStates(inventory: List<Item>) {
        _isSunglassesEquipped.value = inventory.any { it.name == "Sunglasses" && it.isEquipped }
        _isCapEquipped.value = inventory.any { it.name == "Cap" && it.isEquipped }
        _isTshirtEquipped.value = inventory.any { it.name == "T-shirt" && it.isEquipped }
    }

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

            saveShopToFirebase()   // guardar automáticamente
        }

        _itemToBuy.value = null
    }

    fun toggleEquip(item: Item) {
        _inventoryItems.update { list ->
            list.map {
                if (it.name == item.name) {
                    val updated = it.copy(isEquipped = !it.isEquipped)
                    applyEquipment(updated)
                    saveShopToFirebase()  // guardar automáticamente
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
