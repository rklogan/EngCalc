package ec.engcalc

import android.util.Log
import java.lang.ArithmeticException
import java.lang.Math.pow

val NUMERALS = arrayOf('0','1','2','3','4','5','6','7','8','9', 'e', 'p')
val OPERATORS = arrayOf('^','*','/','%','+','-') // '\' represents percent; '%' represents modulo
val PARENTHESESE = arrayOf('(',')')
val PRECEDENCES = mapOf('(' to 3,'^' to 2, '*' to 1, '/' to 1, '+' to 0, '-' to 0)
val FUNCTIONS = arrayOf("nroot","abs","fact","perm","comb","inv","log","sin","cos","tan","asin","acos","atan")


class Expression {
    private var tokens = mutableListOf<String>()
    private var rpn = mutableListOf<String>()
    private var eval: String? = null
    private var parenthesisCount = 0

    /************************Methods for manipulating the data structure externally************************/

    /**
     * Default Constructor assigned no tokens
     */
    fun Expression() {
        setTokens(mutableListOf())
    }

    /**
     * Constructor to initialize the Expression to a MutableList of String tokens
     * @param t A MutableList<String> for the tokens
     */
    fun Expression(t: MutableList<String>) {
        setTokens(t)
    }

    /**
     * Returns the input tokens of the expression
     * @return A mutableList of the strings
     */
    fun getTokens(): MutableList<String> {
        return this.tokens
    }

    /**
     * Returns the Reverse Polish (postfix) notation for the expression
     * @return A MutableList of the postfix notation
     */
    fun getRPN(): MutableList<String> {
        return this.rpn
    }

    /**
     * Returns the evaluation of the expression
     * @return A String containing the evaluation
     */
    fun getEval(): String? {
        return this.eval
    }

    /**
     * Return the parenthesis count of the expression
     */
    fun getParenthesisCount(): Int {
        return this.parenthesisCount
    }

    /**
     * Assigns a new list to tokens and updates all parameters
     * @param t The new list of tokens in infix notation
     */
    fun setTokens(t: MutableList<String>) {
        this.tokens = t
        update()
    }

    /**
     * Appends a new token to the list and updates all parameters
     * @param token The token to be added to the list
     */
    fun appendToken(token: String) {
        tokens.add(token)
        setTokens(tokens)
    }

    /**
     * Function to clear the tokens in the expression
     */
    fun clearTokens(){
        tokens.clear()
        rpn.clear()
        eval = "0"
    }

    /**
     * checks if the expression is a percentage
     * @return Returns true if the expression is a percentage expression
     */
    fun isPercentage() : Boolean {
        if(tokens.size == 3){
            if(tokens [2] == "100%") return true
        }
        return false
    }

    /****************************Methods for manipulating the data structure internally******************************/

    /**
     * A private helper function to update parameters when tokens has been changed
     */
    private fun update(){
        rpn = shuntingYard(tokens)
        eval = computeFromRPN(rpn)
        parenthesisCount = 0
        for(t in tokens){
            if(t=="(") parenthesisCount++
            if(t==")") parenthesisCount--
        }
    }

    /**
     *  converts from infix notation to reverse polish
     *  @param input A Mutable list of tokens in infix order
     *  @return A MutableList of tokens in Reverse Polish (postfix) notation
     */
    private fun shuntingYard(input: MutableList<String>): MutableList<String> {

        val output = mutableListOf<String>()

        if(isPercentage()){
            output.add(tokens[0])
            output.add("100")
            output.add(tokens[1])
            return output
        }

        //stack to hold operators that have been shunted
        val stack = mutableListOf<String>()


        for (token in input) {
            if (token.lastOrNull() in NUMERALS) output.add(token)//numerals can go straight to the output queue
            else if (token.lastOrNull() == '.') output.add(token + "0")
            else if (token in FUNCTIONS) output.add(token)//functions always go on the stack
            else if (token.lastOrNull() in OPERATORS) {
                try {
                    //get info about the current operator
                    var myPrecedence = PRECEDENCES.get(token.last())
                    if (myPrecedence == null) myPrecedence = -1 //unreachable; exists to satisfy compiler

                    //peek at the top of the stack
                    var onTop = stack.lastOrNull()
                    if (onTop != null) {
                        var stackPrecedence = PRECEDENCES.get(onTop.last())
                        if (stackPrecedence == null) stackPrecedence = 4 //a function was found; prevents NPE
                        val sp: Int = stackPrecedence  //compiler doesn't want nullable values in comparison

                        //repeatedly move items from the stack to the output queue
                        while ((onTop in FUNCTIONS
                                    || myPrecedence < sp
                                    || (sp == myPrecedence && onTop != "^"))
                            && onTop != "("
                        ) {

                            output.add(stack.removeAt(stack.lastIndex))

                            //peek at the top of the stack
                            if (stack.lastIndex > 0) {
                                onTop = stack.lastOrNull()
                                stackPrecedence = PRECEDENCES.get(onTop?.lastOrNull())
                                if (stackPrecedence == null) stackPrecedence = 4   //a function was found; avoid NPE
                            } else break //no more elements to pop
                        }
                    }
                } catch (NoSuchElementException: Exception) {  //something failed. Probably means bug
                    return mutableListOf()
                }

                stack.add(token)
            } else if (token == "(") stack.add(token)
            else if (token == ")") {
                //move tokens on the stack to output until we find the matching (
                while (stack.lastOrNull() != "(") {
                    val idx = stack.lastIndex

                    //if the stack is ever empty, we have mismatched parenthesese
                    if (idx < 0) return mutableListOf()

                    output.add(stack.removeAt(idx))
                }

                //discard the remaining '('
                stack.removeAt(stack.lastIndex)
            }
        }
        //once all inputs have been processed, we need to empty the stack
        if (stack.isNotEmpty()) {
            output.addAll(stack.reversed())
        }
        while ("(" in output) output.remove("(")

        return output
    }

    /**
     * Helper Function to use a MutableList as a stack
     * @param input The MutableList being used as a stack
     * @return The last element in the MutableList
     */
    private fun popMutableList(input: MutableList<String>): String? {
        val rv = input.lastOrNull()
        if (!input.isEmpty()) input.removeAt(input.size - 1)
        return rv
    }

    /**
     * Helper Function to perform operations on tokens
     * @param a A string containing the first numerical value (int or double)
     * @param b A string contianing the second numerical value (int or double)
     * @param operator A Char representation of the operator [^,*,/,%,+,-]
     * @return A String containing the result of the operation
     */
    private fun doBasicArithmetic(a: String?, b: String?, operator: Char): String {
        //get the int and double value for each input
        var x_int = a?.toIntOrNull()
        var x_dbl = a!!.toDouble()
        var y_int = b?.toIntOrNull()
        var y_dbl = b!!.toDouble()

        when (operator) {
            '^' -> return pow(x_dbl, y_dbl).toString()
            '*' -> {
                //decide whether to do integer or floating operation. This code is repeated for all operation below
                //except '/'
                if (x_int != null && y_int != null) return (x_int * y_int).toString()
                else if (x_int != null && y_int == null) return (x_int * y_dbl).toString()
                else if (x_int == null && y_int != null) return (x_dbl * y_int).toString()
                else return (x_dbl * y_dbl).toString()
            }
            '/' -> {
                if (y_int == 0 || y_dbl == 0.0) {
                    throw ArithmeticException()
                }
                //We only do integer division if the numbers divide evenly
                else if (x_int != null && y_int != null && x_int % y_int == 0) return (x_int / y_int).toString()
                else if (x_int != null && y_int == null) return (x_int / y_dbl).toString()
                else if (x_int == null && y_int != null) return (x_dbl / y_int).toString()
                else return (x_dbl / y_dbl).toString()
            }
            '%' -> {
                if (x_int != null && y_int != null) return (x_int % y_int).toString()
                else if (x_int != null && y_int == null) return (x_int % y_dbl).toString()
                else if (x_int == null && y_int != null) return (x_dbl % y_int).toString()
                else return (x_dbl % y_dbl).toString()
            }
            '+' -> {
                if (x_int != null && y_int != null) return (x_int + y_int).toString()
                else if (x_int != null && y_int == null) return (x_int + y_dbl).toString()
                else if (x_int == null && y_int != null) return (x_dbl + y_int).toString()
                else return (x_dbl + y_dbl).toString()
            }
            '-' -> {
                if (x_int != null && y_int != null) return (x_int - y_int).toString()
                else if (x_int != null && y_int == null) return (x_int - y_dbl).toString()
                else if (x_int == null && y_int != null) return (x_dbl - y_int).toString()
                else return (x_dbl - y_dbl).toString()
            }
        }
        throw IllegalArgumentException() //the arguments passed in were invalid
    }

    /**
     * computeFromRPN computes the result of an equation in reverse polish notation
     * @param input A MutableList of Strings that represent the equation in reverse polish (postfix) notation
     * @return A String containing the results of the calculation or null if the equation was malformed
     */
    private fun computeFromRPN(input: MutableList<String>): String? {
        if(input.isEmpty()) return eval

        //stack for numbers that have not been operated on yet
        var lhs = mutableListOf<String>()

        for (token in input) {
            val c = token.lastOrNull()
            if (c in NUMERALS) lhs.add(token)   //numbers always get added to the stack
            else if (token.lastOrNull() in OPERATORS) {
                val b = popMutableList(lhs)
                val a = popMutableList(lhs)
                if (a == null || b == null) return null      //The expression is malformed

                //compute the result and add it to lhs
                try {
                    lhs.add(doBasicArithmetic(a, b, token.last()))
                } catch (IllegalArgumentException: IllegalArgumentException) {
                    return null
                } catch (ArithmeticException: ArithmeticException) {
                    return "/0"
                } //sentinel value for divide by zero
            }
        }


        return lhs[0]
    }

    /**
     * Removes percentages from the expression
     */
    private fun removePercentage(){
        if(isPercentage()){
            tokens[2] = tokens[2].dropLast(1)
            update()
            tokens.clear()
            tokens.add(eval.toString())
            update()
        }
    }

    /***************************Logic For External Button Presses****************************************************/

    /**
     * Logic for the onClickListeners for the entry of numerals
     * @param token A String representation of the digit
     */
    fun numericalButtonPress(token: String) {
        val prevToken = tokens.lastOrNull()
        val prevChar = prevToken?.lastOrNull()
        if(!isPercentage()) {   //we can't append to percentages
            if (prevChar in NUMERALS || prevChar == '.') {
                val temp = prevToken.plus(token)
                tokens.removeAt(tokens.lastIndex)
                tokens.add(temp)
            } else tokens.add(token)
        }
        update()

    }

    /**
     * Logic for the onClickListener for Operator Buttons
     * @param token A String representation of the operator
     */
    fun operatorButtonPress(token:String) {
        removePercentage()

        val prevToken = tokens.lastOrNull()
        //An expression can't start with an operator
        if (prevToken == null || prevToken == "(") return

        //check that we don't have two operators in a row
        val prevChar = prevToken.lastOrNull()
        if (prevChar in OPERATORS) return

        tokens.add(token)
        update()
    }

    /**
     * Logic for the onClickListener of the () Button
     */
    fun parenthesisButton(){
        removePercentage()

        Log.d("PBtn",tokens.joinToString(separator = " "))

        val prevToken = tokens.lastOrNull()
        if(prevToken == null ||                 //First Character
            (prevToken.last() !in NUMERALS && prevToken.last() !in PARENTHESESE)
            || prevToken.last() == '('){
            //The first character of the input and any parenthesis following an operator or function
            //must be (
            tokens.add("(")
            parenthesisCount++
        }
        else if(parenthesisCount==0 || prevToken == ")"){
            //If no opening parenthesis already exist, or the previous token was a parenthesis
            // we need to insert '* (' to clean up for the interpereter
            tokens.add("*")
            tokens.add("(")
            parenthesisCount++

        }
        else{
            //in any other case just try to close parenthesese
            assert(--parenthesisCount >= 0)
            tokens.add(")")
        }
        rpn = shuntingYard(tokens)
        eval = computeFromRPN(rpn)
    }

    /**
     * Logic for the onClickListener of the +/- button
     */
    fun signButton(){
        removePercentage()

        val prevToken = tokens.lastOrNull()
        val prevChar = prevToken?.lastOrNull()

        //+/- is only applied to numbers
        if(prevChar in NUMERALS || prevChar == '.'){
            //if it's already negative, make it positive
            if(prevToken?.firstOrNull() == '-'){
                val temp = prevToken.drop(1)
                tokens.removeAt(tokens.lastIndex)
                tokens.add(temp)
            }
            else{   //otherwise add a - sign
                val temp = "-".plus(prevToken)
                tokens.removeAt(tokens.lastIndex)
                tokens.add(temp)
            }
        }
        rpn = shuntingYard(tokens)
        eval = computeFromRPN(rpn)
    }

    /**
     * Logic for the onClickListener of the . Button
     */
    fun decimalButton(){
        removePercentage()

        try{
            val prevToken = tokens.last()
            val prevChar = prevToken.last()

            //we add a decimal point if the previous token was a number that didn't have one already
            if(prevChar in NUMERALS && !prevToken.contains('.')){
                val temp = prevToken.plus('.')
                tokens.removeAt(tokens.lastIndex)
                tokens.add(temp)
            }
        }catch(NoSuchElementException: java.lang.Exception){
            tokens.add("0.")
        }   //This catch means tokens was empty

        rpn = shuntingYard(tokens)
        eval = computeFromRPN(rpn)
    }

    /**
     * Logic for the onClickListener of the PercentButton
     */
    fun percentButton(){
        if(eval != null) {
            tokens = mutableListOf(eval.toString(), "/", "100%")
            rpn = shuntingYard(tokens)
            eval = computeFromRPN(rpn)
            parenthesisCount = 0
        }
    }
}