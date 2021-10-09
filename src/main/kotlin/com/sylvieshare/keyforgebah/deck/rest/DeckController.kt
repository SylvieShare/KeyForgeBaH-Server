package com.sylvieshare.keyforgebah.deck.rest

import com.sylvieshare.keyforgebah.base.services.HttpService
import com.sylvieshare.keyforgebah.deck.net.DeckByUuidNotFoundException
import com.sylvieshare.keyforgebah.deck.services.DeckService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/deck")
class DeckController @Autowired constructor(
    val httpService: HttpService,
    val deckService: DeckService
) {

    @GetMapping("/add/uuid/{deckUuid}")
    fun addDeckByUuid(
        @PathVariable deckUuid: UUID
    ) {
        val deck = httpService.downloadDeckFromOfficialSite(deckUuid)
        deckService.createDeck(deck)
    }
}
