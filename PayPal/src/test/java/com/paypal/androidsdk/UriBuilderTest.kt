package com.paypal.androidsdk

import android.net.Uri
import android.os.Build
import com.braintreepayments.api.models.PayPalUAT
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [ Build.VERSION_CODES.P ])
class UriBuilderTest {

    private lateinit var uat: PayPalUAT

    @Before
    fun beforeEach() {
        uat = mock()
    }

    @Test
    fun testBuildValidatePaymentUri() {
        val sut = UriBuilder()
        whenever(uat.payPalURL).thenReturn("https://sample.com")

        val result = sut.buildValidatePaymentUri("sampleOrderId", uat)
        val expectedUrl =
            "https://sample.com/v2/checkout/orders/sampleOrderId/validate-payment-method"
        assertEquals(result, Uri.parse(expectedUrl))
    }
}