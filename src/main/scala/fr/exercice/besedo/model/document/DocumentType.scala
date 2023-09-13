package fr.exercice.besedo.model.document

import org.latestbit.circe.adt.codec.JsonTaggedAdt

enum DocumentType derives JsonTaggedAdt.PureDecoder:
  case classified, profile, message
