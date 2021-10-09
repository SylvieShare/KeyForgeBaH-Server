package com.sylvieshare.keyforgebah.deck.rest

import com.sylvieshare.keyforgebah.deck.dto.Card
import com.sylvieshare.keyforgebah.deck.net.DeckByUuidNotFoundException
import com.sylvieshare.keyforgebah.base.Response
import com.sylvieshare.keyforgebah.deck.services.DeckService
import com.sylvieshare.keyforgebah.base.services.HttpService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/deck")
class CardController @Autowired constructor(
    val deckService: DeckService
) {

    @PostMapping("/cards")
    fun getCardsInfo(
        @RequestBody cardIdList: Collection<String>,
    ): Response<Collection<Card>> {
//        val cardIdList = bodyGetCardList.cards
        val cardList = deckService.getCards(cardIdList)
        return Response(cardList)
    }
}
