package ec.engcalc

import org.junit.Test
import java.lang.Math.pow

class ArithmeticTest {
    @Test
    fun testBasicArithmetic(){
        val simpleCalc_testPostFix= arrayOf("3 4 / 5 2 / +","9 4 -","7 6 / 1 5 / -","6 4 - 4 8 * +","8 2 ^ 7 5 / 7 * +")
        val simpleCalc_expectedValues = arrayOf((3/4.0+5/2.0).toString(),"5",(7/6.0-1/5.0).toString(),(6-4+4*8).toString(),(pow(8.0,2.0)+7/5.0*7).toString())

        for( i in 0..simpleCalc_testPostFix.size-1){
            System.err.println(simpleCalc_expectedValues[i])
            assert(computeFromRPN(tokenizeString(simpleCalc_testPostFix[i]))
                    == simpleCalc_expectedValues[i])
        }

    }
}