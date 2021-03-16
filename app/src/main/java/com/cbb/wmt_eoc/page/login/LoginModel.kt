package com.cbb.wmt_eoc.page.login

import java.io.Serializable

/**
 * Created by 坎坎.
 * Date: 2019/12/24
 * Time: 22:40
 * describe:
 */
class LoginModel (var token:String?,var user:User){

    class User(var account:String,var mobile:String,var profilePic:String,
               var name:String,var roleId:Int?):Serializable{
        override fun toString(): String {
            return "User(account='$account', mobile='$mobile', profilePic='$profilePic', name='$name')"
        }
    }
//
    override fun toString(): String {
        return "LoginBean(token=$token, user=$user)"
    }
}