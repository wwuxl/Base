package wxl.com.base

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.Adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.item_grid.view.*
import wxl.com.base.activity.NetStatusActivity
import wxl.com.base.databinding.ActivityGridBinding
import wxl.com.base.databinding.ItemGridBinding
import wxl.com.base.recycler.IReloadData
import wxl.com.base.recycler.RIAdapter
import wxl.com.base.recycler.RecyclerViewDelegate
import wxl.com.base.utils.MyLog
import wxl.com.base.view.NetStatusLayout

class GridActivity : NetStatusActivity(), IReloadData, RIAdapter<String> {
    var recyclerViewDelegate:RecyclerViewDelegate<String>?=null

    override fun onCreateView(parent: ViewGroup, viewType: Int): ViewDataBinding {
        return DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.item_grid, parent, false)
    }

    override fun onBindView(data: String, binding: ViewDataBinding, position: Int) {
        var itemBinding = binding as ItemGridBinding
        itemBinding.name.text = data
    }

    override fun reLoadData() {
        Thread(object :Runnable{
            override fun run() {
                MyLog.e("===","发送消息")
                handler.sendMessageDelayed(Message.obtain(),2000)
            }

        }).start()

    }

    lateinit var mBinding: ActivityGridBinding
    override fun initSucceedContentView(): View? {
        mBinding = DataBindingUtil.inflate<ActivityGridBinding>(LayoutInflater.from(this), R.layout.activity_grid, null, false)
        return mBinding.root
    }

    override fun isHasTitle(): Boolean {
        return true
    }

    override fun initTitle(): String? {
        return "网格列表"
    }

    override fun errorReloadData() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initData()
        initView()
    }
    private var datas:ArrayList<String>?=null
    private fun initData() {
        datas= arrayListOf()
        for (i in 0..20){
            datas?.add("小红$i")
        }


    }

    private fun initView() {
//        recyclerViewDelegate=RecyclerViewDelegate.Builder(this,this,this)
//                .recyclerView(mBinding.swipeRefreshLayout,mBinding.recyclerView,2)
//                .build()
//
//        recyclerViewDelegate?.reloadData()

        mBinding.recyclerView.layoutManager=GridLayoutManager(this,2)
        mBinding.recyclerView.adapter= object :Adapter<RecyclerView.ViewHolder>(){
            override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
                var itemBinding:ItemGridBinding=DataBindingUtil.inflate(LayoutInflater.from(this@GridActivity), R.layout.item_grid, p0, false)

                return MyViewHolder(itemBinding.root )
            }

            override fun getItemCount(): Int {
                return datas!!.size
            }

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, p1: Int) {
                var itemHolder =holder as MyViewHolder
                itemHolder.name.text=datas?.get(p1)

            }

        }



    }

    inner class MyViewHolder: RecyclerView.ViewHolder {
        var name:TextView
        constructor(itemView: View):super(itemView){
            name=itemView.name
        }
    }


    private var handler= MyHandler()

    inner class MyHandler : Handler(){
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)

            MyLog.e("===","收到消息")
            var datas= arrayListOf<String>()
            for(i in 0..10){
                datas.add("小明$i")
            }
            recyclerViewDelegate?.setDatas(datas)
            setDataStatus(NetStatusLayout.NetStatus.STATUS_SUCCEED)
        }
    }
}
