package com.ttsq.mobile.ui.adapter

import android.content.Context
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.ttsq.mobile.R
import com.ttsq.mobile.app.AppAdapter
import com.ttsq.mobile.http.api.GetWalletBalanceChangeLogApi

/**
 * @ClassName: WalletBlanceChangeLogAdapter
 * @Description:
 * @Author: 常利兵
 * @Date: 2024/2/04 0004 17:16
 **/
class WalletBlanceChangeLogAdapter(val mContext:Context): AppAdapter<GetWalletBalanceChangeLogApi.WalletBalanceDto>(mContext) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        return ViewHolder()
    }

    inner class ViewHolder: AppViewHolder(R.layout.item_shouyi) {

        private val textView20:TextView? by lazy { findViewById(R.id.textView20) }
        private val tv_time:TextView? by lazy { findViewById(R.id.tv_time) }
        private val textView19:TextView? by lazy { findViewById(R.id.textView19) }
        private val tv_status:TextView? by lazy { findViewById(R.id.tv_status) }
        override fun onBindView(position: Int) {
            val item = getItem(position)
            tv_time?.text = item.createTime
            when (item.type) {
                0 -> {
                    textView20?.text = "订单返利"
                    textView19?.text = "+${item.incomeMoney}"
                    textView19?.setTextColor(ContextCompat.getColor(mContext,R.color.black))
                }
                1 -> {
                    textView20?.text = "提现"
                    textView19?.text = "-${item.incomeMoney}"
                    textView19?.setTextColor(ContextCompat.getColor(mContext,R.color.red))
                }
                else -> {
                    textView20?.text = "提现失败返还"
                    textView19?.text = "+${item.incomeMoney}"
                    textView19?.setTextColor(ContextCompat.getColor(mContext,R.color.black))
                }
            }
        }
    }
}