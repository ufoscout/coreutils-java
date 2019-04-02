package com.ufoscout.vertk.bit.web

data class ErrorDetails(
        val code: Int,
        val message: String,
        val details: Map<String, List<String>>) {
}
