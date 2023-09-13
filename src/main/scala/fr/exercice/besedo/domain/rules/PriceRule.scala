package fr.exercice.besedo.domain.rules

import fr.exercice.besedo.domain.error.ModerationRuleError
import fr.exercice.besedo.model.document.{AdCategory, Price}

trait PriceRule[T <: Price] {
  protected def applyPriceRule: T => Either[ModerationRuleError, T] = document =>
    val (minPrice, maxPrice) = document.category match
      case AdCategory.entertainment => (1f, 1000f)
      case AdCategory.pets          => (500f, 1000f)
      case AdCategory.computers     => (100f, 3500f)
      case AdCategory.food          => (10f, 200f)
      case AdCategory.miscellaneous => (1f, 100f)

    Either.cond(document.price.value > minPrice && document.price.value < maxPrice, document, ModerationRuleError.Scam)
}
