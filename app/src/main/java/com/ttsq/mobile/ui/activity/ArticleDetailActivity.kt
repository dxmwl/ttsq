package com.ttsq.mobile.ui.activity

import android.content.Context
import android.content.Intent
import android.text.method.LinkMovementMethod
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.TimeUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.hjq.http.EasyHttp
import com.hjq.http.listener.OnHttpListener
import com.ttsq.mobile.R
import com.ttsq.mobile.aop.Log
import com.ttsq.mobile.app.AppActivity
import com.ttsq.mobile.http.api.ArticleDetailApi
import com.ttsq.mobile.http.model.HttpData
import com.ttsq.mobile.ui.adapter.ArticelGoodsListAdapter
import com.ttsq.mobile.utils.HtmlUtil
import com.zzhoujay.richtext.RichText
import com.zzhoujay.richtext.RichType

class ArticleDetailActivity : AppActivity() {

    companion object {

        private const val ARTICLE_ID: String = "articleId"

        @Log
        fun start(mContext: Context, articleId: String?) {
            val intent = Intent(mContext, ArticleDetailActivity::class.java)
            intent.putExtra(ARTICLE_ID, articleId)
            mContext.startActivity(intent)
        }
    }

    private var articleId: String = ""
    private lateinit var articelGoodsListAdapter: ArticelGoodsListAdapter
    private val banner_img: ImageView? by lazy { findViewById(R.id.banner_img) }
    private val user_profit: ImageView? by lazy { findViewById(R.id.user_profit) }
    private val nick_name: TextView? by lazy { findViewById(R.id.nick_name) }
    private val send_time: TextView? by lazy { findViewById(R.id.send_time) }
    private val see_num: TextView? by lazy { findViewById(R.id.see_num) }
    private val content: WebView? by lazy { findViewById(R.id.content) }
    private val goods_list: RecyclerView? by lazy { findViewById(R.id.goods_list) }

    override fun getLayoutId(): Int {
        return R.layout.activity_article_detail
    }

    override fun initView() {
        articleId = getString(ARTICLE_ID).toString()
        goods_list?.let {
            it.layoutManager = LinearLayoutManager(this)
            articelGoodsListAdapter = ArticelGoodsListAdapter(this)
            it.adapter = articelGoodsListAdapter
        }
        content?.let {
            val settings = it.settings
            settings.javaScriptEnabled = true
            settings.blockNetworkImage = false
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
    }

    override fun initData() {
        EasyHttp.get(this)
            .api(ArticleDetailApi().apply {
                id = articleId
            })
            .request(object : OnHttpListener<HttpData<ArticleDetailApi.ArticleDetailDto>> {
                override fun onSucceed(result: HttpData<ArticleDetailApi.ArticleDetailDto>?) {
                    result?.getData()?.let {
                        banner_img?.let { it1 ->
                            Glide.with(this@ArticleDetailActivity).load(it.article_banner).into(
                                it1
                            )
                        }
                        user_profit?.let { it1 ->
                            Glide.with(this@ArticleDetailActivity).load(it.head_img).transform(
                                MultiTransformation(CenterCrop(), CircleCrop())
                            ).into(
                                it1
                            )
                        }
                        nick_name?.text = it.talent_name
                        see_num?.text = it.readtimes
                        send_time?.text =
                            TimeUtils.millis2String(it.addtime * 1000, "yyyy.mm.dd hh:mm:ss")

                        var articleContent = it.article
                        if(articleContent.contains("&")){
                            articleContent = articleContent.replace("&#039;", "'");
                            articleContent = articleContent.replace("&quot;", "\"");
                            articleContent = articleContent.replace("&lt;", "<");
                            articleContent =articleContent.replace("&gt;", ">");
                            articleContent =articleContent.replace("&amp;", "&");
                        }
                        content?.loadDataWithBaseURL(null,getHtmlData(articleContent),"text/html","utf-8", null);
//                        content?.loadData(articleContent,"text/javascript", null)
//                        content?.setMovementMethod(LinkMovementMethod.getInstance());
//                        RichText.from(it.article)
//                            .type(RichType.html)
//                            .into(content)
//                        content?.text = it.article

                        articelGoodsListAdapter.setData(it.items)
                        title = it.shorttitle
                    }
                }

                override fun onFail(e: Exception?) {
                    toast(e?.message)
                }

            })
    }

    private fun getHtmlData(bodyHTML: String): String {
        val head = ("<head>"
                + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\"> "
                + "<style>img{max-width: 100%; width:100%; height:auto;}*{margin:0px;}</style>"
                + "</head>")
        return "<html>$head<body>$bodyHTML</body></html>"
    }
}