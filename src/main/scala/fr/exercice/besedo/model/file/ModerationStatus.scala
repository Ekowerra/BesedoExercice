package fr.exercice.besedo.model.file

import org.latestbit.circe.adt.codec.JsonTaggedAdt

enum ModerationStatus derives JsonTaggedAdt.PureEncoder:
  case ok, ko, error
