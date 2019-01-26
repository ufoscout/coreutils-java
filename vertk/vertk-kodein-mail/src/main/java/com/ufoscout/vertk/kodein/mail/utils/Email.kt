package com.ufoscout.vertk.kodein.mail.utils

import io.vertx.ext.mail.mailencoder.EmailAddress

object Email {

    /**
     * Validate the email address using the [io.vertx.ext.mail.mailencoder.EmailAddress]
     */
    fun isValidEmailAddress(emailAddress: String?): Boolean {
        if (emailAddress == null) return false
        try {
            EmailAddress(emailAddress)
            return true
        } catch (ex: IllegalArgumentException) {
            return false
        }

    }

}
