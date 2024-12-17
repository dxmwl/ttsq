package com.ttsq.mobile.ui.activity

import android.view.View
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.hjq.base.FragmentPagerAdapter
import com.ttsq.mobile.ui.fragment.JingdongSearchResultFragment
import com.ttsq.mobile.ui.fragment.PinduoduoSearchResultFragment
import com.ttsq.mobile.ui.fragment.TaobaoSearchResultFragment
import com.ttsq.mobile.ui.fragment.WeipinhuiSearchResultFragment
import com.ttsq.mobile.R
import com.ttsq.mobile.aop.SingleClick
import com.ttsq.mobile.app.AppActivity
import com.ttsq.mobile.app.AppFragment


/**
 * @project : EasyBuy_Android
 * @Description : 项目描述
 * @author : clb
 * @time : 2022/6/23
 */
class SearchResultActivity : AppActivity() {
    private val input_keyword: EditText? by lazy { findViewById(R.id.input_keyword) }
    private val tabLayout: TabLayout? by lazy { findViewById(R.id.tabLayout) }
    private val viewPager: ViewPager? by lazy { findViewById(R.id.viewPager) }
    private var keywordStr: String = ""

    override fun getLayoutId(): Int {
        return R.layout.activity_search_result
    }

    override fun initView() {
        setOnClickListener(R.id.btn_search, R.id.iv_back, R.id.sort_view)
        keywordStr = intent.getStringExtra("KEYWORD").toString()
        input_keyword?.setText(keywordStr)
        input_keyword?.setSelection(keywordStr.length)

        input_keyword?.addTextChangedListener {
            keywordStr = it?.toString().toString()
        }
    }

    override fun initData() {
        tabLayout?.setupWithViewPager(viewPager)
        val homeFragmentAdapter = FragmentPagerAdapter<AppFragment<*>>(this)
        homeFragmentAdapter.addFragment(TaobaoSearchResultFragment.newInstance(keywordStr),"淘宝")
        homeFragmentAdapter.addFragment(JingdongSearchResultFragment.newInstance(keywordStr),"京东")
        homeFragmentAdapter.addFragment(PinduoduoSearchResultFragment.newInstance(keywordStr),"拼多多")
//        homeFragmentAdapter.addFragment(WeipinhuiSearchResultFragment.newInstance(keywordStr),"唯品会")
        viewPager?.adapter = homeFragmentAdapter
    }

    @SingleClick
    override fun onClick(view: View) {
        super.onClick(view)
        when (view.id) {
            R.id.iv_back -> {
                finish()
            }

            R.id.btn_search -> {
                searchGoods()
            }

            R.id.sort_view -> {
//                showPartShadow(view)
            }

            else -> {}
        }
    }

    private fun searchGoods() {

    }
//
//    private fun showPartShadow(v: View) {
//        if (zonghePopupView == null) {
//            val listData = arrayListOf(
//                MenuDto(title = "综合排序", checked = true, value = "new"),
//                MenuDto(title = "总销量从小到大排序", checked = false, value = "total_sale_num_asc"),
//                MenuDto(title = "总销量从大到小排序", checked = false, value = "total_sale_num_desc"),
//                MenuDto(title = "月销量从小到大排序", checked = false, value = "sale_num_asc"),
//                MenuDto(title = "月销量从大到小排序", checked = false, value = "sale_num_desc"),
//                MenuDto(title = "价格从小到大排序", checked = false, value = "price_asc"),
//                MenuDto(title = "价格从大到小排序", checked = false, value = "price_desc"),
//            )
//            val zongheShadowPopupView = ZongheShadowPopupView(
//                this,
//                listData
//            )
//            zongheShadowPopupView.setListener(object : ZongheShadowPopupView.OnListener<MenuDto> {
//                override fun onSelected(
//                    popupWindow: ZongheShadowPopupView?,
//                    position: Int,
//                    data: MenuDto
//                ) {
//                    paixu = data.value.toString()
//                    pageIndex = 1
//                    searchGoods()
//                }
//            })
//            zonghePopupView = XPopup.Builder(this)
//                .atView(v)
//                .hasStatusBarShadow(false)
//                .asCustom(
//                    zongheShadowPopupView
//                ) as ZongheShadowPopupView
//        }
//        zonghePopupView?.show()
//    }


}