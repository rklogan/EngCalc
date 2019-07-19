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

val NUMERALS = arrayOf('0','1','2','3','4','5','6','7','8','9')
val OPERATORS = arrayOf('^','*','/','%','+','-')

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
        equation_area.setText(tokens.joinToString(separator = " "))
    }

    fun drawOutput(){


    }
}
