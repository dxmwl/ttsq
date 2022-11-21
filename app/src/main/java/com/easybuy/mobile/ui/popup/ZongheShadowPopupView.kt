package com.easybuy.mobile.ui.popup

import android.content.Context
import android.view.Menu
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.easybug.mobile.R
import com.easybuy.mobile.http.model.MenuDto
import com.easybuy.mobile.ui.adapter.ZongheListAdapter
import com.hjq.base.BaseAdapter
import com.hjq.base.BasePopupWindow
import com.lxj.xpopup.impl.PartShadowPopupView

/**
 * @project : YunKe
 * @Description : 项目描述
 * @author : clb
 * @time : 2022/6/8
 */
class ZongheShadowPopupView(val mContext: Context, val listData: ArrayList<MenuDto>) :
    PartShadowPopupView(mContext) {

    private lateinit var menuList: RecyclerView
    private var listener: OnListener<MenuDto>? = null

    override fun getImplLayoutId(): Int {
        return R.layout.popu_zonghe
    }

    override fun onCreate() {
        super.onCreate()
        menuList = findViewById(R.id.menu_list)
        menuList.let {
            it.layoutManager = LinearLayoutManager(mContext)
            val zongheListAdapter = ZongheListAdapter(mContext)
            zongheListAdapter.setOnItemClickListener(object : BaseAdapter.OnItemClickListener {
                override fun onItemClick(
                    recyclerView: RecyclerView?,
                    itemView: View?,
                    position: Int
                ) {
                    dismiss()
                    listData.forEachIndexed { index, menuDto ->
                        menuDto.checked = false
                        zongheListAdapter.notifyItemChanged(index)
                    }
                    listData[position].checked = true
                    zongheListAdapter.notifyItemChanged(position)

                    listener?.onSelected(this@ZongheShadowPopupView, position, zongheListAdapter.getItem(position))
                }

            })
            it.adapter = zongheListAdapter
            zongheListAdapter.addData(listData)
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun setListener(listener: OnListener<MenuDto>?) {
        this.listener = listener
    }

    interface OnListener<T> {

        /**
         * 选择条目时回调
         */
        fun onSelected(popupWindow: ZongheShadowPopupView?, position: Int, data: T)
    }
}