package com.paypal.demo

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.braintreepayments.api.models.CardBuilder
import com.paypal.androidsdk.CardCheckoutResult
import com.paypal.androidsdk.CheckoutClient
import com.paypal.androidsdk.CheckoutListener
import com.paypal.androidsdk.CheckoutResult
import com.paypal.demo.models.Amount
import com.paypal.demo.models.OrderRequest
import com.paypal.demo.models.Payee
import com.paypal.demo.models.PurchaseUnit

class MainActivity : AppCompatActivity(), CheckoutListener {

    private val merchant = Merchant()
    private var checkoutClient: CheckoutClient? = null

    private var orderId: String? = null

    private val uatLabel: TextView by lazy { findViewById<TextView>(R.id.uatTextView) }
    private val statusLabel: TextView by lazy { findViewById<TextView>(R.id.statusTextView) }
    private val orderIdLabel: TextView by lazy { findViewById<TextView>(R.id.orderIDTextView) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fetchUAT()
    }

    override fun onResume() {
        super.onResume()
        checkoutClient?.onResume(this)
    }

    private fun fetchUAT() {
        uatLabel.text = getString(R.string.uat_fetching)
        merchant.fetchUAT { error, uat ->
            error?.let {
                uatLabel.text = getString(R.string.uat_display, it.message)
            } ?: run {
                checkoutClient = CheckoutClient(uat!!, this)
                uatLabel.text = getString(R.string.uat_display, uat)
            }
        }
    }

    @Suppress("UNUSED_PARAMETER")
    fun fetchOrderId(view: View) {
        val amount = Amount("USD", "10.00")
        val payee = Payee("ahuang-ppcp-demo-sb1@paypal.com")
        val purchaseUnit = PurchaseUnit(amount, payee)
        val orderRequest = OrderRequest("CAPTURE", listOf(purchaseUnit))

        merchant.fetchOrderId(orderRequest) { error, orderId ->
            error?.let {
                orderIdLabel.text = getString(R.string.error_order_id_not_found)
            } ?: run {
                orderId?.let {
                    this.orderId = it
                    orderIdLabel.text = getString(R.string.order_id_display, this.orderId)
                }
            }
        }
    }

    @Suppress("UNUSED_PARAMETER")
    fun initiateCardCheckout(view: View) {
        statusLabel.text = getString(R.string.checkout_initiated)

        // trigger 3ds v1
        val cardBuilder = CardBuilder()
                .cardholderName("Suzie Smith")
                .cardNumber("4000000000000002")
                .expirationMonth("01")
                .expirationYear("2023")
                .cvv("123")

        checkoutClient?.payWithCard(cardBuilder, orderId, this) { error, result ->
            var status: String? = null
            error?.let {
                status = getString(R.string.checkout_error, it.message)
            } ?: run {
                val orderId = (result as? CardCheckoutResult)?.orderId
                status = getString(R.string.checkout_success, orderId)
            }
            statusLabel.text = status
        }
    }

    @Suppress("UNUSED_PARAMETER")
    fun initiatePayPalCheckout(view: View?) {
        checkoutClient?.payWithPayPal(this.orderId, this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.settings -> {
                this.startActivity(Intent(this, SettingsActivity::class.java))
                true
            }
            R.id.reset -> {
                fetchUAT()
                this.orderId = null
                orderIdLabel.text = getString(R.string.order_id_placeholder)
                super.onOptionsItemSelected(item)
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCheckoutComplete(error: Exception?, result: CheckoutResult?) {
        // TODO: process result
    }
}
