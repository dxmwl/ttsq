package com.easybuy.mobile.ui.adapter

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.easybug.mobile.R
import com.easybuy.mobile.app.AppAdapter
import com.easybuy.mobile.http.model.MenuDto

/**
 * @project : YunKe
 * @Description : 项目描述
 * @author : clb
 * @time : 2022/6/8
 */
class ZongheListAdapter(mContext: Context) : AppAdapter<MenuDto>(mContext) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        return ViewHolder()
    }

    inner class ViewHolder : AppViewHolder(R.layout.item_zonghe_menu) {

        private val ivChecked: ImageView? by lazy { findViewById(R.id.iv_checked) }
        private val tvTitle: TextView? by lazy { findViewById(R.id.textView31) }

        override fun onBindView(position: Int) {
            val item = getItem(position)
            tvTitle?.text = item.title
            if (item.checked) {
                tvTitle?.setTextColor(Color.parseColor("#FFFF8C69"))
                ivChecked?.visibility = View.VISIBLE
            } else {
                tvTitle?.setTextColor(Color.parseColor("#FF666666"))
                ivChecked?.visibility = View.INVISIBLE
            }
        }
    }

}