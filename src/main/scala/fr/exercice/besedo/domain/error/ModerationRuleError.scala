package fr.exercice.besedo.domain.error

sealed trait ModerationRuleError

object ModerationRuleError {
  case object Scam extends ModerationRuleError

  case object Underage extends ModerationRuleError

  case object Contact extends ModerationRuleError

  case object Nonsense extends ModerationRuleError

}
