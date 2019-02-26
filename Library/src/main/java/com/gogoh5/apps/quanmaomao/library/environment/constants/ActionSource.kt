package com.gogoh5.apps.quanmaomao.library.environment.constants

var codeCount = 0

enum class ActionSource(
    val code: Int
) {
    DEFAULT(codeCount ++),
    MAIN_HOME(codeCount ++),
    MAIN_SEARCH(codeCount ++),
    MAIN_CART(codeCount ++),
    MAIN_ME(codeCount ++),
    MAIN_PAGE_CHANGE(codeCount ++),

    MAIN_SEARCH_BACK(codeCount ++),
    MAIN_SEARCH_SEARCH(codeCount ++),
    MAIN_SEARCH_DEL(codeCount ++),
    MAIN_SEARCH_HISTORY_ITEM(codeCount ++),
    MAIN_SEARCH_HOT_ITEM(codeCount ++),


    MAIN_CART_BACK(codeCount ++),

    MAIN_ME_ERROR(codeCount ++),
    MAIN_ME_SETTING(codeCount ++),
    MAIN_ME_REWARD(codeCount ++),
    MAIN_ME_ORDER(codeCount ++),
    MAIN_ME_BALANCE(codeCount ++),
    MAIN_ME_CART(codeCount ++),
    MAIN_ME_ALI_AUTH(codeCount ++),




    SEARCH_BACK(codeCount ++),
    SEARCH_BAR(codeCount ++),

    BALANCE_DETAIL_BACK(codeCount ++),


    PRODUCT_DETAIL_BACK(codeCount ++),
    PRODUCT_DETAIL_ERROR(codeCount ++),
    PRODUCT_DETAIL_HELP(codeCount ++),
    PRODUCT_DETAIL_COUPON_BUY(codeCount ++),
    PRODUCT_DETAIL_BOTTOM_BUY(codeCount ++),
    PRODUCT_DETAIL_BOTTOM_HOME(codeCount ++),
    PRODUCT_DETAIL_BOTTOM_CART(codeCount ++),


    WEB_BACK(codeCount ++),
    WEB_CLOSE(codeCount ++),
    WEB_ERROR(codeCount ++),

    CASH_BACK(codeCount ++),
    CASH_BALANCE(codeCount ++),
    CASH_BALANCE_ALL(codeCount ++),
    CASH_CASH(codeCount ++),

    CASH_LIST_BACK(codeCount ++),

    ALI_AUTH_DIALOG_BACK(codeCount ++),
    ALI_AUTH_DIALOG_PROTOCOL(codeCount ++),
    ALI_AUTH_DIALOG_AGREE(codeCount ++),

    SETTING_BACK(codeCount ++),
    SETTING_WX_AUTH(codeCount ++),
    SETTING_CACHE(codeCount ++),
    SETTING_WX_QUIT(codeCount ++)
}