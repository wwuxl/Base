package wxl.com.base

import android.content.Intent
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.functions.Consumer
import wxl.com.base.activity.NetStatusActivity
import wxl.com.base.databinding.ActivityMainBinding
import wxl.com.base.databinding.ItemMainBinding
import wxl.com.base.databinding.ViewHeader1Binding
import wxl.com.base.databinding.ViewTitleRightBinding
import wxl.com.base.model.Response
import wxl.com.base.model.VersionInfo
import wxl.com.base.netapi.HttpManager
import wxl.com.base.recycler.IReloadData
import wxl.com.base.recycler.RIAdapter
import wxl.com.base.recycler.RecyclerViewDelegate
import wxl.com.base.rx.RxBus
import wxl.com.base.rx.RxHttpUtils
import wxl.com.base.subscriber.OnSubscriberListener
import wxl.com.base.subscriber.OnUpdataListener
import wxl.com.base.utils.MyLog
import wxl.com.base.utils.ToastUtil
import wxl.com.base.view.NetStatusLayout

class MainActivity : NetStatusActivity() ,RIAdapter<String>,IReloadData{

    var recyclerViewDelegate:RecyclerViewDelegate<String>?=null
    override fun onCreateView(parent: ViewGroup, viewType: Int): ViewDataBinding {
        return DataBindingUtil.inflate(LayoutInflater.from(this),R.layout.item_main,parent,false)
    }

    override fun onBindView(data: String, binding: ViewDataBinding, position: Int) {
        var itemBinding = binding as ItemMainBinding
        itemBinding.name.text=data
        itemBinding.root.setOnClickListener {
           //recyclerViewDelegate?.notifyItemRemoved(position)
            ToastUtil.show("$position")
            startActivity(Intent(this@MainActivity,GridActivity::class.java))
           // recyclerViewDelegate?.notifyItemChanged(position,"改变的数据")
            //recyclerViewDelegate?.notifyItemChanged("小明2","改变的数据")
            //recyclerViewDelegate?.notifyItemInserted("新增数据")
            //recyclerViewDelegate?.notifyItemInserted(position,"新增数据")
           // recyclerViewDelegate?.notifyItemRangeRemoved(position,2)


        }

    }

    override fun reLoadData() {
        var nextPage=recyclerViewDelegate?.getNextPage()
        Log.e("===","nextpage= $nextPage")
        Thread(object :Runnable{
            override fun run() {
                MyLog.e("===","发送消息")
                handler.sendMessageDelayed(Message.obtain(),2000)
            }

        }).start()


    }



    private lateinit var mBinding: ActivityMainBinding

    override fun initSucceedContentView(): View? {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.activity_main, null, false)
        return mBinding.root
    }

    override fun isHasTitle(): Boolean {
        return false
    }

    override fun initTitle(): String? {
        return "首页"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        initData()
        initRightView()


    }

    private fun initView() {
        var headerBinding=DataBindingUtil.inflate<ViewHeader1Binding>(LayoutInflater.from(this),R.layout.view_header1,mBinding.swipeRefreshLayout,false)
        recyclerViewDelegate=RecyclerViewDelegate.Builder(this,this,this)
                .recyclerView(mBinding.recyclerView,mBinding.swipeRefreshLayout)
                //.addItemDecoration(LineItemDecoration(Color.RED,50,20,20))
                .addHeaderView(headerBinding.root)
                .setOnPullRefresh(true,true)
                .build()

        recyclerViewDelegate?.reloadData()



    }

    private fun initRightView() {
        var rightBinding: ViewTitleRightBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.view_title_right, null, false)
        addTitleRightView(rightBinding.root)
    }

    private fun initData() {


        mDisposable.add(RxBus.register(String::class.java, Consumer<String> {
            MyLog.e("===","收到消息1  $it")
        }))

        RxBus.register(String::class.java, Consumer<String> {
            MyLog.e("===","收到消息2  $it")
        })

        RxBus.register(String::class.java, Consumer<String> {
            MyLog.e("===","收到消息3  $it")
        })

    }

    override fun errorReloadData() {
        Log.e("===", "重新加载")
        //mDisposable.add(RxUtils.rx(HttpManager.getHttpService().getGoodsQrCode(57468, 57468)))
        mDisposable.add(RxHttpUtils.rxUpdata(HttpManager.getUpdaService().getVersionInfo("seller_version.json"),object :OnUpdataListener<VersionInfo>{
            override fun onError(t: Throwable?) {
                MyLog.e("===","onError "+t.toString())
            }

            override fun onComplete() {
                MyLog.e("===","onComplete")
            }

            override fun onNext(t: VersionInfo) {
                MyLog.e("===","onNext "+t.upContent)
            }

        }))
        mDisposable.add(RxHttpUtils.rx(HttpManager.getHttpService().getGoodsQrCode(57468,57468),object :OnSubscriberListener<Response<String>>{
            override fun onNext(t: Response<String>) {

            }

        },this))


    }
    private var handler= MyHandler()

    inner class MyHandler :Handler(){
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            MyLog.e("===","收到消息")
            var datas= arrayListOf<String>()
//            if(i<=1){
//                i++
                for(i in 0..5){
                    datas.add("小明$i")
                }
//            }
            recyclerViewDelegate?.setDatas(datas)
            setDataStatus(NetStatusLayout.NetStatus.STATUS_SUCCEED)
        }
    }
    var i=0

}
