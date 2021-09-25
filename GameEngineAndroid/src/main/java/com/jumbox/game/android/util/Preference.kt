package com.jumbox.game.android.util

import org.w3c.dom.Element
import org.w3c.dom.Node
import java.io.IOException
import javax.xml.parsers.DocumentBuilderFactory

/**
 * Created by Jumadi Janjaya date on 13/06/2021.
 * Bengkulu, Indonesia.
 * Copyright (c) Jumbox. All rights reserved.
 **/
class Preference(name: String) {

    private val fileHandle = Files.rootApp("shared_prefs/$name.xml")
    private val dataMap = HashMap<String, DataMap>()

    init {
        if (fileHandle.exists()) {
            load()
        }
    }

    /**
     * set @param key String @param value String or Int or Float or Long
     */
    fun <T> set(key: String, value: T) : Preference {
        when(value) {
            is String -> dataMap[key] = DataMap(key, value, "string")
            is Int -> dataMap[key] = DataMap(key, value.toString(), "int")
            is Float -> dataMap[key] = DataMap(key, value.toString(), "float")
            is Long -> dataMap[key] = DataMap(key, value.toString(), "long")
        }
        return this
    }

    fun setString(key: String, value: String) : Preference  = set(key, value)
    fun setInt(key: String, value: Int) : Preference  = set(key, value)
    fun setFloat(key: String, value: Float) : Preference  = set(key, value)
    fun setLong(key: String, value: Long) : Preference  = set(key, value)

    @Suppress("UNCHECKED_CAST")
    fun <T> get(key: String, defaultValue: T) : T {
       return when(defaultValue) {
            is String -> if(dataMap[key]?.type == "string") dataMap[key]?.value as T else defaultValue
            is Int -> if(dataMap[key]?.type == "int") dataMap[key]?.value?.toInt() as T else defaultValue
            is Float -> if(dataMap[key]?.type == "float") dataMap[key]?.value?.toFloat() as T else defaultValue
            is Long -> if(dataMap[key]?.type == "long") dataMap[key]?.value?.toLong() as T else defaultValue
           else -> defaultValue
       }
    }

    fun getString(key: String, defaultValue: String) = get(key, defaultValue)
    fun getInt(key: String, defaultValue: Int) = get(key, defaultValue)
    fun getFloat(key: String, defaultValue: Float) = get(key, defaultValue)
    fun getLong(key: String, defaultValue: Long) = get(key, defaultValue)

    fun save() {
        val out = fileHandle.write(false)
        try {
            out.write("<?xml version='1.0' encoding='utf-8' standalone='yes' ?>\n".encodeToByteArray())
            out.write("<map>\n".encodeToByteArray())
            dataMap.forEach {
                out.write("${it.value}\n".encodeToByteArray())
            }
            out.write("</map>".encodeToByteArray())
        } finally {
            try {
                out.close()
            } catch (ignored: IOException) {
            }
        }
    }

    private fun load() {
        val iin = fileHandle.reed()
        val dbf = DocumentBuilderFactory.newInstance()
        try {

            val db = dbf.newDocumentBuilder().parse(iin)
            val nodeMap = db.firstChild
            if (nodeMap is Element && nodeMap.tagName == "map") {
                val nodeList = nodeMap.childNodes
                for (i in 0 until nodeList?.length!!) {
                    nodeList.item(i)?.let {
                        if (it.nodeType == Node.ELEMENT_NODE) {
                            val elem = it as Element
                            val key = elem.getAttribute("name")
                            val value = elem.textContent
                            val type = elem.tagName
                            dataMap[key] = DataMap(key, value, type)
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    data class DataMap(val key: String, val value: String, val type: String) {
        override fun toString(): String {
            return "<$type name=\"$key\">$value</$type>"
        }
    }
}