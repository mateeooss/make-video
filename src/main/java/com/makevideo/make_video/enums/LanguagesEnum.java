package com.makevideo.make_video.enums;

import lombok.Getter;

@Getter
public enum LanguagesEnum {
    EN_US("EN-US", "English"),
    PT_BR("PT-BR", "Portuguese"),
    ES_ES("ES-ES", "Spanish"),
    FR_FR("FR-FR", "French"),
    DE_DE("DE-DE", "German"),
    ZH_CN("ZH-CN", "Mandarin Chinese"),
    JA_JP("JA-JP", "Japanese"),
    RU_RU("RU-RU", "Russian"),
    AR_SA("AR-SA", "Arabic"),
    HI_IN("HI-IN", "Hindi"),
    IT_IT("IT-IT", "Italian"),
    KO_KR("KO-KR", "Korean"),
    NL_NL("NL-NL", "Dutch"),
    TR_TR("TR-TR", "Turkish"),
    PL_PL("PL-PL", "Polish"),
    TH_TH("TH-TH", "Thai"),
    VI_VN("VI-VN", "Vietnamese"),
    HE_IL("HE-IL", "Hebrew"),
    SV_SE("SV-SE", "Swedish"),
    NO_NO("NO-NO", "Norwegian"),
    DA_DK("DA-DK", "Danish"),
    FI_FI("FI-FI", "Finnish"),
    CS_CZ("CS-CZ", "Czech"),
    HU_HU("HU-HU", "Hungarian"),
    ID_ID("ID-ID", "Indonesian"),
    MS_MY("MS-MY", "Malay"),
    UK_UA("UK-UA", "Ukrainian"),
    RO_RO("RO-RO", "Romanian"),
    BG_BG("BG-BG", "Bulgarian"),
    HR_HR("HR-HR", "Croatian"),
    SK_SK("SK-SK", "Slovak"),
    EL_GR("EL-GR", "Greek"),
    FA_IR("FA-IR", "Persian (Farsi),"),
    TA_IN("TA-IN", "Tamil"),
    TE_IN("TE-IN", "Telugu"),
    BN_IN("BN-IN", "Bengali"),
    MY_MM("MY-MM", "Burmese"),
    TL_PH("TL-PH", "Tagalog"),
    SW_KE("SW-KE", "Swahili");


    private String acronym;
    private String name;

    LanguagesEnum(String acronym, String name) {
        this.acronym = acronym;
        this.name = name;
    }
}
