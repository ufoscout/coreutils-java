package com.ufoscout.vertk.bit.mail.utils

import com.ufoscout.vertk.BaseTest
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class EmailTest : BaseTest() {

    @Test
    fun shouldValidateEmails() {
        assertTrue(Email.isValidEmailAddress("bla@bla"))
        assertTrue(Email.isValidEmailAddress("bla@bla.it"))

        assertFalse(Email.isValidEmailAddress("bla@"))
        assertFalse(Email.isValidEmailAddress("@bla"))
        assertFalse(Email.isValidEmailAddress("@"))
        assertFalse(Email.isValidEmailAddress("bla_bla"))
    }

}