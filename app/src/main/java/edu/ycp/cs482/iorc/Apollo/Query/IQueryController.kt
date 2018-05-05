package edu.ycp.cs482.iorc.Apollo.Query

interface IQueryController {
    fun loginQuery(email:String, password:String)
    fun userCharactersQuery(userID:String):QueryData
}