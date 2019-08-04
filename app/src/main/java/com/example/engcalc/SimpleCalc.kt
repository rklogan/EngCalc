package com.example.engcalc

import android.app.ActivityManager
import android.content.Context
import android.media.Image
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
import android.widget.ImageButton
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
    var expr = Expression()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_simple_calc, container, false)

        //setup click listeners for numerical buttons
        val zero_button: ImageButton = view.findViewById(R.id.zero_button)
        zero_button.setOnClickListener{numericalButtonPress("0")}
        val one_button: ImageButton = view.findViewById(R.id.one_button)
        one_button.setOnClickListener{numericalButtonPress("1")}
        val two_button: ImageButton= view.findViewById(R.id.two_button)
        two_button.setOnClickListener{ numericalButtonPress("2")}
        val three_button: ImageButton = view.findViewById(R.id.three_button)
        three_button.setOnClickListener{ numericalButtonPress("3")}
        val four_button: ImageButton = view.findViewById(R.id.four_button)
        four_button.setOnClickListener{numericalButtonPress("4")}
        val five_button: ImageButton = view.findViewById(R.id.five_button)
        five_button.setOnClickListener{numericalButtonPress("5")}
        val six_button: ImageButton = view.findViewById(R.id.six_button)
        six_button.setOnClickListener{numericalButtonPress("6")}
        val seven_button: ImageButton = view.findViewById(R.id.seven_button)
        seven_button.setOnClickListener{numericalButtonPress("7")}
        val eight_button: ImageButton = view.findViewById(R.id.eight_button)
        eight_button.setOnClickListener{numericalButtonPress("8")}
        val nine_button: ImageButton = view.findViewById(R.id.nine_button)
        nine_button.setOnClickListener{numericalButtonPress("9")}

        //set up listerners for operator buttons
        val multiply_button: ImageButton = view.findViewById(R.id.multiply_button)
        multiply_button.setOnClickListener{operatorButtonPress("*")}
        val divide_button: ImageButton = view.findViewById(R.id.divide_button)
        divide_button.setOnClickListener{operatorButtonPress("/")}
        val add_button: ImageButton = view.findViewById(R.id.add_button)
        add_button.setOnClickListener{operatorButtonPress("+")}
        val subtract_button: ImageButton = view.findViewById(R.id.subtract_button)
        subtract_button.setOnClickListener{operatorButtonPress("-")}


        //TODO
        //set up percent button
        val percent_button: ImageButton = view.findViewById(R.id.percent_button)
        percent_button.setOnClickListener{
            expr.percentButton()
            drawInput()
            drawOutput()
        }


        //setup clear button
        val clear_button: ImageButton = view.findViewById(R.id.clear_button)
        clear_button.setOnClickListener{
            expr.clearTokens()
            drawInput()
            drawOutput()
        }

        //setup parenthesis button
        val parenthesis_button: ImageButton = view.findViewById(R.id.parenthesis_button)
        parenthesis_button.setOnClickListener{
            expr.parenthesisButton()
            drawInput()
            drawOutput()
        }

        //setup plus/minus button
        val sign_button: ImageButton = view.findViewById(R.id.sign_button)
        sign_button.setOnClickListener{
            expr.signButton()
            drawInput()
            drawOutput()
        }

        //setup decimal button
        val decimal_button: ImageButton = view.findViewById(R.id.decimal_button)
        decimal_button.setOnClickListener{
            expr.decimalButton()
            drawInput()
            drawOutput()
        }

        //This is a really stupid button.
        val equals_button: ImageButton = view.findViewById(R.id.equals_button)
        equals_button.setOnClickListener{
            drawInput()
            drawOutput()
        }

        // Inflate the layout for this fragment
        return view
    }

    /**
     * Logic for the onClickListeners for the entry of numerals
     * @param token A String representation of the digit
     */
    fun numericalButtonPress(token:String){
        expr.numericalButtonPress((token))
        drawInput()
        drawOutput()
    }

    /**
     * Logic for the onClickListener for Operator Buttons
     * @param token A String representation of the operator
     */
    fun operatorButtonPress(token:String){
        expr.operatorButtonPress(token)
        drawInput()
        drawOutput()
    }

    fun drawInput(){
        val tmp = expr.getTokens()
        if(tmp.isNotEmpty()){
            if(expr.isPercentage()) equation_area.setText(tmp[0] + "%")
            else equation_area.setText(tmp.joinToString(separator = " "))
        }
        else equation_area.setText("0")
    }

    fun drawOutput(){
        //answer_area.setText(shuntingYard(tokens).joinToString(separator = " "))
        val rpn = expr.getRPN()
        if (rpn.isNotEmpty()){
            val output = expr.getEval()
            if(output == "/0") answer_area.setText("")
            else if(output != null) answer_area.setText(output)
        }
        if(expr.getTokens().isEmpty()) answer_area.setText("0")
    }
}
