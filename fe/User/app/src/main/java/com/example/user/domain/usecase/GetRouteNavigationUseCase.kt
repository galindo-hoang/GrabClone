package com.example.user.domain.usecase

import com.example.user.data.model.route.Route
import com.example.user.domain.repository.RouteNavigationRepository
import com.example.user.utils.Response
import com.example.user.utils.TypeCar
import javax.inject.Inject


class GetRouteNavigationUseCase @Inject constructor(
    private val routeNavigationRepository: RouteNavigationRepository
) {
    suspend fun invoke(typeCar: TypeCar = TypeCar.CAR, origin: String, destination: String): Response<List<Route>> {
        val method = if(typeCar == TypeCar.CAR) "driving"
        else "cycling"
         return try {
            val response = routeNavigationRepository.getRouteNavigation(method,origin, destination)
            if(response.code() == 200){
                if(response.body()?.code == "Oke"){
                    Response.success(response.body()!!.routes)
                }else {
                    Response.error(null,-1, response.body()?.message.toString())
                }
            }else Response.error(null,response.code(),response.message())
        }catch (e:Exception){
            Response.error(null,-1, e.message.toString())
        }
    }
}