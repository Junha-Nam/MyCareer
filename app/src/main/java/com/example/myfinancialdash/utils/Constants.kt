package com.example.myfinancialdash.utils

class KorStockConstants {
    companion object{
        const val API_KEY_KOR_STOCK = ""
        const val BASE_URL_KOR_STOCK = ""

    }
}

class UsdStockConstants {
    companion object {
        const val API_KEY_USD_STOCK = ""
        const val BASE_URL_USD_STOCK = ""
        const val BASE_URL_USD_INFORMATION = ""
    }
}

class CryptoConstants {
    companion object {
        const val API_KEY_CRYPTO = ""
        const val BASE_URL_CRYPTO = "https://api.upbit.com/v1/"
    }
}

class IndexConstants {
    companion object {
        const val API_KEY_INDEX = ""
        const val BASE_URL_INDEX = "https://polling.finance.naver.com/api/realtime/"
        const val BASE_URL_DOLLAR = "https://api.stock.naver.com/marketindex/exchange/"
        const val BASE_URL_10YT = "https://api.stock.naver.com/marketindex/bond/"
    }
}

class SearchConstants {
    companion object {
        const val BASE_URL_SEARCH = "https://ac.stock.naver.com/"
        const val TEST = "https://ac.stock.naver.com/ac?q=애플&target=stock,index,marketindicator/"
    }
}