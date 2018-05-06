package edu.ycp.cs482.iorc.Apollo.Query

import android.content.Context
import com.apollographql.apollo.ApolloMutationCall
import com.apollographql.apollo.ApolloQueryCall
import com.apollographql.apollo.api.Response
import edu.ycp.cs482.iorc.Apollo.Query.Exception.AuthQueryException
import edu.ycp.cs482.iorc.Apollo.Query.Exception.QueryException
import edu.ycp.cs482.iorc.CharacterUserQuery
import edu.ycp.cs482.iorc.LoginMutation

interface IQueryController {
    //Query constructiors
    fun loginQuery(email: String, password: String): ApolloMutationCall<LoginMutation.Data>?
    @Throws(AuthQueryException::class)
    fun parseLoginQuery(context: Context, response: Response<LoginMutation.Data>)

    fun userCharactersQuery(userID:String, context: Context): ApolloQueryCall<CharacterUserQuery.Data>?
    @Throws(QueryException::class, AuthQueryException::class)
    fun parseUserCharactersQuery(userID: String, context: Context, response: Response<CharacterUserQuery.Data>): QueryData?
}