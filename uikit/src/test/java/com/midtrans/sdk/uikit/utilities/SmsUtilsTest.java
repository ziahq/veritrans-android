package com.midtrans.sdk.uikit.utilities;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by rakawm on 12/9/16.
 */
public class SmsUtilsTest {
    private static final String MESSAGE_CIMB = "CIMB Niaga: Paycode Anda adalah 180650 utk transaksi di Jendela360 sebesar IDR 10.000,00. Paycode berlaku selama 5 mnt.";
    private static final String MESSAGE_BCA = "From BCA: Your Authorisation code is 524038 for the purchase at Jendela360, amount IDR 10.000,00. Valid for 5 mins. Resend 2 of 3.";
    private static final String MESSAGE_BNI = "Dari BNI: Kode Otorisasi utk transaksi Anda adalah 923616 di Jendela360 sebesar IDR 10.000,00.Kode Anda berlaku 5 mnt.";
    private static final String INVALID_MESSAGE = "invalid";

    private static final String EXPECTED_CODE_CIMB = "180650";
    private static final String EXPECTED_CODE_BCA = "524038";
    private static final String EXPECTED_CODE_BNI = "923616";

    @Test
    public void testIsOtpMessage() throws Exception {
        assertTrue(SmsUtils.isOtpMessage(MESSAGE_BCA));
        assertTrue(SmsUtils.isOtpMessage(MESSAGE_CIMB));
        assertTrue(SmsUtils.isOtpMessage(MESSAGE_BNI));
        assertFalse(SmsUtils.isOtpMessage(INVALID_MESSAGE));
    }

    @Test
    public void testGetCodeFromMessage() throws Exception {
        assertEquals(EXPECTED_CODE_BCA, SmsUtils.getCodeFromMessage(MESSAGE_BCA));
        assertEquals(EXPECTED_CODE_CIMB, SmsUtils.getCodeFromMessage(MESSAGE_CIMB));
        assertEquals(EXPECTED_CODE_BNI, SmsUtils.getCodeFromMessage(MESSAGE_BNI));
        assertNull(SmsUtils.getCodeFromMessage(INVALID_MESSAGE));
    }
}