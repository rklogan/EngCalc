package ec.engcalc

import android.media.Image
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.ImageButton
import android.widget.PopupMenu

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //setup the hamburger button
        val hamburger = findViewById<ImageButton>(R.id.hamburger_btn)
        hamburger.setOnClickListener{
            val popupMenu: PopupMenu = PopupMenu(this, hamburger)
            popupMenu.menuInflater.inflate(R.menu.nav_popup_menu, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener{ item ->
                when(item.itemId){
                    R.id.nav_simple -> Log.d("NavMenu","Simple")
                    R.id.nav_scientific -> Log.d("NavMenu", "Scientific")
                    else -> Log.d("NavMenu","Other")
                }
                true
            }
            popupMenu.show()
        }

    }
}
