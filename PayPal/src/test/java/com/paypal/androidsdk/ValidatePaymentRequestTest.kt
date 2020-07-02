package com.paypal.androidsdk

import android.os.Build
import com.braintreepayments.api.models.PayPalUAT
import com.braintreepayments.api.models.PaymentMethodNonce
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [ Build.VERSION_CODES.P ])
class ValidatePaymentRequestTest {

    private lateinit var paymentMethodNonce: PaymentMethodNonce

    @Before
    fun beforeEach() {
        paymentMethodNonce = mock()
    }

    @Test
    fun getHttpRequestData_whenThreeDSecureRequested_hasThreeDSecureContingency() {
        whenever(paymentMethodNonce.nonce).thenReturn("samplePaymentMethodNonce")

        val sut = ValidatePaymentRequest.Builder()
                .orderId("sampleOrderId")
                .paymentMethodNonce(paymentMethodNonce)
                .threeDSecureRequested(true)
                .build()

        val expectedResult = JSONObject(
            """
            {
                "payment_source": {
                    "contingencies": ["3D_SECURE"],
                    "token": {
                        "id": "samplePaymentMethodNonce",
                        "type": "NONCE"
                    }
                }
            }
            """.trimIndent()
        )
        assertEquals(sut.httpBody, expectedResult.toString())
    }

    @Test
    fun getHttpRequestData_whenThreeDSecureNotRequested_omitsThreeDSecureContingency() {
        whenever(paymentMethodNonce.nonce).thenReturn("samplePaymentMethodNonce")

        val sut = ValidatePaymentRequest.Builder()
                .orderId("sampleOrderId")
                .paymentMethodNonce(paymentMethodNonce)
                .threeDSecureRequested(false)
                .build()

        val expectedResult = JSONObject(
            """
            {
                "payment_source": {
                    "contingencies": [],
                    "token": {
                        "id": "samplePaymentMethodNonce",
                        "type": "NONCE"
                    }
                }
            }
            """.trimIndent()
        )
        assertEquals(sut.httpBody, expectedResult.toString())
    }
}