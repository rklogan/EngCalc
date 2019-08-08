package ec.engcalc

import android.media.Image
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.ImageButton
import android.widget.PopupMenu

class MainActivity : AppCompatActivity() {
    var mode = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //load the default calculator
        supportFragmentManager
            .beginTransaction()
            .add(R.id.calc_container, SimpleCalc())
            .commit()

        //setup the hamburger button
        val hamburger = findViewById<ImageButton>(R.id.hamburger_btn)
        hamburger.setOnClickListener{
            val popupMenu: PopupMenu = PopupMenu(this, hamburger)
            popupMenu.menuInflater.inflate(R.menu.nav_popup_menu, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener{ item ->
                when(item.itemId){
                    R.id.nav_simple -> {
                        Log.d("NavMenu","Simple")
                        if(mode != 0) {
                            supportFragmentManager
                                .beginTransaction()
                                .replace(R.id.calc_container, SimpleCalc())
                                .commit()
                            mode = 0
                        }
                    }
                    R.id.nav_scientific -> {
                        Log.d("NavMenu", "Scientific")
                        if(mode != 1) {
                            supportFragmentManager
                                .beginTransaction()
                                .replace(R.id.calc_container, ScientificCalc())
                                .commit()
                            mode = 1
                        }
                    }
                    else -> Log.d("NavMenu","Other")
                }
                true
            }
            popupMenu.show()
        }

    }
}
