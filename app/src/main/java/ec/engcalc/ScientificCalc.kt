package ec.engcalc

import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import kotlinx.android.synthetic.main.fragment_simple_calc.*


class ScientificCalc : SimpleCalc() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_scientific_calc, container, false)

        setupNumericalButtons(view)
        setupOperatorButtons(view)
        setupCommonButtons(view)

        // Inflate the layout for this fragment
        return view
    }
}
