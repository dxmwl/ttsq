package com.ttsq.mobile.ui.activity

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ttsq.mobile.R
import com.ttsq.mobile.app.AppActivity
import com.ttsq.mobile.ui.adapter.FriendsListAdapter

/**
 * @ClassName: FriendsListActivity
 * @Description: 邀请的好友
 * @Author: 常利兵
 * @Date: 2023/6/24 23:08
 **/
class FriendsListActivity : AppActivity() {

    private lateinit var friendsListAdapter: FriendsListAdapter
    private val friend_list: RecyclerView? by lazy { findViewById(R.id.friend_list) }

    override fun getLayoutId(): Int {
        return R.layout.activity_friends_list
    }

    override fun initView() {
        friend_list?.also {
            it.layoutManager = LinearLayoutManager(this)
            friendsListAdapter = FriendsListAdapter(this)
            it.adapter = friendsListAdapter
        }
    }

    override fun initData() {
        friendsListAdapter.addData(arrayListOf("", "", "", "", "", "", ""))
    }
}