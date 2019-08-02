package com.example.engcalc

import android.app.ActivityManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import java.lang.StringBuilder
import android.widget.Button
import android.widget.Toast
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.fragment_simple_calc.*
import java.lang.Exception



/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [SimpleCalc.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [SimpleCalc.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class SimpleCalc : Fragment() {
    val tokens = mutableListOf<String>()
    var parenthesisCount = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_simple_calc, container, false)

        //setup click listeners for numerical buttons
        val zero_button: Button = view.findViewById(R.id.zero_button)
        zero_button.setOnClickListener{numericalButtonPress("0")}
        val one_button: Button = view.findViewById(R.id.one_button)
        one_button.setOnClickListener{numericalButtonPress("1")}
        val two_button: Button= view.findViewById(R.id.two_button)
        two_button.setOnClickListener{ numericalButtonPress("2")}
        val three_button: Button = view.findViewById(R.id.three_button)
        three_button.setOnClickListener{ numericalButtonPress("3")}
        val four_button: Button = view.findViewById(R.id.four_button)
        four_button.setOnClickListener{numericalButtonPress("4")}
        val five_button: Button = view.findViewById(R.id.five_button)
        five_button.setOnClickListener{numericalButtonPress("5")}
        val six_button: Button = view.findViewById(R.id.six_button)
        six_button.setOnClickListener{numericalButtonPress("6")}
        val seven_button: Button = view.findViewById(R.id.seven_button)
        seven_button.setOnClickListener{numericalButtonPress("7")}
        val eight_button: Button = view.findViewById(R.id.eight_button)
        eight_button.setOnClickListener{numericalButtonPress("8")}
        val nine_button: Button = view.findViewById(R.id.nine_button)
        nine_button.setOnClickListener{numericalButtonPress("9")}

        //set up listerners for operator buttons
        val multiply_button: Button = view.findViewById(R.id.multiply_button)
        multiply_button.setOnClickListener{operatorButtonPress("*")}
        val divide_button: Button = view.findViewById(R.id.divide_button)
        divide_button.setOnClickListener{operatorButtonPress("/")}
        val add_button: Button = view.findViewById(R.id.add_button)
        add_button.setOnClickListener{operatorButtonPress("+")}
        val subtract_button: Button = view.findViewById(R.id.subtract_button)
        subtract_button.setOnClickListener{operatorButtonPress("-")}
        val percent_button: Button = view.findViewById(R.id.percent_button)
        percent_button.setOnClickListener{operatorButtonPress("\\")}


        //setup clear button
        val clear_button: Button = view.findViewById(R.id.clear_button)
        clear_button.setOnClickListener{
            tokens.clear()
            drawInput()
            drawOutput()
        }

        //setup parenthesis button
        val parenthesis_button: Button = view.findViewById(R.id.parenthesis_button)
        parenthesis_button.setOnClickListener{
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
            drawInput()
            drawOutput()
        }

        //setup plus/minus button
        val sign_button: Button = view.findViewById(R.id.sign_button)
        sign_button.setOnClickListener{

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

            drawInput()
            drawOutput()
        }

        //setup decimal button
        val decimal_button: Button = view.findViewById(R.id.decimal_button)
        decimal_button.setOnClickListener{
            try{
                val prevToken = tokens.last()
                val prevChar = prevToken.last()

                //we add a decimal point if the previous token was a number that didn't have one already
                if(prevChar in NUMERALS && !prevToken.contains('.')){
                    val temp = prevToken.plus('.')
                    tokens.removeAt(tokens.lastIndex)
                    tokens.add(temp)
                }
            }catch(NoSuchElementException: Exception){
            }   //This catch means tokens was empty
            finally {
                drawInput()
                drawOutput()
            }

        }

        // Inflate the layout for this fragment
        return view
    }

    /**
     * Logic for the onClickListeners for the entry of numerals
     * @param token A String representation of the digit
     */
    fun numericalButtonPress(token:String){
        val prevToken = tokens.lastOrNull()
        val prevChar = prevToken?.lastOrNull()
        if(prevChar in NUMERALS || prevChar == '.'){
            val temp = prevToken.plus(token)
            tokens.removeAt(tokens.lastIndex)
            tokens.add(temp)
        }
        else tokens.add(token)

        drawInput()
        drawOutput()
    }

    /**
     * Logic for the onClickListener for Operator Buttons
     * @param token A String representation of the operator
     */
    fun operatorButtonPress(token:String){
        val prevToken = tokens.lastOrNull()
        //An expression can't start with an operator
        if(prevToken == null) return

        //check that we don't have two operators in a row
        val prevChar = prevToken.lastOrNull()
        if(prevChar in OPERATORS) return

        tokens.add(token)

        drawInput()
        drawOutput()
    }

    fun drawInput(){
        if(tokens.isNotEmpty()) equation_area.setText(tokens.joinToString(separator = " "))
        else equation_area.setText("0")
    }

    fun drawOutput(){
        //answer_area.setText(shuntingYard(tokens).joinToString(separator = " "))
        val rpn = shuntingYard(tokens)
        if (rpn.isNotEmpty()){
            val output = computeFromRPN(rpn)
            Log.d("output", output.toString())
            if(output == "/0") answer_area.setText("")
            else if(output != null) answer_area.setText(output)
        }
        if(tokens.isEmpty()) answer_area.setText("0")
    }
}
