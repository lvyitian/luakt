package com.ooooonly.luakt

import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue

class LuaTableBuilder(var tableValue: LuaTable = LuaTable()) {
    infix fun String.to(value: Any) {
        tableValue[this] = value
    }

    infix fun Any.to(value: Any) {
        tableValue[this.asLuaValue()] = value
    }

    infix fun String.map(mapper: (LuaValue) -> Any) {
        tableValue[this] = mapper(tableValue[this]).asLuaValue()
    }

    infix fun Any.map(mapper: (LuaValue) -> Any) {
        tableValue[this] = mapper(tableValue[this]).asLuaValue()
    }

    operator fun LuaValue.unaryPlus() {
        tableValue.insert(tableValue.keyCount(), this)
    }

    fun get(key: String) = tableValue.rawget(key)

    fun get(key: Any) = tableValue.rawget(key.asLuaValue())
}

fun Collection<LuaValue>.asLuaTable() = LuaTable.tableOf(this.toTypedArray())
fun Array<LuaValue>.asLuaTable() = LuaTable.tableOf(this)
fun Map<Any, Any>.asLuaTable() = LuaTable().apply {
    this@asLuaTable.forEach { key, value ->
        rawset(key.asLuaValue(), value.asLuaValue())
    }
}

inline fun luaTableOf(builder: LuaTableBuilder.() -> Unit): LuaTable = LuaTableBuilder()
    .apply { builder() }.tableValue

fun luaTableOf() = LuaTable.tableOf()
fun luaTableOf(map: Map<Any, Any>) = map.asLuaTable()
fun luaTableOf(array: Array<LuaValue>) = array.asLuaTable()
fun luaTableOf(collection: Collection<LuaValue>) = collection.asLuaTable()

inline fun LuaTable.edit(editor: LuaTableBuilder.() -> Unit) = LuaTableBuilder(
    this
).editor()



