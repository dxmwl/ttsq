package com.easybuy.mobile.ui.activity

import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.easybug.mobile.R
import com.easybuy.mobile.aop.SingleClick
import com.easybuy.mobile.app.AppActivity
import com.easybuy.mobile.app.AppHelper
import com.easybuy.mobile.http.api.HomeGoodsListApi
import com.easybuy.mobile.http.api.SearchGoodsApi
import com.easybuy.mobile.http.model.HttpData
import com.easybuy.mobile.http.model.MenuDto
import com.easybuy.mobile.ui.adapter.SearchGoodsListAdapter
import com.easybuy.mobile.ui.popup.ZongheShadowPopupView
import com.hjq.http.EasyHttp
import com.hjq.http.listener.OnHttpListener
import com.hjq.shape.view.ShapeCheckBox
import com.lxj.xpopup.XPopup
import com.scwang.smart.refresh.layout.SmartRefreshLayout

/**
 * @project : EasyBuy_Android
 * @Description : 项目描述
 * @author : clb
 * @time : 2022/6/23
 */
class SearchResultActivity : AppActivity() {
    private var pageIndex: Int = 1
    private var keyword: String = ""

    private var homeGoodsListAdapter: SearchGoodsListAdapter? = null
    private val goodsList: RecyclerView? by lazy { findViewById(R.id.goods_list) }
    private val refresh: SmartRefreshLayout? by lazy { findViewById(R.id.refresh) }
    private val input_keyword: EditText? by lazy { findViewById(R.id.input_keyword) }
    private val sort_view: TextView? by lazy { findViewById(R.id.sort_view) }
    private val is_youquan: ShapeCheckBox? by lazy { findViewById(R.id.is_youquan) }
    private var zonghePopupView: ZongheShadowPopupView? = null
    private var isYouquan: Int = 1
    private var paixu: String = "new"

    override fun getLayoutId(): Int {
        return R.layout.activity_search_result
    }

    override fun initView() {
        setOnClickListener(R.id.btn_search, R.id.iv_back, R.id.sort_view)
        keyword = intent.getStringExtra("KEYWORD").toString()
        input_keyword?.setText(keyword)
        input_keyword?.setSelection(keyword.length)

        input_keyword?.addTextChangedListener {
            keyword = it?.toString().toString()
        }

        goodsList?.let {
            it.layoutManager = GridLayoutManager(this, 2)
            homeGoodsListAdapter = SearchGoodsListAdapter(this)
            it.adapter = homeGoodsListAdapter
        }

        refresh?.setOnRefreshListener {
            pageIndex = 1
            searchGoods()
        }
        refresh?.setOnLoadMoreListener {
            pageIndex++
            searchGoods()
        }
        is_youquan?.setOnCheckedChangeListener { buttonView, isChecked ->
            isYouquan = if (isChecked) {
                1
            } else {
                0
            }
            pageIndex = 1
            searchGoods()
        }
    }

    override fun initData() {

        searchGoods()
    }

    private fun searchGoods() {
        AppHelper.saveSearchHistory(keyword)
        EasyHttp.get(this)
            .api(SearchGoodsApi().apply {
                q = keyword
                page = pageIndex
                youquan = isYouquan
                sort = paixu
            })
            .request(object : OnHttpListener<HttpData<ArrayList<HomeGoodsListApi.GoodsBean>>> {
                override fun onSucceed(result: HttpData<ArrayList<HomeGoodsListApi.GoodsBean>>?) {
                    refresh?.finishRefresh()
                    refresh?.finishLoadMore()
                    if (pageIndex == 1) {
                        homeGoodsListAdapter?.clearData()
                    }
                    homeGoodsListAdapter?.addData(result?.getData())
                }

                override fun onFail(e: Exception?) {
                    refresh?.finishRefresh()
                    refresh?.finishLoadMore()
                    toast(e?.message)
                }
            })
    }

    @SingleClick
    override fun onClick(view: View) {
        super.onClick(view)
        when (view.id) {
            R.id.iv_back -> {
                finish()
            }
            R.id.btn_search -> {
                pageIndex = 1
                searchGoods()
            }
            R.id.sort_view -> {
                showPartShadow(view)
            }
            else -> {}
        }
    }

    private fun showPartShadow(v: View) {
        if (zonghePopupView == null) {
            val listData = arrayListOf(
                MenuDto(title = "综合排序", checked = true, value = "new"),
                MenuDto(title = "总销量从小到大排序", checked = false, value = "total_sale_num_asc"),
                MenuDto(title = "总销量从大到小排序", checked = false, value = "total_sale_num_desc"),
                MenuDto(title = "月销量从小到大排序", checked = false, value = "sale_num_asc"),
                MenuDto(title = "月销量从大到小排序", checked = false, value = "sale_num_desc"),
                MenuDto(title = "价格从小到大排序", checked = false, value = "price_asc"),
                MenuDto(title = "价格从大到小排序", checked = false, value = "price_desc"),
            )
            val zongheShadowPopupView = ZongheShadowPopupView(
                this,
                listData
            )
            zongheShadowPopupView.setListener(object : ZongheShadowPopupView.OnListener<MenuDto> {
                override fun onSelected(
                    popupWindow: ZongheShadowPopupView?,
                    position: Int,
                    data: MenuDto
                ) {
                    paixu = data.value.toString()
                    pageIndex = 1
                    searchGoods()
                }
            })
            zonghePopupView = XPopup.Builder(this)
                .atView(v)
                .hasStatusBarShadow(false)
                .asCustom(
                    zongheShadowPopupView
                ) as ZongheShadowPopupView
        }
        zonghePopupView?.show()
    }
}