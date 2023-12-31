package service.converter

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.dataformat.csv.CsvMapper
import com.fasterxml.jackson.dataformat.csv.CsvSchema
import utils.Constants
import utils.EnvVars
import utils.Helpers
import java.io.File


object CsvToJson : Convertible {
    override fun convert(filePath: String) {
//        val input = File(EnvVars.BASE_PATH + "/" + filePath + Constants.CSV_EXTENSION)
//        val output = File(EnvVars.BASE_PATH + "/" + filePath + Constants.JSON_EXTENSION)
        val input = File(filePath)
        val output = File(Helpers.removeFileExtension(filePath) + Constants.JSON_EXTENSION)

        try {
            val csv = CsvSchema.emptySchema().withHeader()
            val csvMapper = CsvMapper()
            val objectMapper = ObjectMapper()

            val mappingIterator = csvMapper.reader().forType(
                MutableMap::class.java
            ).with(csv).readValues<Map<*, *>>(input)
            val list = mappingIterator.readAll()

            objectMapper.enable(SerializationFeature.INDENT_OUTPUT)
            val json = objectMapper.writeValueAsString(list)

            output.writeText(json)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}