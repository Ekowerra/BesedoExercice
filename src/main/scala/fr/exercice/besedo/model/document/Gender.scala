package fr.exercice.besedo.model.document

import org.latestbit.circe.adt.codec.JsonTaggedAdt

enum Gender derives JsonTaggedAdt.PureDecoder:
  case male, female
