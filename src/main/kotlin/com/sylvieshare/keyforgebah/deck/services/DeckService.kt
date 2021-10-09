package com.sylvieshare.keyforgebah.deck.services

import com.sylvieshare.keyforgebah.deck.dao.CardDao
import com.sylvieshare.keyforgebah.deck.dao.DeckDao
import com.sylvieshare.keyforgebah.deck.dto.Card
import com.sylvieshare.keyforgebah.deck.dto.Deck
import com.sylvieshare.keyforgebah.deck.net.CardByIdNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

interface DeckService {
    fun createDeck(deck: Deck)

    @Throws(CardByIdNotFoundException::class)
    fun getCards(cardsIdList: Collection<String>): Collection<Card>
}

@Component
class DeckServiceImpl @Autowired constructor(
    val deckDao: DeckDao,
    val cardDao: CardDao
) : DeckService {

    override fun createDeck(deck: Deck) {
        deckDao.addDeck(
            deck.uuid,
            deck.name,
            deck.houses.joinToString(VALUE_SEPARATOR),
            deck.cards.joinToString(VALUE_SEPARATOR)
        )
    }

    override fun getCards(cardsIdList: Collection<String>): Collection<Card> {
        return cardsIdList.map { cardId ->
            cardDao.getCard(cardId) ?: throw CardByIdNotFoundException(cardId)
        }
    }

    companion object {
        const val VALUE_SEPARATOR = ","
    }
}