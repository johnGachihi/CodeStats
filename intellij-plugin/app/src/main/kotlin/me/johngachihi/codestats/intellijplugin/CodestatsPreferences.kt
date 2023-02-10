package me.johngachihi.codestats.intellijplugin

import com.intellij.openapi.components.*
import com.intellij.util.xmlb.XmlSerializerUtil

@State(
    name = "me.johngachihi.codestats.CodestatsPreferences",
    storages = [Storage("codestats.xml")]
)
@Service
class CodestatsPreferences : PersistentStateComponent<CodestatsPreferences> {
    companion object {
        fun getInstance() = service<CodestatsPreferences>()
    }

    var username: String? = null

    override fun getState(): CodestatsPreferences = this

    override fun loadState(state: CodestatsPreferences) {
        XmlSerializerUtil.copyBean(state, this)
    }
}