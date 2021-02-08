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

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * The adapter to assist the [com.banksimple.view.StickyHeaderDecoration] in creating and
 * binding the header views.
 *
 * @param <T> the header view holder
</T> */
interface StickyHeaderAdapter {
    /**
     * Returns the header id for the item at the given position.
     *
     * @param position the item position
     * @return the header id
     */
    fun getHeaderId(position: Int): Long

    /**
     * Return the header view type for the given position.
     * @param position the item position.
     * @return the header view type
     */
    fun getHeaderViewType(position: Int): Int = 0

    /**
     * Creates a new header ViewHolder.
     *
     * @param parent the header's view parent
     * @return a view holder for the created view
     */
    fun onCreateHeaderViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder

    /**
     * Updates the header view to reflect the header data for the given position
     *
     * @param holder the header view holder
     * @param position the header's item position
     */
    fun onBindHeaderViewHolder(holder: RecyclerView.ViewHolder, position: Int)

    companion object {
        const val NO_HEADER_ID = -1L
    }
}
