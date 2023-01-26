package com.ttsq.mobile.ui.activity

import android.content.Context
import android.content.Intent
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hjq.http.EasyHttp
import com.hjq.http.listener.OnHttpListener
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.ttsq.mobile.R
import com.ttsq.mobile.aop.Log
import com.ttsq.mobile.app.AppActivity
import com.ttsq.mobile.http.api.PinpaiDetailApi
import com.ttsq.mobile.http.model.HttpData
import com.ttsq.mobile.ui.adapter.PinpaiDetailGoodsListAdapter
import java.lang.Exception

class PinpaiDetailActivity : AppActivity() {

    companion object {

        private val PINPAI_ID: String = "pinpaiId"

        @Log
        fun start(mContext: Context, pinpaiId: String?) {
            val intent = Intent(mContext, PinpaiDetailActivity::class.java)
            intent.putExtra(PINPAI_ID, pinpaiId)
            mContext.startActivity(intent)
        }
    }

    private lateinit var pinpaiDetailGoodsListAdapter: PinpaiDetailGoodsListAdapter
    private var pageIndex = 1

    private var pinpaiIdStr = ""

    private val logo_img: ImageView? by lazy { findViewById(R.id.logo_img) }
    private val textView11: TextView? by lazy { findViewById(R.id.textView11) }
    private val pinpai_biaoyu: TextView? by lazy { findViewById(R.id.pinpai_biaoyu) }
    private val tv_jieshao: TextView? by lazy { findViewById(R.id.tv_jieshao) }
    private val refresh: SmartRefreshLayout? by lazy { findViewById(R.id.refresh) }
    private val list_goods: RecyclerView? by lazy { findViewById(R.id.list_goods) }
    override fun getLayoutId(): Int {
        return R.layout.activity_pinpai_detail
    }

    override fun initView() {
        pinpaiIdStr = getString(PINPAI_ID).toString()
        refresh?.setOnRefreshListener {
            pageIndex = 1
            getPinpaiDetail()
        }
        refresh?.setOnLoadMoreListener {
            pageIndex++
            getPinpaiDetail()
        }
        list_goods?.let {
            it.layoutManager = GridLayoutManager(this, 2)
            pinpaiDetailGoodsListAdapter = PinpaiDetailGoodsListAdapter(this)
            it.adapter = pinpaiDetailGoodsListAdapter
        }
    }

    override fun initData() {
        getPinpaiDetail()
    }

    private fun getPinpaiDetail() {
        EasyHttp.get(this)
            .api(PinpaiDetailApi().apply {
                id = pinpaiIdStr
                min_id = pageIndex
            })
            .request(object : OnHttpListener<HttpData<PinpaiDetailApi.PinpaiDetailDto>> {
                override fun onSucceed(result: HttpData<PinpaiDetailApi.PinpaiDetailDto>?) {
                    refresh?.finishRefresh()
                    refresh?.finishLoadMore()
                    result?.getData()?.let {
                        logo_img?.let { it1 ->
                            Glide.with(this@PinpaiDetailActivity).load(it.brand_logo).into(it1)
                        }
                        textView11?.text = it.tb_brand_name
                        pinpai_biaoyu?.text = it.brand_plain
                        tv_jieshao?.text = it.introduce

                        if (pageIndex == 1) {
                            pinpaiDetailGoodsListAdapter.setData(it.items)
                        } else {
                            pinpaiDetailGoodsListAdapter.addData(it.items)
                        }
                    }
                }

                override fun onFail(e: Exception?) {
                    refresh?.finishRefresh()
                    refresh?.finishLoadMore()
                    toast(e?.message)
                }

            })
    }
}