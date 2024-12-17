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

package com.google.firebase.dataconnect.demo.gqlgen.host

import com.google.firebase.dataconnect.demo.gqlgen.DataConnectGqlScript
import java.io.File
import kotlin.script.experimental.api.EvaluationResult
import kotlin.script.experimental.api.ResultWithDiagnostics
import kotlin.script.experimental.api.ScriptDiagnostic
import kotlin.script.experimental.host.toScriptSource
import kotlin.script.experimental.jvm.dependenciesFromCurrentContext
import kotlin.script.experimental.jvm.jvm
import kotlin.script.experimental.jvmhost.BasicJvmScriptingHost
import kotlin.script.experimental.jvmhost.createJvmCompilationConfigurationFromTemplate

fun main(vararg args: String) {
  if (args.size != 1) {
    throw InvalidCommandLineArguments(
      "invalid command-line arguments: " +
        args.toList().joinToString(" ") +
        " (expected exactly 1 argument, the path of the script to execute)"
    )
  }

  val scriptFile = File(args[0])
  println("Executing script ${scriptFile.absolutePath}")

  val res = evalScript(scriptFile)

  res.reports.forEach {
    if (it.severity > ScriptDiagnostic.Severity.DEBUG) {
      println(" : ${it.message}" + if (it.exception == null) "" else ": ${it.exception}")
    }
  }
}

fun evalScript(scriptFile: File): ResultWithDiagnostics<EvaluationResult> {
  val compilationConfiguration =
    createJvmCompilationConfigurationFromTemplate<DataConnectGqlScript> {
      jvm { dependenciesFromCurrentContext("script-definition") }
    }

  return BasicJvmScriptingHost().eval(scriptFile.toScriptSource(), compilationConfiguration, null)
}

class InvalidCommandLineArguments(message: String) : Exception(message)
