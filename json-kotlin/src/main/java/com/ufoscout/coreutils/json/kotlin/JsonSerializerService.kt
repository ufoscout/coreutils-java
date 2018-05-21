package com.ufoscout.coreutils.json.kotlin

import com.ufoscout.coreutils.json.JsonSerializerService
import java.io.OutputStream
import kotlin.reflect.KClass

/**
 *
 * @author Francesco Cina'
 */
class JsonSerializerService(private val jsonService: JsonSerializerService) {

    /**
     * Return the json representation of the Bean
     * @param obj
     * @return
     */
    fun toJson(obj: Any): String {
        return jsonService.toJson(obj)
    }

    /**
     * Return the json representation of the Bean
     * @param obj
     */
    fun toJson(obj: Any, out: OutputStream) {
        jsonService.toJson(obj, out)
    }

    /**
     * Return the json representation of the Bean
     * WARN: it is slower than the other method!
     * @param obj
     * @return
     */
    fun toPrettyPrintedJson(obj: Any): String {
        return jsonService.toPrettyPrintedJson(obj)
    }

    /**
     * Return the json representation of the Bean
     * WARN: it is slower than the other method!
     * @param obj
     */
    fun toPrettyPrintedJson(obj: Any, out: OutputStream) {
        jsonService.toPrettyPrintedJson(obj, out)
    }

    /**
      * Method to deserialize JSON content from given JSON content String.
      */
    fun <T: Any> fromJson(clazz: KClass<T>, json: String): T {
        return jsonService.fromJson(clazz.java, json)
    }

    /**
     * Method to deserialize JSON content from given JSON content String.
     */
    inline fun <reified T: Any> fromJson(json: String) = fromJson(T::class, json)
}

