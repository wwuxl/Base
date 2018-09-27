package wxl.com.base.recycler

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View
import wxl.com.base.R

/**
 * @date Created time 2018/9/27 11:44
 * @author wuxiulin
 * @description 时间轴分割
 */
class TimelineItemDecoration(context: Context,color:Int) : RecyclerView.ItemDecoration() {
    private var mPaint:Paint
    //ItemView左边的间距
    private var mOffsetLeft:Float
    //ItemView上边的间距
    private var mOffsetTop:Float =0f
    //时间轴结点的半径
    private var mNodeRadius:Float

    init {
        mPaint = Paint()
        mPaint.isAntiAlias=true
        mPaint.color=color
        mOffsetLeft = context.resources.getDimension(R.dimen.timeline_item_offset_left)
        mNodeRadius = context.resources.getDimension(R.dimen.timeline_item_node_radius)
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        //        //第一个ItemView不需要在上面绘制分割线
        if (parent.getChildAdapterPosition(view) != 0) {
            //这里直接硬编码为1px
            outRect.top = 1
            mOffsetTop = 1f
        }

        outRect.left = mOffsetLeft.toInt()
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        val childCount = parent.childCount

        for (i in 0 until childCount) {
            val view = parent.getChildAt(i)

            val index = parent.getChildAdapterPosition(view)

            var dividerTop = view.top - mOffsetTop
            //第一个ItemView 没有向上方向的间隔
            if (index == 0) {
                dividerTop = view.top.toFloat()
            }

            val dividerLeft = parent.paddingLeft
            val dividerBottom = view.bottom.toFloat()
            val dividerRight = parent.width - parent.paddingRight

            val centerX = dividerLeft + mOffsetLeft / 2
            val centerY = dividerTop + (dividerBottom - dividerTop) / 2

            val upLineTopY = dividerTop
            val upLineBottomY = centerY - mNodeRadius

            //绘制上半部轴线
            c.drawLine(centerX, upLineTopY, centerX, upLineBottomY, mPaint)

            //绘制时间轴结点
            mPaint.style=Paint.Style.STROKE
            c.drawCircle(centerX, centerY, mNodeRadius, mPaint)
            mPaint.style=Paint.Style.FILL_AND_STROKE

            val downLineTopY = centerY + mNodeRadius

            //绘制上半部轴线
            c.drawLine(centerX, downLineTopY, centerX, dividerBottom, mPaint)
        }
    }
}