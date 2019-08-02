package com.example.engcalc

import android.util.Log
import java.lang.ArithmeticException
import java.lang.Exception
import java.lang.Math.pow


/**
 * Helper Function to use a MutableList as a stack
 * @param input The MutableList being used as a stack
 * @return The last element in the MutableList
 */
fun popMutableList(input: MutableList<String>) : String? {
    val rv = input.lastOrNull()
    if (!input.isEmpty()) input.removeAt(input.size-1)
    return rv
}

/**
 * Helper Function to perform operations on tokens
 * @param a A string containing the first numerical value (int or double)
 * @param b A string contianing the second numerical value (int or double)
 * @param operator A Char representation of the operator [^,*,/,%,+,-]
 * @return A String containing the result of the operation
 */
fun doBasicArithmetic(a : String?, b:String?, operator: Char) : String{
    //get the int and double value for each input
    var x_int = a?.toIntOrNull()
    var x_dbl = a!!.toDouble()
    var y_int = b?.toIntOrNull()
    var y_dbl = b!!.toDouble()

    when(operator){
        '^'-> return pow(x_dbl, y_dbl).toString()
        '*'-> {
            //decide whether to do integer or floating operation. This code is repeated for all operation below
            //except '/'
            if(x_int != null && y_int != null) return (x_int * y_int).toString()
            else if(x_int != null && y_int == null) return (x_int * y_dbl).toString()
            else if(x_int == null && y_int != null) return (x_dbl * y_int).toString()
            else return (x_dbl * y_dbl).toString()
        }
        '/'-> {
            if(y_int == 0 || y_dbl == 0.0){
                throw ArithmeticException()
            }
            //We only do integer division if the numbers divide evenly
            else if(x_int != null && y_int != null && x_int % y_int == 0) return (x_int / y_int).toString()
            else if(x_int != null && y_int == null) return (x_int / y_dbl).toString()
            else if(x_int == null && y_int != null) return (x_dbl / y_int).toString()
            else return (x_dbl / y_dbl).toString()
        }
        '%'-> {
            if(x_int != null && y_int != null) return (x_int % y_int).toString()
            else if(x_int != null && y_int == null) return (x_int % y_dbl).toString()
            else if(x_int == null && y_int != null) return (x_dbl % y_int).toString()
            else return (x_dbl % y_dbl).toString()
        }
        '+'->{
            if(x_int != null && y_int != null) return (x_int + y_int).toString()
            else if(x_int != null && y_int == null) return (x_int + y_dbl).toString()
            else if(x_int == null && y_int != null) return (x_dbl + y_int).toString()
            else return (x_dbl + y_dbl).toString()
        }
        '-'->{
            if(x_int != null && y_int != null) return (x_int - y_int).toString()
            else if(x_int != null && y_int == null) return (x_int - y_dbl).toString()
            else if(x_int == null && y_int != null) return (x_dbl - y_int).toString()
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
fun computeFromRPN(input : MutableList<String>) : String?{
    //stack for numbers that have not been operated on yet
    var lhs = mutableListOf<String>()

    for(token in input){
        if(token.lastOrNull() in NUMERALS) lhs.add(token)   //numbers always get added to the stack
        else if (token.lastOrNull() in OPERATORS){
            val b = popMutableList(lhs)
            val a = popMutableList(lhs)
            if(a == null || b == null) return null      //The expression is malformed

            //compute the result and add it to lhs
            try{
                lhs.add(doBasicArithmetic(a,b,token.last()))
            }
            catch (IllegalArgumentException : IllegalArgumentException){ return null }
            catch (ArithmeticException : ArithmeticException){ return "/0" } //sentinel value for divide by zero
        }
    }


    return lhs[0]
}