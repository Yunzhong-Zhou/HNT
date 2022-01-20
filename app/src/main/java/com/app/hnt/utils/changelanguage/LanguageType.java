package com.app.hnt.utils.changelanguage;

/**
 * 语言全局变量
 */
public enum LanguageType {

    CHINESE("zh"),//中文
    ENGLISH("en"),//英文
    JAPANESE("ja"),//日语
    KOREAN("ko"),//韩语
    VIETNAMESE("vi"),//越南语
    THAILAND("th");//泰语

    private String language;

    LanguageType(String language) {
        this.language = language;
    }

    public String getLanguage() {
        return language == null ? "" : language;
    }
}
