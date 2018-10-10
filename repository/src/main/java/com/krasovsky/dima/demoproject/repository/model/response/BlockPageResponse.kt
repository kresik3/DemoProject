package com.krasovsky.dima.demoproject.repository.model.response

import com.krasovsky.dima.demoproject.repository.model.TypePagePaging
import com.krasovsky.dima.demoproject.storage.model.BlockPage

class BlockPageResponse(val type: TypePagePaging, val data: BlockPage)