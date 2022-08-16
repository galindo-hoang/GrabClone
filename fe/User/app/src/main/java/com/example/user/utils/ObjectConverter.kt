package com.example.user.utils

interface ObjectConverter<Network,Domain> {
    fun convertFromNetwork(network: Network): Domain
    fun convertToNetwork(domain: Domain): Network
}