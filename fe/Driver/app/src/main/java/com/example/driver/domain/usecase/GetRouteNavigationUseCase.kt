package com.example.driver.domain.usecase

import com.example.driver.data.model.route.Direction
import com.example.driver.domain.repository.BookingRepository
import com.example.driver.utils.Response
import com.example.driver.utils.TypeCar
import javax.inject.Inject


class GetRouteNavigationUseCase @Inject constructor(
    private val bookingRepository: BookingRepository
) {
    suspend fun invoke(origin: String, destination: String, typeCar: TypeCar = TypeCar.CAR): Response<Direction> {
         return try {
             val response = bookingRepository.getRouteNavigation("drive",origin, destination)
             if(response.code() == 200){
                if(response.body()?.statusCode == null){
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