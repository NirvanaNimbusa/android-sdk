package com.paypal.demo

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.braintreepayments.api.Card
import com.braintreepayments.api.models.CardBuilder
import com.braintreepayments.api.models.PaymentMethodNonce
import com.braintreepayments.cardform.view.ExpirationDateEditText
import com.paypal.androidsdk.*
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

    private val cardNumberField: EditText by lazy { findViewById<EditText>(R.id.bt_card_form_card_number) }
    private val expDateField: ExpirationDateEditText by lazy { findViewById<ExpirationDateEditText>(R.id.bt_card_form_expiration) }
    private val cvvField: EditText by lazy { findViewById<EditText>(R.id.bt_card_form_cvv) }

    private val progressBar: ProgressBar by lazy { findViewById<ProgressBar>(R.id.loading_spinner)}

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
        progressBar.visibility = View.VISIBLE
        statusLabel.text = getString(R.string.checkout_initiated)

        val cardNumber = cardNumberField.text.replace("\\s+".toRegex(), "")
        val expirationMonth = expDateField.month
        val expirationYear = expDateField.year
        val cvv = cvvField.text.toString()

        val cardBuilder = CardBuilder()
                .cardNumber(cardNumber)
                .expirationMonth(expirationMonth)
                .expirationYear(expirationYear)
                .cvv(cvv)

        checkoutClient?.payWithCard(cardBuilder, orderId, this, this)
    }

    @Suppress("UNUSED_PARAMETER")
    fun initiatePayPalCheckout(view: View?) {
        progressBar.visibility = View.VISIBLE
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
                statusLabel.text = getString((R.string.checkout_placeholder))
                progressBar.visibility = View.INVISIBLE

                cardNumberField.text = null
                expDateField.text = null
                cvvField.text = null

                super.onOptionsItemSelected(item)
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onPaymentMethodNonceCreated(paymentMethodNonce: PaymentMethodNonce?) {
        val oldStatus = statusLabel.text
        val nonceString = paymentMethodNonce!!.nonce
        val newStatus = "$oldStatus\n$nonceString"
        statusLabel.text = newStatus
    }

    override fun onCheckoutComplete(error: Exception?, result: CheckoutResult?) {
        progressBar.visibility = View.INVISIBLE

        // print error message or order id on success
        var status: String? = null
        error?.let {
            status = getString(R.string.checkout_error, it.message)
        } ?: run {
            if (result is CardCheckoutResult) {
                status = getString(R.string.checkout_success, result.orderId)
            } else if (result is PayPalCheckoutResult) {
                val token = result.token
                val payerId = result.payerId
                status = "Token: $token\nPayer Id: $payerId"
            }
        }
        statusLabel.text = status
    }
}
