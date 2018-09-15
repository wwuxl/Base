package wxl.com.base.recycler

import android.databinding.ViewDataBinding
import android.view.ViewGroup

/**
 * @date Created time 2018/9/11 10:09
 * @author wuxiulin
 * @description 适配器调用者实现此接口
 */
interface RIAdapter<T> {
    /**
     * 调用者创建布局
     */
    fun onCreateView(parent: ViewGroup, viewType: Int):ViewDataBinding
    /**
     * 调用者绑定数据
     */
    fun onBindView(data:T,binding:ViewDataBinding,position:Int)


}