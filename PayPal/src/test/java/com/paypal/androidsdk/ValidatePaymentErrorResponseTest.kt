package com.paypal.androidsdk

import android.os.Build
import com.braintreepayments.api.exceptions.BraintreeApiErrorResponse
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
class ValidatePaymentErrorResponseTest {

    private lateinit var braintreeApiErrorResponse: BraintreeApiErrorResponse

    @Before
    fun beforeEach() {
        braintreeApiErrorResponse = mock()
    }

    @Test
    fun fromBraintreeErrorResponse_whenContingencyUrlPresent_parsesContingencyUrl() {
        val responseBody = """
            {
                links: [
                    {
                        "rel": "3ds-contingency-resolution",
                        "href": "https://sample.com"
                    }
                ]
            }
        """.trimIndent()
        whenever(braintreeApiErrorResponse.errorResponse).thenReturn(responseBody)

        val sut = ValidatePaymentErrorResponse.from(braintreeApiErrorResponse)
        assertEquals(sut.contingencyUrl, "https://sample.com")
    }
}