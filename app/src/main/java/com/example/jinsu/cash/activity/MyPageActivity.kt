package com.example.jinsu.cash.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.speech.tts.TextToSpeech
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.example.jinsu.cash.R
import com.example.jinsu.cash.common.Constant
import com.example.jinsu.cash.dialog.PopupDialog
import com.example.jinsu.cash.util.BluetoothService
import kotlinx.android.synthetic.main.activity_my_page.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.content_my_page.view.*
import kotlinx.android.synthetic.main.navi_header.view.*
import java.lang.ref.WeakReference

class MyPageActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {


    lateinit var glide : RequestManager
    lateinit var handler: ChartActivity.MyHandler
    lateinit var dialog: PopupDialog
    lateinit var tts : TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_page)
        glide = Glide.with(this)
        init()
    }

    fun init()
    {

        glide.load(R.drawable.profile).into(mypage_content.mypage_im_profile)
//        glide.load(R.drawable.tro1).apply(options).into(mypage_im_award)
        glide.load(R.drawable.account).into(mypage_content.im_my)
        glide.load(R.drawable.gift).into(mypage_content.im_point)
        glide.load(R.drawable.setting).into(mypage_content.im_setting)
        glide.load(R.drawable.logout).into(mypage_content.im_logout)
        mypage_content.mypage_toolbar.setNavigationIcon(getDrawable(R.drawable.menu))
        mypage_content.mypage_toolbar.title = getString(R.string.mypage)
        mypage_content.mypage_toolbar.setTitleTextColor(resources.getColor(R.color.white))
        setSupportActionBar(mypage_content.mypage_toolbar)
        val toggle = ActionBarDrawerToggle(this,mypage_layout,mypage_content.mypage_toolbar,
                0,0)
        mypage_layout.addDrawerListener(toggle)
        toggle.syncState()
        mypage_navi.setNavigationItemSelectedListener(this)
        Glide.with(this).load(R.drawable.my)
                .apply(RequestOptions().circleCrop())
                .into(mypage_navi.getHeaderView(0).navi_im_profile)
        Glide.with(this).load(R.drawable.my)
                .apply(RequestOptions().circleCrop())
                .into(mypage_content.mypage_im_profile)
        if(Constant.prefs.user_data != null) {
            val user = Constant.prefs.user_data
            mypage_navi.getHeaderView(0).navi_txt_id.setText(user!!.id.toString())
            mypage_content.mypage_txt_id.setText(user!!.id.toString())
            mypage_content.mypage_txt_nick.setText(user!!.nickname.toString())

        }

    }
    class MyHandler : Handler
    {
        lateinit var weak : WeakReference<ChartActivity>

        constructor(activity : ChartActivity)
        {
            weak = WeakReference<ChartActivity>(activity)
        }

        override fun handleMessage(msg: Message) {
            val activity : ChartActivity = weak.get()!!
            activity.handlerMessage(msg)
        }
    }

    fun handlerMessage(msg : Message)
    {
        when(msg.what)
        {
            //바른자세
            1 ->
            {
                main_txt_point.text = Constant.money.toString();
                main_txt_total_hour.text = (Constant.total_time/3600).toString()
                main_txt_total_min.text = ((Constant.total_time/60)%60).toString()
                main_txt_good_hour.text = (Constant.Right_time/3600).toString()
                main_txt_good_min.text = ((Constant.Right_time/60)%60).toString()
            }
            //기댄 자세
            2 ->
            {
                main_txt_total_hour.text = (Constant.total_time/3600).toString()
                main_txt_total_min.text = ((Constant.total_time/60)%60).toString()
                main_txt_bad_hour.text = (Constant.bad_time/3600).toString()
                main_txt_bad_min.text = ((Constant.bad_time/60)%60).toString()
                tts.speak("기대지 마세요",TextToSpeech.QUEUE_FLUSH,null,null)
                //   val dialog = PopupDialog(this@MainActivity, "뒤로 기댄 자세입니다.")
                if(dialog.isShowing()) {
                    dialog.dismiss()

                }
                if (!(this as Activity).isFinishing) {
                    dialog = PopupDialog(this,"뒤로 기댄 자세입니다.")
                    dialog.show()
                    dialog.setClick {
                        dialog.dismiss();
                    }
                }
            }
            //숙인 자세
            3 ->
            {
                main_txt_total_hour.text = (Constant.total_time/3600).toString()
                main_txt_total_min.text = ((Constant.total_time/60)%60).toString()
                main_txt_bad_hour.text = (Constant.bad_time/3600).toString()
                main_txt_bad_min.text = ((Constant.bad_time/60)%60).toString()
                tts.speak("숙이지 마세요!",TextToSpeech.QUEUE_FLUSH,null,null)
                //val dialog = PopupDialog(this@MainActivity, "앞으로 숙인 자세입니다.")
                if(dialog.isShowing()) {
                    dialog.dismiss()
                }
                if (!(this as Activity).isFinishing) {
                    dialog = PopupDialog(this,"앞으로 숙인 자세입니다.")
                    dialog.show()
                    dialog.setClick {
                        dialog.dismiss();
                    }
                }

            }
            //다리 꼰 자세
            4 ->
            {
                main_txt_total_hour.text = (Constant.total_time/3600).toString()
                main_txt_total_min.text = ((Constant.total_time/60)%60).toString()
                main_txt_bad_hour.text = (Constant.bad_time/3600).toString()
                main_txt_bad_min.text = ((Constant.bad_time/60)%60).toString()
                tts.speak("다리 꼬지 마세요",TextToSpeech.QUEUE_FLUSH,null,null)
                //   val dialog = PopupDialog(this@MainActivity, "왼쪽 다리를 꼰 자세입니다.")
                if(dialog.isShowing()) {
                    dialog.dismiss()

                }
                if (!(this as Activity).isFinishing) {
                    dialog = PopupDialog(this,"왼쪽 다리를 꼰 상태입니다.")
                    dialog.show()
                    dialog.setClick {
                        dialog.dismiss();
                    }
                }
            }
            //다리 꼰 자세
            5 -> {
                main_txt_total_hour.text = (Constant.total_time / 3600).toString()
                main_txt_total_min.text = ((Constant.total_time / 60) % 60).toString()
                main_txt_bad_hour.text = (Constant.bad_time / 3600).toString()
                main_txt_bad_min.text = ((Constant.bad_time / 60) % 60).toString()
                tts.speak("다리 꼬지 마세요", TextToSpeech.QUEUE_FLUSH, null, null)
                //val dialog = PopupDialog(this@MainActivity, "오른쪽 다리를 꼰 자세입니다.")
                if (dialog.isShowing()) {
                    dialog.dismiss()

                }
                if (!(this as Activity).isFinishing) {
                    dialog = PopupDialog(this, "오른쪽 다리를 꼰 상태입니다.")
                    dialog.show()
                    dialog.setClick {
                        dialog.dismiss();
                    }
                }
            }
        }
    }
    override fun onResume() {
        super.onResume()
        BluetoothService.get.setHandler(handler)
    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            R.id.menu_nav_home ->
            {
                startActivity(Intent(this,MainActivity::class.java))
                finish()
            }
            R.id.menu_nav_mypage ->
            {

            }
            R.id.menu_nav_chart ->
            {
                startActivity(Intent(this,ChartActivity::class.java))
            }
            R.id.menu_nav_shopping ->
            {
                startActivity(Intent(this,ShopActivity::class.java))
            }
            R.id.menu_nav_rank ->
            {
                startActivity(Intent(this,RankActivity::class.java))
            }
        }
        mypage_layout.closeDrawer(GravityCompat.START)
        return true
    }
}