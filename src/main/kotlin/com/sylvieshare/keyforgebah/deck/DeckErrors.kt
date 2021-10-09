package com.sylvieshare.keyforgebah.deck.net

import com.sylvieshare.keyforgebah.base.ServerException
import java.util.*


class DeckByUuidNotFoundException(deckUuid: UUID) : ServerException("Deck not found", deckUuid)
class CardByIdNotFoundException(cardId: String) : ServerException("Card not found", cardId)