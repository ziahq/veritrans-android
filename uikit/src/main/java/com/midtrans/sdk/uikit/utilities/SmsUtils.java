package com.midtrans.sdk.uikit.utilities;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by rakawm on 12/9/16.
 */

public class SmsUtils {
    public static final Pattern ALL_PATTERN = Pattern.compile("CIMB Niaga: Paycode Anda adalah [0-9]+ utk transaksi di [a-zA-Z0-9 ]+ sebesar IDR [0-9.,]+. Paycode berlaku selama [0-9]+ mnt.( Pengiriman [0-9]+ dari [0-9]+.)?|From BCA: Your Authorisation code is [0-9]+ for the purchase at [a-zA-Z0-9 ]+, amount IDR [0-9.,]+. Valid for [0-9]+ mins.( Resend [0-9]+ of [0-9]+.)?|Dari BNI: Kode Otorisasi utk transaksi Anda adalah [0-9]+ di [a-zA-Z0-9 ]+ sebesar IDR [0-9.,]+.Kode Anda berlaku [0-9]+ mnt.( Pengiriman [0-9]+ dari [0-9]+.)?");
    private static final Pattern NUMBER_PATTERN = Pattern.compile("[0-9]{6}");
    private static final Pattern BCA_PATTERN = Pattern.compile("From BCA: Your Authorisation code is [0-9]+ for the purchase at [a-zA-Z0-9 ]+, amount IDR [0-9.,]+. Valid for [0-9]+ mins.( Resend [0-9]+ of [0-9]+.)?");
    private static final Pattern BNI_PATTERN = Pattern.compile("Dari BNI: Kode Otorisasi utk transaksi Anda adalah [0-9]+ di [a-zA-Z0-9 ]+ sebesar IDR [0-9.,]+.Kode Anda berlaku [0-9]+ mnt.( Pengiriman [0-9]+ dari [0-9]+.)?");
    private static final Pattern CIMB_PATTERN = Pattern.compile("CIMB Niaga: Paycode Anda adalah [0-9]+ utk transaksi di [a-zA-Z0-9 ]+ sebesar IDR [0-9.,]+. Paycode berlaku selama [0-9]+ mnt.( Pengiriman [0-9]+ dari [0-9]+.)?");

    /**
     * Check if the message is an OTP message by using regex pattern.
     */
    public static boolean isOtpMessage(String message) {
        return ALL_PATTERN.matcher(message).matches();
    }

    /**
     * Get payment code from SMS message.
     *
     * @param message SMS message
     */
    public static String getCodeFromMessage(String message) {
        if (BCA_PATTERN.matcher(message).matches()) {
            Matcher bcaMatcher = NUMBER_PATTERN.matcher(message);
            return extractCodeFromMessage(bcaMatcher);
        } else if (BNI_PATTERN.matcher(message).matches()) {
            Matcher bniMatcher = NUMBER_PATTERN.matcher(message);
            return extractCodeFromMessage(bniMatcher);
        } else if (CIMB_PATTERN.matcher(message).matches()) {
            Matcher cimbMatcher = NUMBER_PATTERN.matcher(message);
            return extractCodeFromMessage(cimbMatcher);
        }

        return null;
    }

    private static String extractCodeFromMessage(Matcher matcher) {
        matcher.find();
        return matcher.group(0);
    }
}
