package com.holeCode.novamoda


interface TransactionCallback {
    fun onTransactionComplete()
}

class PendingTransactionManager {
    private val callbacks = mutableListOf<TransactionCallback>()

    fun registerCallback(callback: TransactionCallback) {
        callbacks.add(callback)
    }

    // This method should be called when a transaction is completed
    fun notifyTransactionComplete() {
        callbacks.forEach { it.onTransactionComplete() }
    }
}

class TransactionManager {
    private var isCallbackRegistered = false
    private val pendingTransactionManager = PendingTransactionManager()

    fun registerCallbackForPendingTransactions() {
        // Check if callback is already registered to avoid redundant registration
        if (!isCallbackRegistered) {
            // Register the callback
            pendingTransactionManager.registerCallback(object : TransactionCallback {
                override fun onTransactionComplete() {
                    // Handle transaction completion
                }
            })
            isCallbackRegistered = true
        }
    }

    fun completeTransaction() {
        pendingTransactionManager.notifyTransactionComplete()
    }
}

