package com.example.driver.utils

interface ObjectConverter<Network,Domain> {
    fun convertFromNetwork(network: Network): Domain
    fun convertToNetwork(domain: Domain): Network
}