package fr.exercice.besedo.model.document

import org.latestbit.circe.adt.codec.JsonTaggedAdt

enum AdCategory derives JsonTaggedAdt.PureDecoder:
  case entertainment, pets, computers, food, miscellaneous
