package com.shengqianjun.mobile.ui.activity

import android.content.Intent
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.EncodeUtils
import com.blankj.utilcode.util.ToastUtils
import com.shengqianjun.mobile.R
import com.shengqianjun.mobile.aop.SingleClick
import com.shengqianjun.mobile.app.AppActivity
import com.shengqianjun.mobile.app.AppHelper
import com.shengqianjun.mobile.http.api.HotSearchApi
import com.shengqianjun.mobile.http.api.LianxiangApi
import com.shengqianjun.mobile.http.model.HttpData
import com.shengqianjun.mobile.ui.adapter.LianxiangciAdapter
import com.shengqianjun.mobile.ui.dialog.MessageDialog
import com.shengqianjun.mobile.widget.FlowLayout
import com.gyf.immersionbar.ImmersionBar
import com.hjq.base.BaseAdapter
import com.hjq.base.BaseDialog
import com.hjq.http.EasyHttp
import com.hjq.http.listener.OnHttpListener

class SearchActivity : AppActivity() {

    private lateinit var lianxiangciAdapter: LianxiangciAdapter
    private val btn_search: TextView? by lazy { findViewById(R.id.btn_search) }
    private val input_keyword: EditText? by lazy { findViewById(R.id.input_keyword) }
    private val search_history_layout: FlowLayout? by lazy { findViewById(R.id.search_history_layout) }
    private val hot_search_layout: FlowLayout? by lazy { findViewById(R.id.hot_search_layout) }
    private val icon_del_history: ImageView? by lazy { findViewById(R.id.icon_del_history) }
    private val iv_back: ImageView? by lazy { findViewById(R.id.iv_back) }
    private val match_character_list: RecyclerView? by lazy { findViewById(R.id.match_character_list) }

    override fun getLayoutId(): Int {
        return R.layout.activity_search
    }

    override fun initView() {
        setOnClickListener(btn_search, icon_del_history, iv_back)

        input_keyword?.addTextChangedListener {
            if (it.isNullOrEmpty()) {
                match_character_list?.visibility = View.GONE
            } else {
                getLianxiang(it.toString())
            }
        }

        match_character_list?.also {
            it.layoutManager = LinearLayoutManager(this)
            lianxiangciAdapter = LianxiangciAdapter(this)
            lianxiangciAdapter.setOnItemClickListener(object : BaseAdapter.OnItemClickListener {
                override fun onItemClick(
                    recyclerView: RecyclerView?,
                    itemView: View?,
                    position: Int
                ) {
                    val intent = Intent(this@SearchActivity, SearchResultActivity::class.java)
                    intent.putExtra("KEYWORD", lianxiangciAdapter.getItem(position)[0])
                    startActivity(intent)
                }
            })
            it.adapter = lianxiangciAdapter
        }
    }

    /**
     * 获取联想词
     */
    private fun getLianxiang(toString: String) {
        EasyHttp.get(this)
            .api(LianxiangApi().apply {
                content = EncodeUtils.urlEncode(toString)
            })
            .request(object : OnHttpListener<HttpData<ArrayList<LianxiangApi.LianxiangDto>>> {
                override fun onSucceed(result: HttpData<ArrayList<LianxiangApi.LianxiangDto>>?) {
                    result?.getData()?.let {
                        if (it.isEmpty()) {
                            match_character_list?.visibility = View.GONE
                        } else {
                            match_character_list?.visibility = View.VISIBLE
                            lianxiangciAdapter.setData(it)
                        }
                    }
                }

                override fun onFail(e: Exception?) {
                    toast(e?.message)
                }

            })
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
            icon_del_history -> {
                MessageDialog.Builder(this)
                    .setMessage("确定要清空所有的搜索记录吗?")
                    .setConfirm("清空")
                    .setListener(object : MessageDialog.OnListener {
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
            iv_back -> {
                finish()
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
            tv.text = data.keyword
            tv.maxEms = 10
            tv.setTextColor(Color.parseColor("#FC383E"))
            tv.setSingleLine()
            tv.setBackgroundResource(R.drawable.shape_hot_search_bg)
            tv.layoutParams = layoutParams
            hot_search_layout?.addView(tv, layoutParams)
            tv.setOnClickListener {
                val intent = Intent(this, SearchResultActivity::class.java)
                intent.putExtra("KEYWORD", data.keyword)
                startActivity(intent)
            }
        }
    }

}
