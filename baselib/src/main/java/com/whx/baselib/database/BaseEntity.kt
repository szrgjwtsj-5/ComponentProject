package com.whx.baselib.database

import io.objectbox.annotation.BaseEntity
import io.objectbox.annotation.Id

@BaseEntity
abstract class BaseEntity {
    @Id(assignable = true)
    var id: Long = 0
}