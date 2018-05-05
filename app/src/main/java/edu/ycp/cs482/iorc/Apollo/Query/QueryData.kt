package edu.ycp.cs482.iorc.Apollo.Query

import com.google.gson.Gson

data class QueryData(
        val gsonData:Gson,
        val inputs:HashMap<String, String>
)