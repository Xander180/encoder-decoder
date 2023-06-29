package encryptdecrypt

import java.io.File

val path: String = System.getProperty("user.dir")
val separator: String = File.separator // Uses "/" or "\" depending on OS
var mode = "enc" // Default is encode if anything other than decode is set
var data = ""
var key = 0
var alg = "shift" // Default is shift if anything other than unicode is set
var inFileName = ""
var outFileName = ""


fun decryptEncrypt() {
    val message: String //Default is data arg if data arg exists, regardless of if input file exists
    val fileText: String
    val inFile = File("$path$separator$inFileName")
    val outFile = File("$path$separator$outFileName")
    if (outFileName != "") {
        outFile.writeText("") // Creates output file in order to be writeable
    }

    message = if (data != "" && inFile.exists() || !inFile.exists()) {
        shift(data)
    } else {
        fileText = inFile.readText()
        shift(fileText)
    }

    if (outFile.exists()) outFile.writeText(message) else println(message) //Prints message to console if output file does not exist
}

fun shift(data: String): String {
    var message = ""
    data.trim().forEach {
        var ch = it
        when (alg) {
            "unicode" -> {
                if (mode == "dec") ch -= key else ch += key
            }

            else -> {
                for (i in 1..key) {
                    if (mode == "dec" && ch.isUpperCase()) {
                        ch--
                        if (ch < 'A') ch = 'Z'
                    } else if (mode == "dec" && ch.isLowerCase()) {
                        ch--
                        if (ch < 'a') ch = 'z'
                    }
                    if (mode == "enc" && ch.isUpperCase()) {
                        ch++
                        if (ch > 'Z') ch = 'A'
                    } else if (mode == "enc" && ch.isLowerCase()) {
                        ch++
                        if (ch > 'z') ch = 'a'
                    } else if (!ch.isLetter()) ch = it
                }
            }
        }
        message += ch
    }
    return message
}

fun main(args: Array<String>) {
    for (element in args) {
        when (element) {
            "-mode" -> mode = args[args.indexOf("-mode") + 1]
            "-data" -> data = args[args.indexOf("-data") + 1]
            "-key" -> key = args[args.indexOf("-key") + 1].toInt()
            "-in" -> inFileName = args[args.indexOf("-in") + 1]
            "-out" -> outFileName = args[args.indexOf("-out") + 1]
            "-alg" -> alg = args[args.indexOf("-alg") + 1]
        }
    }
    decryptEncrypt()
}