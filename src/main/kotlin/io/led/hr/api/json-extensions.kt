package io.led.hr.api

import org.json.JSONObject
import java.util.*

fun JSONObject.toOptString(): Optional<String> = if (isEmpty) Optional.empty() else Optional.of(toString())
