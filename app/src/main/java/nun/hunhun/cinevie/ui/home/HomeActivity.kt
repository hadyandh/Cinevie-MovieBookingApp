package nun.hunhun.cinevie.ui.home

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_home.*
import nun.hunhun.cinevie.R
import nun.hunhun.cinevie.ui.home.dashboard.DashboardFragment
import nun.hunhun.cinevie.ui.home.setting.SettingFragment
import nun.hunhun.cinevie.ui.home.ticket.TicketFragment


class HomeActivity : AppCompatActivity() {

    val fm = supportFragmentManager

    private val dashboardFragment = DashboardFragment()
    private val settingFragment = SettingFragment()
    private val ticketFragment = TicketFragment()
    private lateinit var active: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setFragment()
        active = dashboardFragment

        navbottom_dashboard.setOnClickListener {
            fm.beginTransaction().hide(active).show(dashboardFragment).commit()
            active = dashboardFragment
            setIconChange(navbottom_dashboard, R.drawable.ic_dashboard_active)
            setIconChange(navbottom_ticket, R.drawable.ic_tiket)
            setIconChange(navbottom_user, R.drawable.ic_user)
        }

        navbottom_ticket.setOnClickListener {
            fm.beginTransaction().hide(active).show(ticketFragment).commit()
            active = ticketFragment
            setIconChange(navbottom_dashboard, R.drawable.ic_dashboard)
            setIconChange(navbottom_ticket, R.drawable.ic_tiket_active)
            setIconChange(navbottom_user, R.drawable.ic_user)
        }

        navbottom_user.setOnClickListener {
            fm.beginTransaction().hide(active).show(settingFragment).commit()
            active = settingFragment
            setIconChange(navbottom_dashboard, R.drawable.ic_dashboard)
            setIconChange(navbottom_ticket, R.drawable.ic_tiket)
            setIconChange(navbottom_user, R.drawable.ic_user_active)
        }
    }

//    private fun setFragment(mFragment: Fragment){
//
//        Log.d("mFRAG", ""+mFragment)
//
//        if (fragment != null){
//            fragmentTransaction.hide(active).show(mFragment)
//            active = mFragment
//            Log.d("ACTIVE", ""+active)
//        } else {
//            fragmentTransaction.add(R.id.layout_frame, mFragment, mFragment::class.java.simpleName).commit()
//            active = mFragment
//            Log.d("ACTIVE", ""+active)
//        }
//
//        fragmentTransaction.commit()
//    }

    private fun setFragment(){
        fm.beginTransaction().add(
            R.id.layout_frame,
            DashboardFragment(),
            DashboardFragment::class.java.simpleName
        ).commit()

        fm.beginTransaction().add(
            R.id.layout_frame,
            ticketFragment,
            TicketFragment::class.java.simpleName
        ).hide(ticketFragment).commit()

        fm.beginTransaction().add(
            R.id.layout_frame,
            settingFragment,
            SettingFragment::class.java.simpleName
        ).hide(settingFragment).commit()
    }

    private fun setIconChange(image: ImageView, int: Int){
        image.setImageResource(int)
    }
}