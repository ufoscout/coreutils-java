package com.ufoscout.vertk.bit.web

/**
 * BadRequest Exception thrown when input is not valid
 *
 * @author Francesco Cina'
 */
class BadRequestException(message: String) : RuntimeException(message) {
    companion object {
        private val serialVersionUID = 1L
    }
}
