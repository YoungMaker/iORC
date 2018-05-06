package edu.ycp.cs482.iorc.Apollo.Query

import com.google.gson.Gson

data class QueryData(
        val gsonData: String,
        val inputs: Map<String, String>
)