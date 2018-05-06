package edu.ycp.cs482.iorc.Apollo.Query

import android.content.Context
import com.apollographql.apollo.ApolloMutationCall
import com.apollographql.apollo.ApolloQueryCall
import com.apollographql.apollo.api.Response
import edu.ycp.cs482.iorc.Apollo.Query.Exception.AuthQueryException
import edu.ycp.cs482.iorc.Apollo.Query.Exception.QueryException
import edu.ycp.cs482.iorc.CharacterUserQuery
import edu.ycp.cs482.iorc.LoginMutation
import edu.ycp.cs482.iorc.LogoutMutation
import edu.ycp.cs482.iorc.VersionSheetQuery

interface IQueryController {
    //Query constructors
    //FIXME: is actually mutation
    fun loginQuery(email: String, password: String): ApolloMutationCall<LoginMutation.Data>?
    @Throws(AuthQueryException::class)
    fun parseLoginQuery(context: Context, response: Response<LoginMutation.Data>)

    fun logoutMuation(context: Context): ApolloMutationCall<LogoutMutation.Data>?
    @Throws(QueryException::class)
    fun logoutMutationParse(response: Response<LogoutMutation.Data>, context: Context): String?

    fun userCharactersQuery(context: Context): ApolloQueryCall<CharacterUserQuery.Data>?
    @Throws(QueryException::class, AuthQueryException::class)
    fun parseUserCharactersQuery(context: Context, response: Response<CharacterUserQuery.Data>): QueryData?

    fun versionQuery(version: String, context: Context): ApolloQueryCall<VersionSheetQuery.Data>?
    @Throws(QueryException::class, AuthQueryException::class)
    fun parseVersionQuery(version: String, context: Context, response: Response<VersionSheetQuery.Data>): QueryData?
}