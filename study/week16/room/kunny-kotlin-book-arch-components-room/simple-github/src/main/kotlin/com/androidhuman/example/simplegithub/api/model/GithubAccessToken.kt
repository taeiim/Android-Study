package com.androidhuman.example.simplegithub.api.model

import com.google.gson.annotations.SerializedName

class GithubAccessToken(
        @SerializedName("access_token") val accessToken: String,
        val scope: String,
        @SerializedName("token_type") val tokenType: String)
