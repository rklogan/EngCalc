package com.example.engcalc

val NUMERALS = arrayOf('0','1','2','3','4','5','6','7','8','9', 'e', 'p')
val OPERATORS = arrayOf('\\','^','*','/','%','+','-') // '\' represents percent; '%' represents modulo
val PARENTHESESE = arrayOf('(',')')
val PRECEDENCES = mapOf('(' to 3, '\\' to 2,'^' to 2, '*' to 1, '/' to 1, '+' to 0, '-' to 0)
val FUNCTIONS = arrayOf("nroot","abs","fact","perm","comb","inv","log","sin","cos","tan","asin","acos","atan")

//converts from infix notation to reverse polish
fun shuntingYard(input: MutableList<String>): MutableList<String>{
    val output = mutableListOf<String>()

    //stack to hold operators that have been shunted
    val stack = mutableListOf<String>()

    for(token in input){
        if(token.lastOrNull() in NUMERALS) output.add(token)//numerals can go straight to the output queue
        else if(token in FUNCTIONS) output.add(token)//functions always go on the stack
        else if(token.lastOrNull() in OPERATORS){

            try{
                //get info about the current operator
                var myPrecedence = PRECEDENCES.get(token.last())
                if(myPrecedence == null) myPrecedence = -1 //unreachable; exists to satisfy compiler

                //peek at the top of the stack
                var onTop = stack.last()
                var stackPrecedence = PRECEDENCES.get(onTop.last())
                if(stackPrecedence == null) stackPrecedence = 4 //a function was found; prevents NPE
                val sp : Int = stackPrecedence  //compiler doesn't want nullable values in comparison

                //repeatedly move items from the stack to the output queue
                while((     onTop in FUNCTIONS
                            || myPrecedence < sp
                            || (stackPrecedence == myPrecedence && onTop != "^"))
                            && onTop != "("){

                    output.add(stack.removeAt(stack.lastIndex))

                    //peek at the top of the stack
                    if(stack.lastIndex > 0){
                        onTop = stack.last()
                        stackPrecedence = PRECEDENCES.get(onTop.last())
                        if(stackPrecedence==null) stackPrecedence = 4   //a function was found; avoid NPE
                    }
                    else break //no more elements to pop
                }
            }catch(NoSuchElementException: Exception){  //something failed. Probably means bug
                return mutableListOf()
            }

            stack.add(token)
        }
        else if(token == "(") stack.add(token)
        else if(token == ")"){
            do{
                //if the stack is ever empty, we have mismatched parenthesese
                if(stack.lastIndex < 0) return mutableListOf()
                output.add(stack.removeAt(stack.lastIndex))
            }while(stack.last() != "(")

            //discard the remaining '('
            stack.removeAt(stack.lastIndex)
        }
    }
    //once all inputs have been processed, we need to empty the stack
    while(stack.lastIndex > 0){
        output.add(stack.removeAt(stack.lastIndex))
    }

    return output
}