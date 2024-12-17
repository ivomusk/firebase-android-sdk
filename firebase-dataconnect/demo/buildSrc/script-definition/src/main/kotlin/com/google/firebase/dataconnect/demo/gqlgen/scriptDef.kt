/*
 * Copyright 2024 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.firebase.dataconnect.demo.gqlgen

import kotlin.script.experimental.annotations.KotlinScript
import kotlin.script.experimental.api.ScriptAcceptedLocation
import kotlin.script.experimental.api.ScriptCompilationConfiguration
import kotlin.script.experimental.api.acceptedLocations
import kotlin.script.experimental.api.defaultImports
import kotlin.script.experimental.api.ide

@KotlinScript(
  displayName = "Data Connect GraphGQL Generator",
  fileExtension = "dataconnectgql.kts",
  compilationConfiguration = DataConnectGqlScriptCompilationConfiguration::class,
)
abstract class DataConnectGqlScript {
  var schema: DataConnectSchema = DataConnectSchema()
}

fun DataConnectGqlScript.schema(block: DataConnectSchema.() -> Unit) {
  block(schema)
}

class DataConnectSchema {
  var tables: MutableList<DataConnectTable> = mutableListOf()
}

fun DataConnectSchema.table(name: String, block: DataConnectTable.() -> Unit) {
  tables.add(DataConnectTable(name).apply(block))
}

class DataConnectTable(var name: String) {
  var fields: MutableList<DataConnectTableField<*>> = mutableListOf()
  var tableName: String? = null
  var singular: String? = null
  var plural: String? = null
  var keys: MutableList<DataConnectTableField<*>> = mutableListOf()
}

fun <T : GraphQLType<T>> DataConnectTable.field(name: String, type: T): DataConnectTableField<T> {
  val newField = DataConnectTableField(name, type)
  fields.add(newField)
  return newField
}

fun DataConnectTable.key(field: DataConnectTableField<*>) {
  keys.add(field)
}

class DataConnectTableField<T : GraphQLType<T>>(var name: String, var type: T)

sealed class GraphQLType<T : GraphQLType<T>>(var isNullable: Boolean) {
  class GraphQLList<T : Scalar<T>>(var componentType: T, isNullable: Boolean = false) :
    GraphQLType<GraphQLList<T>>(isNullable)

  sealed class Scalar<T : Scalar<T>>(isNullable: Boolean) : GraphQLType<T>(isNullable)

  sealed class UUIDScalar(isNullable: Boolean) : Scalar<UUIDScalar>(isNullable) {
    data object Nullable : UUIDScalar(true)

    data object NonNull : UUIDScalar(false)
  }

  sealed class StringScalar(isNullable: Boolean) : Scalar<StringScalar>(isNullable) {
    data object Nullable : StringScalar(true)

    data object NonNull : StringScalar(false)
  }

  sealed class IntScalar(isNullable: Boolean) : Scalar<IntScalar>(isNullable) {
    data object Nullable : IntScalar(true)

    data object NonNull : IntScalar(false)
  }

  sealed class Int64Scalar(isNullable: Boolean) : Scalar<Int64Scalar>(isNullable) {
    data object Nullable : Int64Scalar(true)

    data object NonNull : Int64Scalar(false)
  }

  sealed class FloatScalar(isNullable: Boolean) : Scalar<FloatScalar>(isNullable) {
    data object Nullable : FloatScalar(true)

    data object NonNull : FloatScalar(false)
  }

  sealed class BooleanScalar(isNullable: Boolean) : Scalar<BooleanScalar>(isNullable) {
    data object Nullable : BooleanScalar(true)

    data object NonNull : BooleanScalar(false)
  }

  sealed class DateScalar(isNullable: Boolean) : Scalar<DateScalar>(isNullable) {
    data object Nullable : DateScalar(true)

    data object NonNull : DateScalar(false)
  }

  sealed class TimestampScalar(isNullable: Boolean) : Scalar<TimestampScalar>(isNullable) {
    data object Nullable : TimestampScalar(true)

    data object NonNull : TimestampScalar(false)
  }

  sealed class AnyScalar(isNullable: Boolean) : Scalar<AnyScalar>(isNullable) {
    data object Nullable : AnyScalar(true)

    data object NonNull : AnyScalar(false)
  }
}

object DataConnectGqlScriptCompilationConfiguration :
  ScriptCompilationConfiguration({
    ide { acceptedLocations(ScriptAcceptedLocation.Everywhere) }

    defaultImports(
      "com.google.firebase.dataconnect.demo.gqlgen.*",
      "com.google.firebase.dataconnect.demo.gqlgen.GraphQLType.GraphQLList",
      "com.google.firebase.dataconnect.demo.gqlgen.GraphQLType.UUIDScalar",
      "com.google.firebase.dataconnect.demo.gqlgen.GraphQLType.StringScalar",
      "com.google.firebase.dataconnect.demo.gqlgen.GraphQLType.IntScalar",
      "com.google.firebase.dataconnect.demo.gqlgen.GraphQLType.Int64Scalar",
      "com.google.firebase.dataconnect.demo.gqlgen.GraphQLType.FloatScalar",
      "com.google.firebase.dataconnect.demo.gqlgen.GraphQLType.BooleanScalar",
      "com.google.firebase.dataconnect.demo.gqlgen.GraphQLType.DateScalar",
      "com.google.firebase.dataconnect.demo.gqlgen.GraphQLType.TimestampScalar",
      "com.google.firebase.dataconnect.demo.gqlgen.GraphQLType.AnyScalar",
    )
  })
