package com.ttsq.mobile.ui.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.recyclerview.widget.RecyclerView
import com.hjq.http.EasyHttp
import com.hjq.http.listener.HttpCallback
import com.hjq.http.listener.OnHttpListener
import com.hjq.toast.ToastUtils
import com.ttsq.mobile.R
import com.ttsq.mobile.app.AppAdapter
import com.ttsq.mobile.http.api.GetMenuListApi
import com.ttsq.mobile.http.api.SwitchBdshLinkApi
import com.ttsq.mobile.http.glide.GlideApp
import com.ttsq.mobile.http.model.HttpData
import com.ttsq.mobile.http.model.MenuDto
import com.ttsq.mobile.manager.UserManager
import com.ttsq.mobile.ui.activity.BrowserActivity
import java.lang.Exception

/**
 * @project : YunKe
 * @Description : 项目描述
 * @author : clb
 * @time : 2022/5/31
 */
class HomeMenuListAdapter(val mContext: Context) : AppAdapter<GetMenuListApi.AppMenuDto>(mContext),
    LifecycleOwner {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        return ViewHolder()
    }

    inner class ViewHolder : AppViewHolder(R.layout.item_bdsh_menu) {

        private val imageView4: ImageView? by lazy { findViewById(R.id.imageView4) }
        private val title: TextView? by lazy { findViewById(R.id.title) }
        private val tv_label: TextView? by lazy { findViewById(R.id.tv_label) }

        override fun onBindView(position: Int) {
            val item = getItem(position)
            imageView4?.let { GlideApp.with(mContext).load(item.menuIcon).into(it) }
            title?.text = item.menuTitle
            if (item.menuLabel.isNullOrBlank()) {
                tv_label?.visibility = View.GONE
            } else {
                tv_label?.visibility = View.VISIBLE
                tv_label?.text = item.menuLabel
            }

            itemView.setOnClickListener {
                when (item.type) {
                    1 -> {
                        BrowserActivity.start(mContext, item.paramsValue)
                    }

                    5 -> {
                        switchBdshLink(item.paramsValue)
                    }

                    else -> {}
                }
            }
        }

    }

    private fun switchBdshLink(paramsValue: String) {

        EasyHttp.post(this)
            .api(SwitchBdshLinkApi().apply {
                platform = paramsValue
                channel = UserManager.userInfo?.userId
            })
            .request(object : OnHttpListener<HttpData<SwitchBdshLinkApi.SwitchBdshLinkDto>> {
                override fun onSucceed(result: HttpData<SwitchBdshLinkApi.SwitchBdshLinkDto>?) {
                    result?.getData()?.let {
                        BrowserActivity.start(mContext, it.link)
                    }
                }

                override fun onFail(e: Exception?) {
                    ToastUtils.show(e?.message)
                }

            })
    }

    override fun getLifecycle(): Lifecycle {
        return LifecycleRegistry(this)
    }
}