package com.gs.buluo.store.bean

import com.gs.buluo.store.bean.ResponseBody.IBaseResponse

/**
 * Created by hjn on 2016/11/18.
 */
class WalletAccount(var id: String, var balance: Float, var withdrawCharge: Float, var state: STATUS, var lastTrading: String, var password: String
,var accountType: AccountType) : IBaseResponse {
    enum class STATUS private constructor(internal var status: String) {
        NORMAL("NORMAL"), LOCKED("LOCKED")
    }

    enum class AccountType private constructor(){
        CARD,PROTOCOL
    }
}
