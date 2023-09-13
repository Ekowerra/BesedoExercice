# Besedo Scala Exercice
​
This is my implementation of the BESEDO exercice.
## Libraries 
​
* I choose to use `Circe` for encoding and decoding JSON. It comes with some other libraries to work with types, enums and streams.
* I choose to use `Cats/Cats-Effects` for some functional programming function and deal with IO operations.
* I choose to use `FS2` for streaming and working with files.
* I choose to use `Refined` to implements the data format rules.
* I choose to use `ScalaTest` to write my unit tests.

## Approach
I made moderation functions based on the properties of the documents and create specific moderation checker for each type of document. Thus, everything is composable and so having new types of documents or changing rules have limited impact on the code.

## Run the code
You'll need to set the 2 environment variables: INPUT_FILE (which is the path of the file you want to use) and OUTPUT_DIRECTORY (which is the path of directory where you want the result of the execution). Then you can run the command `sbt run` to launch the code.

## A question
I did not check the unicity of the ids, I thought this was not something that I had to implement. If I had to, I would implement a database to store the ids and try to find them right after reading. If I find one already used  I would stop the stream.