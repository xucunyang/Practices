package com.oranle.practices.refresh

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.AbsListView
import androidx.appcompat.app.AppCompatActivity
import com.oranle.practices.R
import com.oranle.practices.base.BaseRecyclerAdapter
import com.oranle.practices.base.SmartViewHolder
import kotlinx.android.synthetic.main.activity_example_basic.*

import android.R.layout.simple_list_item_2
import android.content.Context
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import java.util.*

class RecyclerViewActivity : AppCompatActivity() {

    var mAdapter = MyAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_example_basic)

        listView.setAdapter(mAdapter)
        //todo SCROLL_STATE_IDLE
        listView.setOnScrollListener(object : AbsListView.OnScrollListener {
            internal var SCROLL_STATE_IDLE = 0
            internal var SCROLL_STATE_TOUCH_SCROLL = 1
            internal var SCROLL_STATE_FLING = 2
            override fun onScrollStateChanged(view: AbsListView, scrollState: Int) {
                if (scrollState == SCROLL_STATE_IDLE) {
                    println("SCROLL_STATE_IDLE")
                } else if (scrollState == SCROLL_STATE_TOUCH_SCROLL) {
                    println("SCROLL_STATE_TOUCH_SCROLL")
                } else if (scrollState == SCROLL_STATE_FLING) {
                    println("SCROLL_STATE_FLING")
                }
            }

            override fun onScroll(
                view: AbsListView,
                firstVisibleItem: Int,
                visibleItemCount: Int,
                totalItemCount: Int
            ) {

            }
        })

        refreshLayout.setEnableAutoLoadMore(true)//开启自动加载功能（非必须）
        refreshLayout.setOnRefreshListener(OnRefreshListener { refreshLayout ->
            refreshLayout.layout.postDelayed({
                mAdapter.refresh(initData())
                refreshLayout.finishRefresh()
                refreshLayout.resetNoMoreData()//setNoMoreData(false);
            }, 2000)
        })
        refreshLayout.setOnLoadMoreListener(OnLoadMoreListener { refreshLayout ->
            refreshLayout.layout.postDelayed({
                if (mAdapter.itemCount > 30) {
                    Toast.makeText(application, "数据全部加载完毕", Toast.LENGTH_SHORT).show()
                    refreshLayout.finishLoadMoreWithNoMoreData()//将不会再次触发加载更多事件
                } else {
                    mAdapter.loadMore(initData())
                    refreshLayout.finishLoadMore()
                }
            }, 2000)
        })

        //触发自动刷新
        refreshLayout.autoRefresh()
        //item 点击测试
        mAdapter.setOnItemClickListener { parent, view, position, id ->
            val dialog = BottomSheetDialog(this@RecyclerViewActivity)
            val dialogView = View.inflate(baseContext, R.layout.activity_example_basic, null)
            val refreshLayout = dialogView.findViewById<SmartRefreshLayout>(R.id.refreshLayout)
            val recyclerView = RecyclerView(baseContext)
            recyclerView.setLayoutManager(LinearLayoutManager(baseContext))
            recyclerView.setAdapter(mAdapter)
            refreshLayout.setEnableRefresh(false)
            refreshLayout.setEnableNestedScroll(false)
            refreshLayout.setRefreshContent(recyclerView)
            dialog.setContentView(dialogView)
            dialog.show()
        }

        //点击测试
        val footer = refreshLayout.getRefreshFooter()
        if (footer is ClassicsFooter) {
            refreshLayout.getRefreshFooter()!!.getView().findViewById<View>(ClassicsFooter.ID_TEXT_TITLE)
                .setOnClickListener(
                    View.OnClickListener {
                        Toast.makeText(baseContext, "点击测试", Toast.LENGTH_SHORT).show()
                    })
        }
    }

    private fun initData(): MutableCollection<Int> {
        return IntArray(5).toMutableList()
    }

    class MyAdapter(val context: Context) : BaseRecyclerAdapter<Int>(simple_list_item_2) {
        override fun onBindViewHolder(holder: SmartViewHolder?, model: Int?, position: Int) {
            holder?.text(
                android.R.id.text1,
                context.getString(R.string.item_example_number_title, position)
            )
            holder?.text(
                android.R.id.text2, context.getString(R.string.item_example_number_abstract, position)
            )
            holder?.textColorId(android.R.id.text2, R.color.colorAccent)
        }

    }

}