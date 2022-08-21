package com.example.user.domain.usecase

import android.util.Log
import com.example.user.data.model.route.Direction
import com.example.user.domain.repository.RouteNavigationRepository
import com.example.user.utils.Response
import com.example.user.utils.TypeCar
import javax.inject.Inject


class GetRouteNavigationUseCase @Inject constructor(
    private val routeNavigationRepository: RouteNavigationRepository
) {
    suspend fun invoke(typeCar: TypeCar = TypeCar.CAR, origin: String, destination: String): Response<Direction> {
         return try {
             val response = routeNavigationRepository.getRouteNavigation("drive",origin, destination)
             Log.e("---------", response.body()!!.features[0].geometry.coordinates[0].size.toString())
             Log.e("---------", response.body()!!.statusCode.toString())
             if(response.code() == 200){
                 Log.e("------","hello")
                if(response.body()?.statusCode == null){
                    Log.e("------","hi")
                    Response.success(response.body()!!)
                }else {
                    Response.error(
                        null,
                        response.body()?.statusCode ?: 400,
                        response.body()?.message.toString()
                    )
                }
             }else Response.error(null,response.code(),response.message())
         }catch (e:Exception){
             e.printStackTrace()
             Response.error(null,-1, e.message.toString())
         }
    }
}