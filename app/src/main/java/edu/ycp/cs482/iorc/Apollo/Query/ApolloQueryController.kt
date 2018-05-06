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
import java.security.AuthProvider
import edu.ycp.cs482.iorc.type.Context as ContextQL



class ApolloQueryCntroller: IQueryController {
    //TODO: Re-enable HTTP caching
    private val PREFS_FILE = "iorctkfile"

    override fun versionQuery(version: String, context: Context): ApolloQueryCall<VersionSheetQuery.Data>? {
        return MyApolloClient.getMyApolloClient().query(
                VersionSheetQuery.builder().version(version).context(getQueryContext(context)).build())
    }

    override fun parseVersionQuery(version: String, context: Context, response: Response<VersionSheetQuery.Data>): QueryData? {
        if(response.data() == null) {
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
        if(response.data() == null) {
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
    override fun parseLoginQuery(context: Context, response: Response<LoginMutation.Data>){
        val editor = context.getSharedPreferences(PREFS_FILE, MODE_PRIVATE).edit()

        if(response.data() == null){
            var errStr = ""
            for (error in response.errors()){
                errStr += error.message() + ", "
            }
            throw AuthQueryException(errStr) // throws query exception
        }else{
            //stores token in Shared Prefs
            val token =  response.data()!!.loginUser().token()
            if(token == "") { throw QueryException("Invalid response!") }
            editor.putString("ENC_TOKEN", token)
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

    private fun getQueryContext(context: Context): ContextQL{
        val token = getToken(context) ?: throw AuthQueryException("No saved token!")
        return ContextQL.builder().token(token).build()
    }

    private fun getToken(context: Context): String? {
        val prefs = context.getSharedPreferences(PREFS_FILE, MODE_PRIVATE)
        return prefs.getString("ENC_TOKEN", null)
    }

}