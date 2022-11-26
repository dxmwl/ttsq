package com.shengqianjun.mobile.app

import android.content.Context
import android.text.TextUtils
import androidx.core.content.edit
import com.shengqianjun.mobile.http.api.ClassApi

/**
 * APP辅助类
 */
object AppHelper {

    //APP分类数据
    var classData = ArrayList<ClassApi.ClassInfo>()

    /**
     * 1女装，2男装，3内衣，4美妆，5配饰，6鞋品，7箱包，8儿童，9母婴，10居家，11美食，12数码，13家电，14其他，15车品，16文体
     */
    var bigClassList = arrayListOf(
        ClassApi.ClassInfo(cid = "1", main_name = "女装"),
        ClassApi.ClassInfo(cid = "2", main_name = "男装"),
        ClassApi.ClassInfo(cid = "3", main_name = "内衣"),
        ClassApi.ClassInfo(cid = "4", main_name = "美妆"),
        ClassApi.ClassInfo(cid = "5", main_name = "配饰"),
        ClassApi.ClassInfo(cid = "6", main_name = "鞋品"),
        ClassApi.ClassInfo(cid = "7", main_name = "箱包"),
        ClassApi.ClassInfo(cid = "8", main_name = "儿童"),
        ClassApi.ClassInfo(cid = "9", main_name = "母婴"),
        ClassApi.ClassInfo(cid = "10", main_name = "居家"),
        ClassApi.ClassInfo(cid = "11", main_name = "美食"),
        ClassApi.ClassInfo(cid = "12", main_name = "数码"),
        ClassApi.ClassInfo(cid = "13", main_name = "家电"),
        ClassApi.ClassInfo(cid = "14", main_name = "其他"),
        ClassApi.ClassInfo(cid = "15", main_name = "车品"),
        ClassApi.ClassInfo(cid = "16", main_name = "文体"),
    )

    private const val RECENT_SEARCH = "RECENT_SEARCH"
    private const val SEARCH_HISTORY = "SEARCH_HISTORY"

    /**
     * 保存历史搜索记录
     */
    fun saveSearchHistory(keyword: String) {
        if (keyword.isBlank()) {
            return
        }
        val sp = AppApplication.getApp().getSharedPreferences(RECENT_SEARCH, Context.MODE_PRIVATE)
        if (TextUtils.isEmpty(keyword)) {
            return
        }
        //获取之前保存的历史记录
        val longHistory = sp.getString(SEARCH_HISTORY, "")
        val tmpHistory = longHistory!!.split(",".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray() //逗号截取 保存在数组中
        //将改数组转换成ArrayList
        val historyList = tmpHistory.toMutableList()
        val editor = sp.edit()
        if (historyList.isNotEmpty()) {
            //1.移除之前重复添加的元素
            for (i in historyList.indices) {
                if (keyword == historyList[i]) {
                    historyList.removeAt(i)
                    break
                }
            }
            //将新输入的文字添加集合的第0位也就是最前面(2.倒序)
            historyList.add(0, keyword)
            if (historyList.size > 8) {
                //3.最多保存8条搜索记录 删除最早搜索的那一项
                historyList.removeAt(historyList.size - 1)
            }
            //逗号拼接
            val sb = StringBuilder()
            for (i in historyList.indices) {
                sb.append(historyList.get(i) + ",")
            }
            //保存到sp
            editor.putString(SEARCH_HISTORY, sb.toString())
            editor.apply()
        } else {
            //之前未添加过
            editor.putString(SEARCH_HISTORY, "$keyword,")
            editor.commit()
        }
    }


    //获取搜索记录
    fun getHistorySearch(): List<String> {
        val sp = AppApplication.getApp().getSharedPreferences(RECENT_SEARCH, Context.MODE_PRIVATE)
        val longHistory = sp.getString(SEARCH_HISTORY, "")
        //split后长度为1有一个空串对象
        val tmpHistory =
            longHistory!!.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val historyList = tmpHistory.toMutableList()
        //如果没有搜索记录，split之后第0位是个空串的情况下
        if (historyList.size == 1 && historyList[0] == "") {
            historyList.clear()  //清空集合，这个很关键
        }
        return historyList
    }

    //清除搜索记录
    fun clearHistorySearch() {
        val sp = AppApplication.getApp().getSharedPreferences(RECENT_SEARCH, Context.MODE_PRIVATE)
        sp.edit { clear() }
    }
}