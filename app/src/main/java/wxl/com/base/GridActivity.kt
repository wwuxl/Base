package wxl.com.base

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import wxl.com.base.activity.NetStatusActivity
import wxl.com.base.databinding.ActivityGridBinding
import wxl.com.base.databinding.ItemGridBinding
import wxl.com.base.databinding.ViewFooterBinding
import wxl.com.base.databinding.ViewHeaderBinding
import wxl.com.base.recycler.IReloadData
import wxl.com.base.recycler.RIAdapter
import wxl.com.base.recycler.RecyclerViewDelegate
import wxl.com.base.rx.RxBus
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
            if(i%2==0){
                datas?.add("根据国家二级哥几个我够为各国看i佛我配几个武功鄂温克健儿搞明白")
            }else{
                datas?.add("小红$i")
            }
        }


    }

    private fun initView() {
        var headerBinding=DataBindingUtil.inflate<ViewHeaderBinding>(LayoutInflater.from(this),R.layout.view_header,null,false)
        var footerBinding=DataBindingUtil.inflate<ViewFooterBinding>(LayoutInflater.from(this),R.layout.view_footer,null,false)

        recyclerViewDelegate=RecyclerViewDelegate.Builder(this,this,this)
                .recyclerView(mBinding.swipeRefreshLayout,mBinding.recyclerView,3,StaggeredGridLayoutManager.VERTICAL,true)
                .addHeaderView(headerBinding.root)
                .addFooterView(footerBinding.root)
                .onPullRefresh(true,false)
                .build()

        recyclerViewDelegate?.reloadData()





    }


    private var handler= MyHandler()

    inner class MyHandler : Handler(){
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)

            MyLog.e("===","收到消息")
            RxBus.post("发送 nihao2")
            var datas= arrayListOf<String>()
            for(i in 0..30){
                if(i%2==0){
                    datas?.add("根据国家二级哥几个我够为各国看i佛我配几个武功鄂温克健儿搞明白")
                }else{
                    datas?.add("小明$i")
                }
            }
            recyclerViewDelegate?.setDatas(datas)
            setDataStatus(NetStatusLayout.NetStatus.STATUS_SUCCEED)
        }
    }
}
