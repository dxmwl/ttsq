package com.easybuy.mobile.ui.activity

import android.content.Intent
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.blankj.utilcode.util.ToastUtils
import com.easybug.mobile.R
import com.easybuy.mobile.aop.SingleClick
import com.easybuy.mobile.app.AppActivity

class SearchActivity : AppActivity() {

    private val btn_search: TextView? by lazy { findViewById(R.id.btn_search) }
    private val input_keyword: EditText? by lazy { findViewById(R.id.input_keyword) }

    override fun getLayoutId(): Int {
        return R.layout.activity_search
    }

    override fun initView() {
        setOnClickListener(btn_search)
    }

    override fun initData() {

    }

    @SingleClick
    override fun onClick(view: View) {
        when (view) {
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

}
