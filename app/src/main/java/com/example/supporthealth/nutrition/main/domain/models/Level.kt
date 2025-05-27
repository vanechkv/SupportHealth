package com.example.supporthealth.nutrition.main.domain.models

enum class Level(val code: Int) {
    SOFT(1),
    BASE(2),
    STRONG(3);

    companion object {
        private val map = Level.values().associateBy(Level::code)
        fun fromCode(code: Int) = map[code]
    }
}