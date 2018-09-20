package wxl.com.base.recycler

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View
import wxl.com.base.utils.UIUtil

class LineItemDecoration(lineColor: Int, var lineWidthDp: Int, var leftDisDp: Int = 0, var rightDisDp: Int = 0) : RecyclerView.ItemDecoration() {

    private var mPaint: Paint = Paint()

    init {
        mPaint.color = lineColor
        mPaint.style = Paint.Style.FILL_AND_STROKE
        mPaint.strokeWidth = UIUtil.dip2px(lineWidthDp).toFloat()
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        var childCount = parent.childCount

        for (i in 1 until childCount) {
            val view = parent.getChildAt(i)

            val index = parent.getChildAdapterPosition(view)

            var dividerTop = view.top - UIUtil.dip2px(lineWidthDp)
            //第一个ItemView 没有向上方向的间隔
            if (index == 0) {
                dividerTop = view.top
            }
            val dividerLeft = parent.paddingLeft
            val dividerBottom = view.bottom.toFloat()
            val dividerRight = parent.width - parent.paddingRight
            val y = dividerTop.toFloat() + UIUtil.dip2px(lineWidthDp) / 2f
            val leftX = dividerLeft.toFloat() + UIUtil.dip2px(leftDisDp)
            val rightX = dividerRight.toFloat() - UIUtil.dip2px(rightDisDp)
            c.drawLine(leftX, y, rightX, y, mPaint)

        }

    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        if (parent.getChildAdapterPosition(view) != 0) {
            outRect.top = UIUtil.dip2px(lineWidthDp)
        }


    }

}