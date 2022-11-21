package com.easybuy.mobile.ui.activity

import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.ClipboardUtils
import com.easybug.mobile.R
import com.easybuy.mobile.app.AppActivity
import com.easybuy.mobile.http.api.GetYouhuiApi
import com.easybuy.mobile.http.model.HttpData
import com.hjq.http.EasyHttp
import com.hjq.http.listener.OnHttpListener
import com.hjq.shape.view.ShapeEditText

/**
 * 省钱宝
 */
class ShengqianbaoActivity : AppActivity() {

    private var youhuiUrl: String = ""
    private val shapeEditText: ShapeEditText? by lazy { findViewById(R.id.shapeEditText) }
    private val textView2: TextView? by lazy { findViewById(R.id.textView2) }
    private val shiji_pay: TextView? by lazy { findViewById(R.id.shiji_pay) }
    private val yuanjia: TextView? by lazy { findViewById(R.id.yuanjia) }
    private val youhuiquan: TextView? by lazy { findViewById(R.id.youhuiquan) }
    private val go_buy: TextView? by lazy { findViewById(R.id.go_buy) }

    override fun getLayoutId(): Int {
        return R.layout.activity_shengqianbao
    }

    private var inputTklStr = ""

    override fun initView() {
        setOnClickListener(R.id.go_buy, R.id.shapeTextView, R.id.clear_input)

        shapeEditText?.addTextChangedListener {
            inputTklStr = it?.toString().toString()
        }

        val clipboardContent = ClipboardUtils.getText()
        if (clipboardContent.isNullOrBlank().not()) {
            inputTklStr = clipboardContent.toString()
            shapeEditText?.setText(inputTklStr)
            shapeEditText?.setSelection(inputTklStr.length)
            getYouhuiInfo()
        }
    }

    override fun initData() {
        getYouhuiInfo()
    }

    override fun onClick(view: View) {
        super.onClick(view)
        when (view.id) {
            R.id.go_buy -> {
                if (AppUtils.isAppInstalled("com.taobao.taobao")) {
                    val intent = Intent()
                    intent.setAction("Android.intent.action.VIEW");
                    val uri = Uri.parse(youhuiUrl); // 商品地址
                    intent.setData(uri);
                    intent.setClassName(
                        "com.taobao.taobao",
                        "com.taobao.browser.BrowserActivity"
                    );
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//在非activity类中调用startactivity方法必须添加标签
                    startActivity(intent)
                } else {
                    BrowserActivity.start(this@ShengqianbaoActivity, youhuiUrl)
                }
            }
            R.id.clear_input -> {
                shapeEditText?.text?.clear()
            }
            R.id.shapeTextView -> {
                getYouhuiInfo()
            }
            else -> {}
        }
    }

    private fun getYouhuiInfo() {
        if (inputTklStr.isBlank()) {
            toast("请商品链接到输入框")
            return
        }
        EasyHttp.get(this)
            .api(GetYouhuiApi().apply {
                tkl = inputTklStr
            })
            .request(object : OnHttpListener<HttpData<ArrayList<GetYouhuiApi.GoodsYouhuiDto>>> {
                override fun onSucceed(result: HttpData<ArrayList<GetYouhuiApi.GoodsYouhuiDto>>?) {
                    result?.getData()?.get(0)?.let {
                        youhuiUrl = it.coupon_click_url
                        yuanjia?.text = it.size
                        textView2?.text = it.tao_title
                        youhuiquan?.text = "-${it.coupon_info_money}"
                        shiji_pay?.text = it.quanhou_jiage
                        go_buy?.text = "立即购买省\n¥${it.coupon_info_money}"
                    }
                }

                override fun onFail(e: Exception?) {
                    toast(e?.message)
                }
            })
    }
}