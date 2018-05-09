package edu.ycp.cs482.iorc.Apollo.Query

import android.content.Context
import android.provider.ContactsContract
import com.apollographql.apollo.ApolloMutationCall
import com.apollographql.apollo.ApolloQueryCall
import com.apollographql.apollo.api.Response
import edu.ycp.cs482.iorc.*
import edu.ycp.cs482.iorc.Apollo.Query.Exception.AuthQueryException
import edu.ycp.cs482.iorc.Apollo.Query.Exception.QueryException
import edu.ycp.cs482.iorc.type.AbilityInput
import edu.ycp.cs482.iorc.type.ObjType

interface IQueryController {
    //Query constructors
    //FIXME: is actually mutation
    fun loginQuery(email: String, password: String): ApolloMutationCall<LoginMutation.Data>?
    @Throws(AuthQueryException::class)
    fun parseLoginQuery(email: String, context: Context, response: Response<LoginMutation.Data>)

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

    @Throws(AuthQueryException::class)
    fun versionRacesQuery(version:String, context:Context): ApolloQueryCall<RaceVersionQuery.Data>?
    @Throws(QueryException::class, AuthQueryException::class)
    fun parseRacesQuery(version:String, context:Context, response: Response<RaceVersionQuery.Data>): QueryData?

    @Throws(AuthQueryException::class)
    fun versionClassesQuery(version: String, context: Context): ApolloQueryCall<ClassVersionQuery.Data>?
    @Throws(QueryException::class, AuthQueryException::class)
    fun parseClassesQuery(version: String, context: Context, response: Response<ClassVersionQuery.Data>):QueryData?

    @Throws(AuthQueryException::class)
    fun deleteCharacterMutation(id: String, context: Context): ApolloMutationCall<DeleteCharacterMutation.Data>
    @Throws(AuthQueryException::class, QueryException::class)
    fun parseDeleteCharacterMutation(id: String, response: Response<DeleteCharacterMutation.Data>): QueryData?
    
    @Throws(AuthQueryException::class)
    fun versionInfoTypeQuery(version: String, type: String, context: Context): ApolloQueryCall<VersionInfoTypeQuery.Data>?
    @Throws(QueryException::class, AuthQueryException::class)
    fun parseVersionInfoTypeQuery(version: String, type: String, response: Response<VersionInfoTypeQuery.Data>): QueryData?

    @Throws(AuthQueryException::class)
    fun userInfoQuery(email: String, context: Context): ApolloQueryCall<UserDataQuery.Data>
    @Throws(AuthQueryException::class, QueryException::class)
    fun parseUserInfoQuery(email: String, response: Response<UserDataQuery.Data>): QueryData

    @Throws(AuthQueryException::class)
    fun createCharacterMutation(name:String, raceid:String, classid:String, version:String, context: Context, abilityInput: AbilityInput): ApolloMutationCall<CreateCharacterMutation.Data>?
    @Throws(QueryException::class, AuthQueryException::class)
    fun parseCreateCharacterMutation(version: String, response: Response<CreateCharacterMutation.Data>): QueryData?

    @Throws(AuthQueryException::class)
    fun getVersionItemsByType(type: ObjType, version: String, context: Context): ApolloQueryCall<VersionItemsByTypeQuery.Data>?
    @Throws(QueryException::class, AuthQueryException::class)
    fun parseGetVersionItemsByType(version: String, response: Response<VersionItemsByTypeQuery.Data>): QueryData?

    @Throws(AuthQueryException::class)
    fun purchaseItemForCharacters(id: String, itemid: String, context: Context): ApolloMutationCall<PurchaseItemMutation.Data>?
    @Throws(QueryException::class, AuthQueryException::class)
    fun parsePurchaseItemForChar(id: String, response: Response<PurchaseItemMutation.Data>): QueryData?

    @Throws(AuthQueryException::class)
    fun updateCharMutation(id: String, name: String, abil:AbilityInput, raceid: String, classid: String, context: Context): ApolloMutationCall<EditCharacterMutation.Data>?
    @Throws(QueryException::class, AuthQueryException::class)
    fun parseUpdateCharMutation(name: String, abil: AbilityInput, response: Response<EditCharacterMutation.Data>): QueryData
}

