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
import edu.ycp.cs482.iorc.CreateCharacterMutation
import edu.ycp.cs482.iorc.LoginMutation
import edu.ycp.cs482.iorc.R
import android.content.Context.MODE_PRIVATE
import android.R.id.edit
import android.content.Context
import android.content.SharedPreferences
import com.apollographql.apollo.ApolloMutationCall
import com.apollographql.apollo.ApolloQueryCall
import com.google.gson.Gson
import edu.ycp.cs482.iorc.Apollo.Query.Exception.ServerCommunicationError
import edu.ycp.cs482.iorc.CharacterUserQuery
import java.security.AuthProvider
import edu.ycp.cs482.iorc.type.Context as ContextQL



class ApolloQueryCntroller: IQueryController {
      private val PREFS_FILE = "iorctkfile"

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
            throw AuthQueryException(errStr, "") // throws query exception
        }else{
            //stores token in Shared Prefs
            val token =  response.data()!!.loginUser().token()
            if(token == "") { throw QueryException("Invalid response!") }
            editor.putString("ENC_TOKEN", token)
            editor.apply()
        }
    }

    override fun userCharactersQuery(userID: String, context: Context): ApolloQueryCall<CharacterUserQuery.Data>? {
        return MyApolloClient.getMyApolloClient().query(
                CharacterUserQuery.builder().context(getQueryContext(context)).build())

    }

    override fun parseUserCharactersQuery(userID: String, context: Context, response: Response<CharacterUserQuery.Data>): QueryData?{
        if(response.data() == null){
            if(!response.errors().isEmpty()){
                throw AuthQueryException("Invalid Token, or user banned", userID)
            }
            throw QueryException("Query returned nothing")
        } else {
            return QueryData(Gson().toJson(response.data()!!.usersCharacters), mapOf(Pair("userID", userID)))
        }
    }

    private fun getQueryContext(context: Context): ContextQL{
        val token = getToken(context) ?: throw AuthQueryException("No saved token!", "")
        return ContextQL.builder().token(token).build()
    }

    private fun getToken(context: Context): String? {
        val prefs = context.getSharedPreferences(PREFS_FILE, MODE_PRIVATE)
        return prefs.getString("ENC_TOKEN", null)
    }

}