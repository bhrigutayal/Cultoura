//package com.tourismclient.cultoura.repositories
//
//import com.tourismclient.cultoura.models.AuthResponse
//import com.tourismclient.cultoura.models.LoginRequest
//import com.tourismclient.cultoura.models.RefreshTokenRequest
//import com.tourismclient.cultoura.models.RegisterRequest
//
//import com.tourismclient.cultoura.utils.Result
//import com.tourismclient.cultoura.utils.TokenManager
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.withContext
//
///**
// * Repository for handling authentication-related API calls
// */
//class AuthRepository(private val apiService: ApiService = ApiService.getInstance()) {
//
//    /**
//     * Login with email and password
//     */
//    suspend fun login(email: String, password: String): Result<AuthResponse> = withContext(Dispatchers.IO) {
//        try {
//            val loginRequest = LoginRequest(email, password)
//            val response = apiService.login(loginRequest)
//
//            if (response.isSuccessful && response.body() != null) {
//                // Save tokens on successful login
//                response.body()?.let { authResponse ->
//                    TokenManager.saveTokens(
//                        authResponse.accessToken,
//                        authResponse.refreshToken,
//                        authResponse.expiresIn
//                    )
//                }
//                Result.Success(response.body()!!)
//            } else {
//                Result.Error("Login failed: ${response.message()}")
//            }
//        } catch (e: Exception) {
//            Result.Error("Network error: ${e.message}")
//        }
//    }
//
//    /**
//     * Register a new user
//     */
//    suspend fun register(name: String, email: String, password: String): Result<AuthResponse> =
//        withContext(Dispatchers.IO) {
//            try {
//                val registerRequest = RegisterRequest(name, email, password)
//                val response = apiService.register(registerRequest)
//
//                if (response.isSuccessful && response.body() != null) {
//                    // Save tokens on successful registration
//                    response.body()?.let { authResponse ->
//                        TokenManager.saveTokens(
//                            authResponse.accessToken,
//                            authResponse.refreshToken,
//                            authResponse.expiresIn
//                        )
//                    }
//                    Result.Success(response.body()!!)
//                } else {
//                    Result.Error("Registration failed: ${response.message()}")
//                }
//            } catch (e: Exception) {
//                Result.Error("Network error: ${e.message}")
//            }
//        }
//
//    /**
//     * Refresh access token using refresh token
//     */
//    suspend fun refreshToken(): Result<AuthResponse> = withContext(Dispatchers.IO) {
//        try {
//            val refreshToken = TokenManager.getRefreshToken()
//            if (refreshToken == null) {
//                return@withContext Result.Error("No refresh token available")
//            }
//
//            val refreshRequest = RefreshTokenRequest(refreshToken)
//            val response = apiService.refreshToken(refreshRequest)
//
//            if (response.isSuccessful && response.body() != null) {
//                response.body()?.let { authResponse ->
//                    TokenManager.saveTokens(
//                        authResponse.accessToken,
//                        authResponse.refreshToken,
//                        authResponse.expiresIn
//                    )
//                }
//                Result.Success(response.body()!!)
//            } else {
//                Result.Error("Token refresh failed: ${response.message()}")
//            }
//        } catch (e: Exception) {
//            Result.Error("Network error: ${e.message}")
//        }
//    }
//
//    /**
//     * Logout - clear stored tokens
//     */
//    fun logout() {
//        TokenManager.clearTokens()
//    }
//
//    /**
//     * Check if user is logged in
//     */
//    fun isLoggedIn(): Boolean {
//        val token = TokenManager.getAccessToken()
//        return !token.isNullOrEmpty() && !TokenManager.isTokenExpired()
//    }
//}