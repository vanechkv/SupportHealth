package com.example.supporthealth.nutrition.main.domain.models

enum class Module(val code: Int) {
    NUTRITION(1),
    ACTIVITY(2),
    MOOD(3),
    HABITS(4);

    companion object {
        private val map = values().associateBy(Module::code)
        fun fromCode(code: Int) = map[code]
    }
}