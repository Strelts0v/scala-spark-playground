package com.example

final case class Entity(id: String, nested: NestedEntity)
final case class NestedEntity(value: String)
