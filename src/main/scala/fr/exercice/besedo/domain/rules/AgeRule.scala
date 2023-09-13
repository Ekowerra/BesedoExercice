package fr.exercice.besedo.domain.rules

import fr.exercice.besedo.domain.error.ModerationRuleError
import fr.exercice.besedo.model.document.Age

trait AgeRule[T <: Age] {
  protected def applyAgeRule: T => Either[ModerationRuleError, T] =
    document => Either.cond(document.age.value >= 18, document, ModerationRuleError.Underage)
}
