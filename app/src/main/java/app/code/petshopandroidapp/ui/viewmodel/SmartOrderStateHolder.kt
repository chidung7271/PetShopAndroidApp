package app.code.petshopandroidapp.ui.viewmodel

import app.code.petshopandroidapp.data.model.SmartOrderCartItem

/**
 * Singleton object để lưu state từ Smart Order tạm thời
 * Giúp share state giữa SmartOrderScreen và SellScreen
 */
object SmartOrderStateHolder {
    private var _pendingCartItems: List<SmartOrderCartItem>? = null
    val pendingCartItems: List<SmartOrderCartItem>?
        get() = _pendingCartItems

    fun setPendingCartItems(items: List<SmartOrderCartItem>) {
        _pendingCartItems = items
    }

    fun clearPendingCartItems() {
        _pendingCartItems = null
    }
}

