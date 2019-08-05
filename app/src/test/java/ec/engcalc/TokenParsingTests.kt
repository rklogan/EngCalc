package ec.engcalc


import org.junit.Test

/**
 * "nroot","abs","fact","perm","comb","inv","log","sin","cos","tan",
 * "asin","acos","atan")

 */

/**
 * Helper function to convert a space delimited string of tokens into a MutableList
 * @param input The string to be converted
 * @return A MutableList of the tokens
 */
fun tokenizeString(input : String) : MutableList<String>{
    var output = mutableListOf<String>()
    for( token in input.split(" ")){
        output.add(token)
    }
    return output
}

class TokenParsingTests {
    /**
     * Helper function to convert a space delimited string of tokens into a MutableList
     * @param input The string to be converted
     * @return A MutableList of the tokens
     */
    fun tokenizeString(input : String) : MutableList<String>{
        var output = mutableListOf<String>()
        for( token in input.split(" ")){
            output.add(token)
        }
        return output
    }

    @Test
    fun shuntingYard_isCorrect(){
        val simpleCalc_testCases = arrayOf("3 / 4 + 5 / 2", "9 - 4","7 / 6 - 1 / 5","6 - 4 + 4 * 8","8 ^ 2 + 7 / 5 * 7")
        val simpleCalc_testResults = arrayOf("3 4 / 5 2 / +","9 4 -","7 6 / 1 5 / -","6 4 - 4 8 * +","8 2 ^ 7 5 / 7 * +")

        for(i in 0..simpleCalc_testCases.size-1){
            assert(shuntingYard(tokenizeString(simpleCalc_testCases[i]))
                    == tokenizeString(simpleCalc_testResults[i]))
        }
    }
}