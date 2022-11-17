package com.easybuy.mobile.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.easybug.mobile.R
import com.easybuy.mobile.app.AppAdapter
import com.easybuy.mobile.http.api.ZtkClassApi
import com.easybuy.mobile.http.glide.GlideApp

/**
 * @project : YunKe
 * @Description : 项目描述
 * @author : clb
 * @time : 2022/5/31
 */
class TwoClassAdapter(val listener: OnItemClickListener) :
    RecyclerView.Adapter<TwoClassAdapter.TwoClassViewHolder>() {

    private var listData = ArrayList<ZtkClassApi.ClassInfo>()

    inner class TwoClassViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView14: TextView? by lazy { itemView.findViewById(R.id.textView14) }
        val serviceList: RecyclerView? by lazy { itemView.findViewById(R.id.two_class_list) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TwoClassViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_two_class, parent, false)
        return TwoClassViewHolder(view)
    }

    override fun onBindViewHolder(holder: TwoClassViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        val serviceClassDto = listData[position]
//        holder.textView14?.text = serviceClassDto.next_name
        holder.serviceList?.let {
//            it.layoutManager = GridLayoutManager(holder.itemView.context, 2)
//            val threeClassAdapter = ThreeClassAdapter(holder.itemView.context, listener)
//            it.adapter = threeClassAdapter
//            threeClassAdapter.setData(listData)
//            it.addItemDecoration(SpacesItemDecoration(15))
        }
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    fun setData(listData: ArrayList<ZtkClassApi.ClassInfo>) {
        this.listData.clear()
        this.listData.addAll(listData)
        notifyDataSetChanged()
//        notifyItemRangeInserted(0, this.listData.size)
    }

    interface OnItemClickListener {
        fun onItemClick(classDataBean: ZtkClassApi.ClassInfo)
    }

    class ThreeClassAdapter(
        val mContext: Context,
        val listener: OnItemClickListener?
    ) :
        AppAdapter<ZtkClassApi.ClassInfo>(mContext) {


        inner class ViewHolder : AppViewHolder(R.layout.item_three_class) {
            private val serviceTitle: TextView? by lazy { findViewById(R.id.textView15) }
            private val tvNumber: TextView? by lazy { findViewById(R.id.tv_number) }
            private val serviceIcon: ImageView? by lazy { findViewById(R.id.imageView2) }

            override fun onBindView(position: Int) {
                val item = getItem(position)
                serviceTitle?.text = item.q

                serviceIcon?.let { GlideApp.with(mContext).load(item.q_pic).into(it) }

                getItemView().setOnClickListener {
                    listener?.onItemClick(item)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
            return ViewHolder()
        }

        interface OnItemClickListener {
            fun onItemClick(classDataBean: ZtkClassApi.ClassInfo)
        }
    }
}