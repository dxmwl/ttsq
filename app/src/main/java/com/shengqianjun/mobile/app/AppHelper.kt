package com.shengqianjun.mobile.app

import android.content.Context
import android.text.TextUtils
import androidx.core.content.edit
import com.shengqianjun.mobile.http.api.ZtkClassApi

/**
 * APP辅助类
 */
object AppHelper {

    //APP分类数据
    var classData = ArrayList<ZtkClassApi.ClassInfo>()
    //APP二级分类数据
    var secondaryClassificationData = ArrayList<ZtkClassApi.ClassInfo>()

    private const val RECENT_SEARCH = "RECENT_SEARCH"
    private const val SEARCH_HISTORY = "SEARCH_HISTORY"

    /**
     * 保存历史搜索记录
     */
    fun saveSearchHistory(keyword: String) {
        if (keyword.isBlank()){
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