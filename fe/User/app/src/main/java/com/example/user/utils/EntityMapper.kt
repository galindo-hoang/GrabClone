package com.example.user.utils

interface EntityMapper <Entity,EntityDto> {
    fun mapFromEntity(entity: Entity): EntityDto
    fun mapToEntity(entityDto: EntityDto): Entity
}