package com.oranle.practices.share_element

import android.os.Bundle
import android.transition.*
import android.view.Gravity
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import com.hw.ycshareelement.YcShareElement
import com.hw.ycshareelement.transition.IShareElements
import com.hw.ycshareelement.transition.ShareElementInfo
import com.hw.ycshareelement.transition.TextViewStateSaver
import com.oranle.practices.R
import com.oranle.practices.login.SHARE_IC
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        ViewCompat.setTransitionName(detail_img, "$SHARE_IC icLauncher")
        ViewCompat.setTransitionName(desc, "$SHARE_IC text")

        window.enterTransition = Slide(Gravity.START)
        window.exitTransition = Slide(Gravity.END)

//        //设置ShareElementTransition, 指定的share Element会执行这个Transition动画
//        val transitionSet = TransitionSet()
//        transitionSet.addTransition(ChangeBounds())
//        transitionSet.addTransition(ChangeTransform())
//        transitionSet.addTarget(detail_img)
//        transitionSet.addTarget(desc)
//
//        window.sharedElementEnterTransition = transitionSet
//        window.sharedElementExitTransition = transitionSet

        YcShareElement.setEnterTransitions(this,
            {
                arrayOf(
                    ShareElementInfo<Nothing>(detail_img),
                    ShareElementInfo(desc, TextViewStateSaver())
                )
            }, false
        )
    }

}