package com.ttsq.mobile.ui.dialog

import android.content.Context
import android.view.Gravity
import android.view.View
import com.hjq.base.BaseDialog
import com.hjq.base.action.AnimAction
import com.ttsq.mobile.R
import com.ttsq.mobile.aop.SingleClick
import com.ttsq.mobile.ui.activity.FreeGoodsActivity

/**
 * @ClassName: FreeGoodsDialog
 * @Description:
 * @Author: 常利兵
 * @Date: 2024/2/05 0005 14:37
 **/
class FreeGoodsDialog {
    class Builder(context: Context) : BaseDialog.Builder<Builder>(context) {

        init {
            setContentView(R.layout.free_goods_dialog)
            setAnimStyle(AnimAction.ANIM_BOTTOM)
            setGravity(Gravity.CENTER)

            setOnClickListener(R.id.iv_img)
        }

        @SingleClick
        override fun onClick(view: View) {
            when (view.id) {
                R.id.iv_img -> {
                    startActivity(FreeGoodsActivity::class.java)
                    dismiss()
                }

                else -> {}
            }
        }
    }
}