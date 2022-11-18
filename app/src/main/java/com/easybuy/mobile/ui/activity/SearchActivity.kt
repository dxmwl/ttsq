package com.easybuy.mobile.ui.activity

import android.content.Intent
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.ToastUtils
import com.easybug.mobile.R
import com.easybuy.mobile.aop.SingleClick
import com.easybuy.mobile.app.AppActivity
import com.easybuy.mobile.app.AppHelper
import com.easybuy.mobile.http.api.HotSearchApi
import com.easybuy.mobile.http.model.HttpData
import com.easybuy.mobile.ui.dialog.MessageDialog
import com.easybuy.mobile.widget.FlowLayout
import com.gyf.immersionbar.ImmersionBar
import com.hjq.base.BaseDialog
import com.hjq.http.EasyHttp
import com.hjq.http.listener.OnHttpListener
import java.lang.Exception

class SearchActivity : AppActivity() {

    private val btn_search: TextView? by lazy { findViewById(R.id.btn_search) }
    private val input_keyword: EditText? by lazy { findViewById(R.id.input_keyword) }
    private val search_history_layout: FlowLayout? by lazy { findViewById(R.id.search_history_layout) }
    private val hot_search_layout: FlowLayout? by lazy { findViewById(R.id.hot_search_layout) }
    private val icon_del_history: ImageView? by lazy { findViewById(R.id.icon_del_history) }

    override fun getLayoutId(): Int {
        return R.layout.activity_search
    }

    override fun initView() {
        setOnClickListener(btn_search,icon_del_history)
    }

    override fun initData() {
        getHotSearch()
    }

    private fun getHotSearch() {
        EasyHttp.get(this)
            .api(HotSearchApi())
            .request(object : OnHttpListener<HttpData<ArrayList<HotSearchApi.HotSeaDto>>> {
                override fun onSucceed(result: HttpData<ArrayList<HotSearchApi.HotSeaDto>>?) {
                    result?.getData()?.let {
                        if (it.size > 20) {
                            val subList = it.subList(0, 19)
                            buildHotSearch(subList)
                        } else {
                            buildHotSearch(it)
                        }
                    }
                }

                override fun onFail(e: Exception?) {
                    toast(e?.message)
                }

            })
    }

    @SingleClick
    override fun onClick(view: View) {
        when (view) {
            icon_del_history->{
                MessageDialog.Builder(this)
                    .setMessage("确定要清空所有的搜索记录吗?")
                    .setConfirm("清空")
                    .setListener(object :MessageDialog.OnListener{
                        override fun onConfirm(dialog: BaseDialog?) {
                            AppHelper.clearHistorySearch()
                            onResume()
                        }
                    })
                    .show()
            }
            btn_search -> {
                val keyword = input_keyword?.text.toString()
                if (keyword.isEmpty()) {
                    ToastUtils.showShort("请输入要搜索的内容")
                    return
                }
                val intent = Intent(this, SearchResultActivity::class.java)
                intent.putExtra("KEYWORD", keyword)
                startActivity(intent)
            }
            else -> {}
        }
    }

    override fun createStatusBarConfig(): ImmersionBar {
        return super.createStatusBarConfig() // 指定导航栏背景颜色
            .navigationBarColor(R.color.white) // 不要把整个布局顶上去
            .keyboardEnable(true)
    }

    override fun onResume() {
        super.onResume()
        val historySearch = AppHelper.getHistorySearch()
        buildHistorySearch(historySearch)
    }

    /**
     * 构造历史搜索记录
     */
    private fun buildHistorySearch(it: List<String>) {
        search_history_layout?.removeAllViews()
        val layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(
            ConvertUtils.dp2px(7F),
            ConvertUtils.dp2px(7F),
            ConvertUtils.dp2px(7F),
            ConvertUtils.dp2px(7F)
        )
        it.forEach { data ->
            val tv = TextView(this)
            tv.setPadding(
                ConvertUtils.dp2px(16F),
                ConvertUtils.dp2px(5F),
                ConvertUtils.dp2px(16F),
                ConvertUtils.dp2px(5F)
            )
            tv.text = data
            tv.maxEms = 10
            tv.setTextColor(Color.parseColor("#666666"))
            tv.setSingleLine()
            tv.setBackgroundResource(R.drawable.shape_history_search_bg)
            tv.layoutParams = layoutParams
            search_history_layout?.addView(tv, layoutParams)
            tv.setOnClickListener {
                val intent = Intent(this, SearchResultActivity::class.java)
                intent.putExtra("KEYWORD", data)
                startActivity(intent)
            }
        }
    }

    /**
     * 构造热门搜索
     */
    private fun buildHotSearch(data: List<HotSearchApi.HotSeaDto>) {
        hot_search_layout?.removeAllViews()
        val layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(
            ConvertUtils.dp2px(7F),
            ConvertUtils.dp2px(7F),
            ConvertUtils.dp2px(7F),
            ConvertUtils.dp2px(7F)
        )
        data.forEach { data ->
            val tv = TextView(this)
            tv.setPadding(
                ConvertUtils.dp2px(16F),
                ConvertUtils.dp2px(5F),
                ConvertUtils.dp2px(16F),
                ConvertUtils.dp2px(5F)
            )
            tv.text = data.keywords
            tv.maxEms = 10
            tv.setTextColor(Color.parseColor("#FC383E"))
            tv.setSingleLine()
            tv.setBackgroundResource(R.drawable.shape_hot_search_bg)
            tv.layoutParams = layoutParams
            hot_search_layout?.addView(tv, layoutParams)
            tv.setOnClickListener {
                val intent = Intent(this, SearchResultActivity::class.java)
                intent.putExtra("KEYWORD", data.keywords)
                startActivity(intent)
            }
        }
    }

}
