package com.ttsq.mobile.ui.activity

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.alibcprotocol.callback.AlibcTradeCallback
import com.baichuan.nb_trade.AlibcTrade
import com.hjq.base.BaseDialog
import com.hjq.http.EasyHttp
import com.hjq.http.listener.OnHttpListener
import com.orhanobut.logger.Logger
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.ttsq.mobile.R
import com.ttsq.mobile.app.AppActivity
import com.ttsq.mobile.http.api.CheckUserFreeGoodsApi
import com.ttsq.mobile.http.api.GetFreeGoodsListApi
import com.ttsq.mobile.http.api.GetLingquanUrlApi
import com.ttsq.mobile.http.model.HttpData
import com.ttsq.mobile.manager.UserManager
import com.ttsq.mobile.other.AppConfig
import com.ttsq.mobile.ui.adapter.FreeGoodsListAdapter
import com.ttsq.mobile.ui.dialog.MessageDialog
import java.lang.Exception

/**
 * @ClassName: FreeGoodsActivity
 * @Description:
 * @Author: 常利兵
 * @Date: 2024/2/04 0004 20:27
 **/
class FreeGoodsActivity : AppActivity() {

    private lateinit var freeGoodsListAdapter: FreeGoodsListAdapter
    private val refresh: SmartRefreshLayout? by lazy { findViewById(R.id.refresh) }
    private val goods_list: RecyclerView? by lazy { findViewById(R.id.goods_list) }

    //是否有0元购资格
    private var hasFreeGoods = false
    private var pageNum = 1

    override fun getLayoutId(): Int {
        return R.layout.activity_free_goods
    }

    override fun initView() {
        goods_list?.also {
            it.layoutManager = LinearLayoutManager(this)
            freeGoodsListAdapter = FreeGoodsListAdapter(this)
            it.adapter = freeGoodsListAdapter

            freeGoodsListAdapter.setClickListener(object :FreeGoodsListAdapter.OnFreeGoodsClick{
                override fun buyFreeGoods(goodsId:String,goodsName:String) {
                    if (AppConfig.isDebug()){
                        //测试用
                        hasFreeGoods = true
                    }
                    if (hasFreeGoods.not()){
                        MessageDialog.Builder(this@FreeGoodsActivity)
                            .setTitle("非常抱歉")
                            .setMessage("您的0元购资格已用尽，或不满足0元购资格，无法购买该商品")
                            .show()
                        return
                    }
                    MessageDialog.Builder(this@FreeGoodsActivity)
                        .setTitle("恭喜您，可参与0元购")
                        .setMessage("1.付款金额务必与补贴金额一致，付款时不要使用淘金币、红包，否则无法返现。\n2.所有商品均包邮，如遇商家设置运费，请购买其他物品")
                        .setConfirm("去购买")
                        .setListener(object :MessageDialog.OnListener{
                            override fun onConfirm(dialog: BaseDialog?) {
                                //判断是否授权绑定
                                UserManager.userInfo?.let {
                                    if (it.taobaoSpecialId.isNullOrBlank()) {
                                        MessageDialog.Builder(this@FreeGoodsActivity)
                                            .setTitle("温馨提示")
                                            .setMessage("授权绑定淘宝，才可以获得0元购权益，是否立即绑定？")
                                            .setConfirm("立即绑定")
                                            .setCancel("直接购买")
                                            .setListener(object : MessageDialog.OnListener {
                                                override fun onConfirm(dialog: BaseDialog?) {
                                                    startActivity(AuthorizationManagementActivity::class.java)
                                                }

                                                override fun onCancel(dialog: BaseDialog?) {
                                                    getLingquanUrl(goodsId,goodsName)
                                                }
                                            })
                                            .show()
                                    }else{
                                        getLingquanUrl(goodsId,goodsName)
                                    }
                                }
                            }
                        })
                        .show()
                }
            })
        }

        refresh?.setOnRefreshListener {
            pageNum = 1
            getFreeGoodsList()
        }
        refresh?.setOnLoadMoreListener {
            pageNum++
            getFreeGoodsList()
        }
    }

    override fun initData() {
        getFreeGoodsList()
    }

    /**
     * 获取领券地址
     */
    private fun getLingquanUrl(goodsId:String,goodsName:String) {
        showDialog()
        EasyHttp.post(this)
            .api(GetLingquanUrlApi().apply {
                itemid =goodsId
                title = goodsName
            })
            .request(object :
                OnHttpListener<HttpData<GetLingquanUrlApi.LingquanUrlDto>> {
                override fun onSucceed(result: HttpData<GetLingquanUrlApi.LingquanUrlDto>?) {
                    hideDialog()

                    BrowserActivity.start(
                        this@FreeGoodsActivity,
                        result?.getData()?.coupon_click_url.toString()
                    )
                }

                override fun onFail(e: java.lang.Exception?) {
                    hideDialog()
                    toast(e?.message)
                }

            })
    }

    private fun getFreeGoodsList() {
        EasyHttp.post(this)
            .api(GetFreeGoodsListApi().apply {
                pageNum = this@FreeGoodsActivity.pageNum
            })
            .request(object :
                OnHttpListener<HttpData<ArrayList<GetFreeGoodsListApi.FreeGoodsDeo>>> {
                override fun onSucceed(result: HttpData<ArrayList<GetFreeGoodsListApi.FreeGoodsDeo>>?) {
                    refresh?.finishRefresh()
                    refresh?.finishLoadMore()
                    result?.getData()?.let {
                        if (pageNum == 1) {
                            freeGoodsListAdapter.clearData()
                        }
                        freeGoodsListAdapter.addData(it)
                    }
                }

                override fun onFail(e: Exception?) {
                    refresh?.finishRefresh()
                    refresh?.finishLoadMore()
                    toast(e?.message)
                }

            })
    }

    override fun onResume() {
        super.onResume()
        checkUserFreeGoods()
    }

    private fun checkUserFreeGoods() {
        EasyHttp.post(this)
            .api(CheckUserFreeGoodsApi())
            .request(object :
                OnHttpListener<HttpData<CheckUserFreeGoodsApi.CheckUserFreeGoodsDto>> {
                override fun onSucceed(result: HttpData<CheckUserFreeGoodsApi.CheckUserFreeGoodsDto>?) {
                    result?.getData()?.let {
                        hasFreeGoods = it.hasFreeGoods
                    }
                }

                override fun onFail(e: Exception?) {
                    toast(e?.message)
                }
            })
    }
}