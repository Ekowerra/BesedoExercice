package fr.exercice.besedo.model.file

import fr.exercice.besedo.domain.error.ModerationRuleError
import org.latestbit.circe.adt.codec.JsonTaggedAdt

enum RejectReason derives JsonTaggedAdt.PureEncoder:
  case underage, scam, contact, nonsense

object RejectReason {
  def fromModerationRuleError: ModerationRuleError => RejectReason = {
    case ModerationRuleError.Contact  => contact
    case ModerationRuleError.Scam     => scam
    case ModerationRuleError.Underage => underage
    case ModerationRuleError.Nonsense => nonsense
  }
}
