package com.soutosss.marvelpoc.data.mappers

import com.soutosss.marvelpoc.data.Comics

class ComicsMapper {
    fun apply(comics: List<Comics>): List<com.soutosss.marvelpoc.data.model.view.Comics> =
        comics.map {
            com.soutosss.marvelpoc.data.model.view.Comics(
                title = it.title,
                thumbnailUrl = it.imageUrl
            )
        }
}
