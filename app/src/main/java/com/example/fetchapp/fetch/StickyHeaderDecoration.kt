/*
 * Copyright 2014 Eduardo Barrenechea
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.fetchapp.fetch

import android.graphics.Canvas
import android.graphics.Rect
import android.util.LongSparseArray
import android.util.SparseArray
import android.view.View
import android.view.View.MeasureSpec
import android.view.View.MeasureSpec.makeMeasureSpec
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

/** A sticky header decoration for android's RecyclerView.  */
class StickyHeaderDecoration @JvmOverloads constructor(
    private val adapter: StickyHeaderAdapter,
    private val renderInline: Boolean = false
) : ItemDecoration() {

    // viewType -> header id -> holder
    private val headerCache = SparseArray<LongSparseArray<RecyclerView.ViewHolder>>()

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val headerHeight = if (position != RecyclerView.NO_POSITION &&
            hasHeader(position) &&
            showHeaderAboveItem(position)
        ) {
            getHeaderHeightForLayout(getHeader(parent, position).itemView)
        } else {
            0
        }
        outRect[0, headerHeight, 0] = 0
    }

    private fun showHeaderAboveItem(itemAdapterPosition: Int): Boolean {
        return if (itemAdapterPosition == 0) {
            true
        } else {
            adapter.getHeaderId(itemAdapterPosition - 1) != adapter.getHeaderId(itemAdapterPosition)
        }
    }

    /**
     * Clears the header view cache. Headers will be recreated and rebound on list scroll after this
     * method has been called.
     */
    fun clearHeaderCache() {
        headerCache.clear()
    }

    private fun hasHeader(position: Int): Boolean {
        return adapter.getHeaderId(position) != StickyHeaderAdapter.NO_HEADER_ID
    }

    private fun getHeader(parent: RecyclerView, position: Int): RecyclerView.ViewHolder {
        val viewType = adapter.getHeaderViewType(position)
        val headerId = adapter.getHeaderId(position)
        val headers = headerCache.getOrPut(viewType) { LongSparseArray() }
        return headers.getOrPut(headerId) {
            adapter.onCreateHeaderViewHolder(parent, viewType).also { holder ->
                adapter.onBindHeaderViewHolder(holder, position)
                val header = holder.itemView
                val widthSpec = makeMeasureSpec(parent.measuredWidth, MeasureSpec.EXACTLY)
                val heightSpec = makeMeasureSpec(parent.measuredHeight, MeasureSpec.UNSPECIFIED)
                val childWidth = ViewGroup.getChildMeasureSpec(
                    widthSpec,
                    parent.paddingLeft + parent.paddingRight,
                    header.layoutParams.width
                )
                val childHeight = ViewGroup.getChildMeasureSpec(
                    heightSpec,
                    parent.paddingTop + parent.paddingBottom,
                    header.layoutParams.height
                )
                header.measure(childWidth, childHeight)
                header.layout(0, 0, header.measuredWidth, header.measuredHeight)
            }
        }
    }

    /** {@inheritDoc}  */
    override fun onDrawOver(
        canvas: Canvas,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val count = parent.childCount
        var previousHeaderId: Long = -1
        for (layoutPos in 0 until count) {
            val child = parent.getChildAt(layoutPos)
            val adapterPos = parent.getChildAdapterPosition(child)
            if (adapterPos != RecyclerView.NO_POSITION && hasHeader(adapterPos)) {
                val headerId = adapter.getHeaderId(adapterPos)
                if (headerId != previousHeaderId) {
                    previousHeaderId = headerId
                    val header = getHeader(parent, adapterPos).itemView
                    canvas.save()
                    val left = child.left
                    val top = getHeaderTop(parent, child, header, adapterPos, layoutPos)
                    canvas.translate(left.toFloat(), top.toFloat())
                    header.translationX = left.toFloat()
                    header.translationY = top.toFloat()
                    header.draw(canvas)
                    canvas.restore()
                }
            }
        }
    }

    private fun getHeaderTop(
        parent: RecyclerView,
        child: View,
        header: View,
        adapterPos: Int,
        layoutPos: Int
    ): Int {
        val headerHeight = getHeaderHeightForLayout(header)
        var top = child.y.toInt() - headerHeight
        if (layoutPos == 0) {
            val count = parent.childCount
            val currentId = adapter.getHeaderId(adapterPos)
            // find next view with header and compute the offscreen push if needed
            for (i in 1 until count) {
                val adapterPosHere = parent.getChildAdapterPosition(parent.getChildAt(i))
                if (adapterPosHere != RecyclerView.NO_POSITION) {
                    val nextId = adapter.getHeaderId(adapterPosHere)
                    if (nextId != currentId) {
                        val next = parent.getChildAt(i)
                        val headerHere = getHeader(parent, adapterPos).itemView
                        val offset = next.y.toInt() - (headerHeight + headerHere.height)
                        return if (offset < 0) {
                            offset
                        } else {
                            break
                        }
                    }
                }
            }
            top = top.coerceAtLeast(0)
        }
        return top
    }

    private fun getHeaderHeightForLayout(header: View): Int {
        return if (renderInline) 0 else header.height
    }

    /**
     * Returns the value for the given key. If the key is not found in the map, calls the
     * [defaultValue] function, puts its result into the map under the given key and returns it.
     */
    inline fun <T : Any> SparseArray<T>.getOrPut(key: Int, defaultValue: () -> T): T {
        val value = get(key)
        return if (value == null) {
            val answer = defaultValue()
            put(key, answer)
            answer
        } else {
            value
        }
    }

    /**
     * Returns the value for the given key. If the key is not found in the map, calls the
     * [defaultValue] function, puts its result into the map under the given key and returns it.
     */
    inline fun <T : Any> LongSparseArray<T>.getOrPut(key: Long, defaultValue: () -> T): T {
        val value = get(key)
        return if (value == null) {
            val answer = defaultValue()
            put(key, answer)
            answer
        } else {
            value
        }
    }
}
