package com.sylvieshare.keyforgebah.base.services

import com.fasterxml.jackson.databind.ObjectMapper
import com.sylvieshare.keyforgebah.deck.dto.Deck
import com.sylvieshare.keyforgebah.deck.net.DeckByUuidNotFoundException
import org.springframework.stereotype.Component
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.FileNotFoundException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.util.*


interface HttpService {
    fun downloadDeckFromOfficialSite(deckUuid: UUID): Deck
}

@Component
class HttpServiceImpl : HttpService {

    val mapper = ObjectMapper()

    override fun downloadDeckFromOfficialSite(deckUuid: UUID): Deck {
        val res = try {
            get("https://www.keyforgegame.com/api/decks/$deckUuid?links=cards")
        } catch (ex: FileNotFoundException) {
            throw throw DeckByUuidNotFoundException(deckUuid)
        }
        val node = mapper.readTree(res.toByteArray())
        val name = node.get("data").get("name").textValue()
        val houses = node.get("data").get("_links").get("houses").map { it.textValue() }
        val cards = node.get("_linked").get("cards").map {
            cardNameToId(it.get("card_title").textValue())
        }
        return Deck(deckUuid, name, houses, cards)
    }

    private fun get(urlStr: String, params: Map<Any, Any> = mapOf()): String {
        val url = URL(urlStr)
        val connection = url.openConnection() as HttpURLConnection

//        connection.requestMethod = "GET"

        val postData = StringBuilder()
        params.forEach { (key, value) ->
            if (postData.isNotEmpty()) {
                postData.append('&')
            }
            postData.append(URLEncoder.encode(key.toString(), "UTF-8"))
            postData.append('=')
            postData.append(URLEncoder.encode(value.toString(), "UTF-8"))
        }

        val postDataBytes = postData.toString().toByteArray(charset("UTF-8"))
        connection.doOutput = true
        try {
            DataOutputStream(connection.outputStream).use { writer ->
                writer.write(postDataBytes)
                writer.flush()
                writer.close()
                var content: StringBuilder
                BufferedReader(
                    InputStreamReader(connection.inputStream)
                ).use { `in` ->
                    var line: String?
                    content = StringBuilder()
                    while (`in`.readLine().also { line = it } != null) {
                        content.append(line).append(System.lineSeparator())
                    }
                }
                return content.toString()
            }
        } finally {
            connection.disconnect()
        }
    }

    companion object {
        private fun cardNameToId(cardName: String) = cardName
            .lowercase()
            .replace("'", "")
            .replace("\"", "")
            .replace("-", "")
            .replace(' ', '-')
    }
}