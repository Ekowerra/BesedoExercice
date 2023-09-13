# Besedo Scala test
​
You're tasked with writing a program that implements a simplified version of an automated moderation process. The
general idea is that:
​
* documents are fed to your program.
* moderation rules are applied to each document to decide whether it should be accepted or rejected and, if the later,
  why.
* decisions are written back for further processing.
​
​
​
## Input Files
​
Input files are JSON formatted, and contain, at the top level, an object of type `Batch`.
​
​
​
### `Batch`
​
An object of type `Batch` represents a set of 1 to 30 documents submitted for moderation purposes. It is composed of the
following fields:
​
* `id`: batch's unique identifier (as a `String`, composed of alphanumeric characters, cannot be empty)
* `content`: an array of objects of type `Document` (length must be between 1 and 30, inclusive)

​
If either the `id` or `documents` field is ill-formed, its' fine to quit with an error message of some sort.
​
​
​
### `Document`
​
An object of type `Document` describes the content of a document submitted for moderation. Our mock platform supports
three kinds of documents:
​
* classified ads
* online dating profiles
* private user-to-user messages
​

An object of type `Document` may contain different fields depending on its kind:
​
* `id`: unique document identifier (as a `String`, composed of alphanumeric characters, cannot be empty). Applies to
  classified ads, online dating profiles and private messages.
* `type`: describes the type of the document (one of `classified`, `profile` or `message`).
* `author`: unique author identifier (as a `String`, composed of alphanumeric characters, cannot be empty). Applies to
  classified ads, online dating profiles and private messages.
* `category`: category to which a classified's goods belong (a value of type `category`). Applies to classified ads
  only.
* `body`: textual body of the document (as a `String`, cannot be empty or composed of whitespace only). Applies to
  classified ads, online dating profiles and private messages.
* `price`: price of a good, in EUR (as a `Float`, must be strictly greater than `0`). Applies to classified ads
  only.
* `gender`: gender of the document's author (as a value of type `gender`). Applies to online profiles only.
* `seeks`: gender of the partners sought by the document's author (as a value of type `gender`). Applies to online
  profiles only.
* `age`: age of the document's author (as a value of type `Int`, must be strictly greater than `0`). Applies to online
  profiles only.
* `subject`: optional short textual description of the document (as a value of type `String`, if present cannot be
  empty of composed only of whitespace). Applies to online profiles and private messages only.
* `to`: unique identifier of the person a document is meant for (as a `String`, composed of alphanumeric characters,
  cannot be empty). Applies to direct messages only.

​
If `id` is ill-formed, it's fine to quit with an error message of some sort.

​
If any other field is ill-formed, the document must be flagged with an error status, but other, valid documents in the
batch must still be processed.
​
​
​
### `gender`
​
A value of type `gender` must be one of:
​
* `male`
* `female`
​
​
​
### `category`
​
A value of type `category` must be one of:
​
* `entertainment`
* `pets`
* `computers`
* `food`
* `miscellaneous`
​
​
​
## Moderation rules
​
If a document is well formed, moderation rules are applied to its various fields, as described below.
​
​
​
### `price`
​
A document must be rejected if its price doesn't meet the following, category-specific (and fairly arbitrary) rules:
​
* `entertainment`: `1F < price < 1000F`
* `pets`: `500F < price < 1000F`
* `computers`: `100F < price < 3500F`
* `food`: `10F < price < 200F`
* `miscellaneous`: `1F < price < 100F`
​
​
​
### `body` and `subject`
​
Textual parts cannot contain:
​
* e-mail adresses (it's sufficient to test for well-formed emails)
* URLs (it's sufficient to test for well-formed URLs)

​
Additionally, if `c` is the number of consonants in the document and `v` the number of vowels, the following must hold
true: `min < c / (v + c) < max`, where:
​
* for classifieds: `min = 0.1F` and `max = 0.84F`
* for online profiles: `min = 0.1F` and `max = 0.84F`
* for private messages: `min = 0.2F` and `max = 0.76F`
​
​
​
### `age`
​
It must always be greater than or equal to `18`.
​
​
​
## Output Files
​
After a batch has been fully processed, the moderation output must be stored in a JSON file that has a top-level element
of type `ModeratedBatch`.
​
​
​
### `ModeratedBatch`
​
An object of type `ModeratedBatch` is composed of the following fields:
​
* `id`: same value as the `id` field of the input `Batch` object.
* `content`: an array of values of type `ModerationResult`, one for each input `Document`.
​
​
​
### `ModerationResult`
​
An object of type `ModerationResult` is composed of the following fields:
​
* `id`: same value as the `id` field of the corresponding `Document`
* `status`: value of type `moderationStatus` describing the moderation decision.
* `reason`: if `status` describes a rejection, reason for which the document was rejected (value of type `rejectReason`).
​
​
​
### `moderationStatus`
​
A value of type `moderationStatus` can be one of:
​
* `ok`: the document is accepted
* `ko`: the document is rejected
* `error`: the document was not well-formed
​
​
​
### `rejectReason`
​
A value of type `rejectReason` can be one of:
​
* `underage`: if the age is smaller than `18`
* `scam`: if the price falls outside of the allowed range for the category
* `contact`: if `body` or `subject` contain either an e-mail or an URL
* `nonsense`: if the ratio of consonants and vowels is not valid
​
These values are ranked by order of importance - that is, if, for a given document, more than one reason applies, then
`underage` should be prefered if present, then `scam`, then `contact` and finally `nonsense`.
​
​
## Important notes
​
JSON shouldn't be parsed manually. You're free to use whatever library you wish to do parsing / serialisation.
​
As much as possible (and reasonable), use the type system to enforce invariants - some moderation rules differ depending
on the kind of documents they're applied to, for example, and an ideal solution would guarantee at compile time that
the right rule must be applied to the right document type.
​​
If you feel any of this assignment's requirements doesn't make sense or is too time consuming, feel free to disregard
it (and document why). It's better to submit a partial result that you're happy with than a complete project poorly done
because it was rushed.