package edu.ycp.cs482.iorc.Apollo.Query

import android.content.Context
import com.apollographql.apollo.ApolloMutationCall
import com.apollographql.apollo.ApolloQueryCall
import com.apollographql.apollo.api.Response
import edu.ycp.cs482.iorc.*
import edu.ycp.cs482.iorc.Apollo.Query.Exception.AuthQueryException
import edu.ycp.cs482.iorc.Apollo.Query.Exception.QueryException

interface IQueryController {
    //Query constructors
    //FIXME: is actually mutation
    fun loginQuery(email: String, password: String): ApolloMutationCall<LoginMutation.Data>?
    @Throws(AuthQueryException::class)
    fun parseLoginQuery(context: Context, response: Response<LoginMutation.Data>)

    @Throws(AuthQueryException::class)
    fun logoutMuation(context: Context): ApolloMutationCall<LogoutMutation.Data>?
    @Throws(QueryException::class, AuthQueryException::class)
    fun logoutMutationParse(response: Response<LogoutMutation.Data>, context: Context): String?

    @Throws(AuthQueryException::class)
    fun userCharactersQuery(context: Context): ApolloQueryCall<CharacterUserQuery.Data>?
    @Throws(QueryException::class, AuthQueryException::class)
    fun parseUserCharactersQuery(context: Context, response: Response<CharacterUserQuery.Data>): QueryData?

    @Throws(AuthQueryException::class)
    fun versionQuery(version: String, context: Context): ApolloQueryCall<VersionSheetQuery.Data>?
    @Throws(QueryException::class, AuthQueryException::class)
    fun parseVersionQuery(version: String, context: Context, response: Response<VersionSheetQuery.Data>): QueryData?

    fun createAccountMutation(email: String, password: String, uname: String): ApolloMutationCall<CreateAccountMutation.Data>?
    @Throws(QueryException::class)
    fun parseCreateAccountMutation(response: Response<CreateAccountMutation.Data>): QueryData?
}