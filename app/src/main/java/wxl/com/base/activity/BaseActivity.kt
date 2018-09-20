package wxl.com.base.activity

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import io.reactivex.disposables.CompositeDisposable
import wxl.com.base.R
import wxl.com.base.databinding.ActivityBaseBinding
import wxl.com.base.databinding.ViewBaseTitleBinding
import wxl.com.base.subscriber.IFDialog
import wxl.com.base.subscriber.LoadingDialog


abstract class BaseActivity : AppCompatActivity() ,IFDialog{
    private lateinit var mBaseBinding: ActivityBaseBinding
    private var titleBinding: ViewBaseTitleBinding?=null
    private var loadingDialog:LoadingDialog?=null
    var mDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
        mBaseBinding = DataBindingUtil.setContentView(this, R.layout.activity_base)
        if (isHasTitle()) {
            titleBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.view_base_title, mBaseBinding!!.titleBar, false)
            titleBinding!!.titleText.text=initTitle()
            mBaseBinding.titleBar.addView(titleBinding!!.root)
            titleBinding!!.imgBtnBack.setOnClickListener { finish() }
        }
        mBaseBinding.contentView.addView(initContentView())

    }

    abstract fun initContentView(): View?

    /**
     * @return true 有标题（统一标题） false 没标题(自定义标题)
     */
    abstract fun isHasTitle(): Boolean

    abstract fun initTitle(): String?

    fun addTitleRightView(view: View) {
        //做空判断
        titleBinding?.titleRight?.addView(view)

    }

    /**
     * 显示正在加载网络的dialog
     */
    override fun showLoadingDialog() {
        loadingDialog=LoadingDialog(this)
        loadingDialog?.show()

    }

    /**
     * 关闭正在加载网络的dialog
     */
    override fun dismissLoadingDialog() {
        loadingDialog?.dismiss()
    }

    override fun onDestroy() {
        super.onDestroy()
        dismissLoadingDialog()
        mDisposable.clear()
    }
}
