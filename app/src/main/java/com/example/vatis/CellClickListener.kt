package com.example.vatis

import com.example.vatis.items.*

interface CellClickListener {
    fun onCellClickListener(data: FolderItem) {}
    fun onCellClickListener(data: PlanItem){}
    fun onCellClickListener(data: BookmarkItem) {}
    fun onCellClickListener(data: MemoSubItem) {}
    fun onCellClickListener(data: RecommendationItem) {}
    fun onCellClickListener(data: TemplateItem) {}

}