package com.shengqianjun.mobile.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shengqianjun.mobile.R
import com.shengqianjun.mobile.app.AppAdapter
import com.shengqianjun.mobile.http.api.ClassApi
import com.shengqianjun.mobile.http.glide.GlideApp
import com.shengqianjun.mobile.widget.SpacesItemDecoration

/**
 * @project : YunKe
 * @Description : 项目描述
 * @author : clb
 * @time : 2022/5/31
 */
class TwoClassAdapter(val listener: ThreeClassAdapter.OnItemClickListener) :
    RecyclerView.Adapter<TwoClassAdapter.TwoClassViewHolder>() {

    private var listData = ArrayList<ClassApi.Data>()

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
        holder.textView14?.text = serviceClassDto.next_name
        holder.serviceList?.let {
            it.layoutManager = GridLayoutManager(holder.itemView.context, 4)
            val threeClassAdapter = ThreeClassAdapter(holder.itemView.context, listener)
            it.adapter = threeClassAdapter
            threeClassAdapter.setData(serviceClassDto.info)
            it.addItemDecoration(SpacesItemDecoration(15))
        }
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    fun setData(listData: ArrayList<ClassApi.Data>) {
        this.listData.clear()
        this.listData.addAll(listData)
        notifyDataSetChanged()
//        notifyItemRangeInserted(0, this.listData.size)
    }

//    interface OnItemClickListener {
//        fun onItemClick(classDataBean: ClassApi.ClassInfo)
//    }

    class ThreeClassAdapter(
        val mContext: Context,
        val listener: OnItemClickListener?
    ) :
        AppAdapter<ClassApi.Info>(mContext) {


        inner class ViewHolder : AppViewHolder(R.layout.item_three_class) {
            private val serviceTitle: TextView? by lazy { findViewById(R.id.textView15) }
            private val tvNumber: TextView? by lazy { findViewById(R.id.tv_number) }
            private val serviceIcon: ImageView? by lazy { findViewById(R.id.imageView2) }

            override fun onBindView(position: Int) {
                val item = getItem(position)
                serviceTitle?.text = item.son_name

                serviceIcon?.let { GlideApp.with(mContext).load(item.imgurl).into(it) }

                getItemView().setOnClickListener {
                    listener?.onItemClick(item)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
            return ViewHolder()
        }

        interface OnItemClickListener {
            fun onItemClick(classDataBean: ClassApi.Info)
        }
    }
}