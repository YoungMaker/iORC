package edu.ycp.cs482.iorc.Apollo.Query.Exception

/**
 * Created by Aaron Walsh on 5/5/2018.
 */
open class QueryException(message: String): Exception(message)

class AuthQueryException(
        message: String,
        val userId: String) : QueryException(message)

class InvalidQueryInputException(message: String): QueryException(message)

class ServerCommunicationError(message: String): QueryException(message)

