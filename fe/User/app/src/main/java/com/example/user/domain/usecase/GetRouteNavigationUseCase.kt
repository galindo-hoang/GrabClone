package com.example.user.domain.usecase

import android.util.Log
import com.example.user.data.model.googlemap.Route
import com.example.user.domain.repository.RouteNavigationRepository
import com.example.user.utils.Response
import javax.inject.Inject


class GetRouteNavigationUseCase @Inject constructor(
    private val routeNavigationRepository: RouteNavigationRepository
) {
    suspend fun invoke(origin: String,destination: String, mode: String): Response<List<Route>?> {
         return try {
            val response = routeNavigationRepository.getRouteNavigation(origin, destination, mode)
            Log.e("------",response.raw().toString())
            if(response.code() == 200){
                if(response.body() != null &&
                    response.body()!!.status == "200" &&
                    response.body()!!.routes.isNotEmpty()
                ) { Response.success(response.body()!!.routes) }
                else Response.error(null,"cant get body")
            }else Response.error(null,response.message())
        }catch (e:Exception){
            Response.error(null, e.message.toString())
        }
    }
}