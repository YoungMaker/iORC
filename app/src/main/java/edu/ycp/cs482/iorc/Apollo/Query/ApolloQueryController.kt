package edu.ycp.cs482.iorc.Apollo.Query


import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Error
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.api.cache.http.HttpCachePolicy
import com.apollographql.apollo.exception.ApolloException
import edu.ycp.cs482.iorc.Apollo.MyApolloClient
import edu.ycp.cs482.iorc.Apollo.Query.Exception.AuthQueryException
import edu.ycp.cs482.iorc.Apollo.Query.Exception.QueryException
import android.content.Context.MODE_PRIVATE
import android.R.id.edit
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.apollographql.apollo.ApolloMutationCall
import com.apollographql.apollo.ApolloQueryCall
import com.google.gson.Gson
import edu.ycp.cs482.iorc.*
import edu.ycp.cs482.iorc.Apollo.Query.Exception.ServerCommunicationError
import edu.ycp.cs482.iorc.type.AbilityInput
import edu.ycp.cs482.iorc.type.ObjType
import java.security.AuthProvider
import edu.ycp.cs482.iorc.type.Context as ContextQL



class ApolloQueryCntroller: IQueryController {
    //TODO: Re-enable HTTP caching
    //TODO: move error processing to private function?
    private val PREFS_FILE = "iorctkfile"

    override fun userInfoQuery(email: String, context: Context): ApolloQueryCall<UserDataQuery.Data> {
        return MyApolloClient.getMyApolloClient().query(
                UserDataQuery.builder().email(email).context(getQueryContext(context)).build()
        )
    }

    override fun parseUserInfoQuery(email: String, response: Response<UserDataQuery.Data>): QueryData {
        if(!response.errors().isEmpty()){
            var errStr = ""
            for (error in response.errors()) {
                errStr += error.message()!!.replace(Regex("Exception while fetching data \\(.*\\)\\s:"), "") + ", "
            }
            errStr = errStr.removeSuffix(",") //remove the last ,
            if(errStr.contains("Invalid Token!") || errStr.contains("banned")){
                throw AuthQueryException(errStr)
            }
            else {throw QueryException(errStr)}
        } else if(response.data() != null) {
            return QueryData(Gson().toJson(response.data()!!.userInfo), mapOf(Pair("email", email)))
        } else {
            throw QueryException("Invalid Response!")
        }
    }


    override fun createAccountMutation(email: String, password: String, uname: String): ApolloMutationCall<CreateAccountMutation.Data>? {
       return MyApolloClient.getMyApolloClient().mutate(
               CreateAccountMutation.builder().email(email).password(password).uname(uname).build()
       )
    }

    override fun parseCreateAccountMutation(response: Response<CreateAccountMutation.Data>): QueryData? {
        if(response.data() == null || response.data()!!.createUser() == null){
            if(!response.errors().isEmpty()){
                var errStr = ""
                for (error in response.errors()){
                    errStr += error.message()!!.replace(Regex("Exception while fetching data \\(.*\\)\\s:"), "") + ", "
                }
                errStr = errStr.removeSuffix(",")
                throw QueryException(errStr) // throws query exception
            }
            throw QueryException("Invalid Response!")
        }
        else {
            return QueryData(Gson().toJson(response.data()!!.createUser()), mapOf())
        }
    }

    override fun versionQuery(version: String, context: Context): ApolloQueryCall<VersionSheetQuery.Data>? {
        return MyApolloClient.getMyApolloClient().query(
                VersionSheetQuery.builder().version(version).context(getQueryContext(context)).build())
    }

    override fun parseVersionQuery(version: String, context: Context, response: Response<VersionSheetQuery.Data>): QueryData? {
        if(response.data() == null || response.data()!!.versionSheet == null) {
            if(response.errors().isEmpty()) { throw  QueryException("Invalid Response!")}
            else if(response.errors()[0].message()!!.contains("Invalid Token!")) {
                throw AuthQueryException("Invalid Token")
            }
        } else {
            return QueryData(Gson().toJson(response.data()!!.versionSheet),
                    mapOf(Pair("version", version)))
        }
        return null
    }

    override fun logoutMuation(context: Context): ApolloMutationCall<LogoutMutation.Data>? {
        return MyApolloClient.getMyApolloClient().mutate(
                LogoutMutation.builder().context(getQueryContext(context)).build())
    }

    override fun logoutMutationParse(response: Response<LogoutMutation.Data>, context: Context): String? {
        val editor = context.getSharedPreferences(PREFS_FILE, MODE_PRIVATE).edit()
        if(response.data() == null || response.data()!!.logout() == null) {
            if(response.errors().isEmpty()){ throw  QueryException("Invalid Response!")}
            val err = response.errors()[0] //assumes we only have 1 error, which is true always but risky boi
            if(err.message()!!.contains("Invalid Token!")){
                throw QueryException("Invalid Token")
            } else {
                throw QueryException(err.message()!!)
            }
        } else {
            editor.clear() //clears current token from memory.
            editor.apply()
            return response.data()!!.logout()
        }
    }

    override fun loginQuery(email: String, password: String): ApolloMutationCall<LoginMutation.Data>? {
        return MyApolloClient.getMyApolloClient().mutate(
                LoginMutation.builder().email(email).password(password).build())
    }

    //stores token from resp in shared prefs
    override fun parseLoginQuery(email: String, context: Context, response: Response<LoginMutation.Data>){
        val editor = context.getSharedPreferences(PREFS_FILE, MODE_PRIVATE).edit()

        if(response.data() == null || response.data()!!.loginUser().token() == ""){
            if(!response.errors().isEmpty()) {
                var errStr = ""
                for (error in response.errors()) {
                    errStr += error.message()!!.replace(Regex("Exception while fetching data \\(.*\\)\\s:"), "") + ", "
                }
                errStr = errStr.removeSuffix(",")
                throw AuthQueryException(errStr) // throws query exception
            } else {
                throw AuthQueryException("Invalid Response!")
            }
        }else{
            //stores token in Shared Prefs
            val token =  response.data()!!.loginUser().token()
            if(token == "") { throw QueryException("Invalid response!") }
            editor.putString("ENC_TOKEN", token)
            editor.putString("USER_EMAIL", email)
            editor.apply()
        }
    }

    override fun userCharactersQuery(context: Context): ApolloQueryCall<CharacterUserQuery.Data>? {
        return MyApolloClient.getMyApolloClient().query(
                CharacterUserQuery.builder().context(getQueryContext(context)).build())

    }

    override fun parseUserCharactersQuery(context: Context, response: Response<CharacterUserQuery.Data>): QueryData?{
        if(response.data() == null){
            if(!response.errors().isEmpty()){
                if(response.errors()[0].message()!!.contains("Invalid Token!")) { throw AuthQueryException("Invalid Token, or user banned")}
                else { throw QueryException("user does not exist or is banned")}
            }
            throw QueryException("Query returned nothing")
        } else {
            return QueryData(Gson().toJson(response.data()!!.usersCharacters), mapOf())
        }
    }

    override fun deleteCharacterMutation(id: String, context: Context): ApolloMutationCall<DeleteCharacterMutation.Data> {
       return MyApolloClient.getMyApolloClient().mutate(
               DeleteCharacterMutation.builder().context(getQueryContext(context)).id(id).build()
       )
    }

    override fun parseDeleteCharacterMutation(id: String, response: Response<DeleteCharacterMutation.Data>): QueryData? {
        if(!response.errors().isEmpty()){
            var errStr = ""
            for (error in response.errors()) {
                errStr += error.message()!!.replace(Regex("Exception while fetching data \\(.*\\)\\s:"), "") + ", "
            }
            errStr = errStr.removeSuffix(",") //remove the last ,
            if(errStr.contains("Invalid Token!") || errStr.contains("banned")){
                throw AuthQueryException(errStr)
            }
            else {throw QueryException(errStr)}
        } else if(response.data() != null) {
            return QueryData(Gson().toJson(response.data()!!.deleteCharacter()), mapOf(Pair("id", id)))
        } else {
            throw QueryException("Invalid Response!")
        }
    }


    override fun versionRacesQuery(version:String, context:Context): ApolloQueryCall<RaceVersionQuery.Data>?{
        return MyApolloClient.getMyApolloClient().query(
                RaceVersionQuery.builder().version(version).context(getQueryContext(context)).build())
    }

    override fun parseRacesQuery(version: String, context:Context, response: Response<RaceVersionQuery.Data>): QueryData?{
        if(response.data() == null || response.data()!!.racesByVersion == null) {
            if(response.errors().isEmpty()) { throw  QueryException("Invalid Response!")}
            else if(response.errors()[0].message()!!.contains("Invalid Token!")) {
                throw AuthQueryException("Invalid Token")
            }
        } else {
            return QueryData(Gson().toJson(response.data()!!.racesByVersion),
                    mapOf(Pair("version", version)))
        }
        return null
    }

    override fun versionClassesQuery(version: String, context: Context): ApolloQueryCall<ClassVersionQuery.Data>?{
        return MyApolloClient.getMyApolloClient().query(
                ClassVersionQuery.builder().version(version).context(getQueryContext(context)).build()
        )
    }

    override fun parseClassesQuery(version: String, context: Context, response: Response<ClassVersionQuery.Data>):QueryData?{
        if(response.data() == null || response.data()!!.classesByVersion == null) {
            if(response.errors().isEmpty()) { throw  QueryException("Invalid Response!")}
            else if(response.errors()[0].message()!!.contains("Invalid Token!")) {
                throw AuthQueryException("Invalid Token")
            }
        } else {
            return QueryData(Gson().toJson(response.data()!!.classesByVersion),
                    mapOf(Pair("version", version)))
        }
        return null
    }

    override fun versionInfoTypeQuery(version: String, type: String, context: Context): ApolloQueryCall<VersionInfoTypeQuery.Data>? {
        return MyApolloClient.getMyApolloClient().query(
                VersionInfoTypeQuery.builder().version(version).type(type).context(getQueryContext(context)).build()
        )
    }

    override fun parseVersionInfoTypeQuery(version: String, type: String, response: Response<VersionInfoTypeQuery.Data>): QueryData? {
        if(response.data() == null || response.data()!!.versionInfoType == null) {
            if(response.errors().isEmpty()) { throw  QueryException("Invalid Response!")}
            else if(response.errors()[0].message()!!.contains("Invalid Token!")) {
                throw AuthQueryException("Invalid Token")
            }
        } else {
            return QueryData(Gson().toJson(response.data()!!.versionInfoType),
                    mapOf(Pair("version", version)))
        }
        return null
    }

    override fun createCharacterMutation(name: String, raceid: String, classid: String, version: String, context: Context, abilityInput: AbilityInput): ApolloMutationCall<CreateCharacterMutation.Data>? {
        return MyApolloClient.getMyApolloClient().mutate(
                CreateCharacterMutation.builder().name(name).version(version).abil(abilityInput)
                        .classid(classid).raceid(raceid).context(getQueryContext(context)).build()
        )
    }

    override fun parseCreateCharacterMutation(version: String, response: Response<CreateCharacterMutation.Data>): QueryData? {
        if(!response.errors().isEmpty()){
            var errStr = ""
            for (error in response.errors()) {
                errStr += error.message()!!.replace(Regex("Exception while fetching data \\(.*\\)\\s:"), "") + ", "
            }
            errStr.removeSuffix(",") //remove the last ,
            if(errStr.contains("Invalid Token!") || errStr.contains("banned")){
                throw AuthQueryException(errStr)
            }
            else {throw QueryException(errStr)}
        } else if(response.data() != null) {
            return QueryData(Gson().toJson(response.data()!!.createCharacter()), mapOf(Pair("version", version)))
        } else {
            throw QueryException("Invalid Response!")
        }
    }

    override fun getVersionItemsByType(type: ObjType, version: String, context: Context): ApolloQueryCall<VersionItemsByTypeQuery.Data>? {
        return MyApolloClient.getMyApolloClient().query(
                VersionItemsByTypeQuery.builder().type(type).version(version).context(getQueryContext(context)).build()
        )
    }

    override fun parseGetVersionItemsByType(version: String, response: Response<VersionItemsByTypeQuery.Data>): QueryData? {
        if(response.data() == null || response.data()!!.versionItemType == null) {
            if(response.errors().isEmpty()) { throw  QueryException("Invalid Response!")}
            else if(response.errors()[0].message()!!.contains("Invalid Token!")) {
                throw AuthQueryException("Invalid Token")
            }
        } else {
            return QueryData(Gson().toJson(response.data()!!.versionItemType),
                    mapOf(Pair("version", version)))
        }
        return null
    }

    override fun purchaseItemForCharacters(id: String, itemid: String, context: Context): ApolloMutationCall<PurchaseItemMutation.Data>? {
        return MyApolloClient.getMyApolloClient().mutate(
                PurchaseItemMutation.builder().id(id).itemID(itemid).context(getQueryContext(context)).build()
        )
    }

    override fun parsePurchaseItemForChar(id: String, response: Response<PurchaseItemMutation.Data>): QueryData? {
        if(!response.errors().isEmpty()){
            var errStr = ""
            for (error in response.errors()) {
                errStr += error.message()!!.replace(Regex("Exception while fetching data \\(.*\\)\\s:"), "") + ", "
            }
            errStr.removeSuffix(",") //remove the last ,
            if(errStr.contains("Invalid Token!") || errStr.contains("banned")){
                throw AuthQueryException(errStr)
            }
            else {throw QueryException(errStr)}
        } else if(response.data() != null) {
            return QueryData(Gson().toJson(response.data()!!.purchaseItem()), mapOf(Pair("id", id)))
        } else {
            throw QueryException("Invalid Response!")
        }
    }

    private fun getQueryContext(context: Context): ContextQL{
        val token = getToken(context) ?: throw AuthQueryException("No saved token!")
        return ContextQL.builder().token(token).build()
    }

    private fun getToken(context: Context): String? {
        val prefs = context.getSharedPreferences(PREFS_FILE, MODE_PRIVATE)
        return prefs.getString("ENC_TOKEN", null)
    }

}