package ru.sber.serialization

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class JsonCustomClassDeserializer {

    @Test
    fun `Нобходимо десериализовать данные в класс`() {
        // given
        val data = """{"client": "Иванов Иван Иванович"}"""
        val simpleModuleClient = SimpleModule()
            .addDeserializer(Client7::class.java, ClientDeserializer())
        val objectMapper = ObjectMapper()
            .registerModules(simpleModuleClient)

        // when
        val client = objectMapper.readValue<Client7>(data)

        // then
        assertEquals("Иван", client.firstName)
        assertEquals("Иванов", client.lastName)
        assertEquals("Иванович", client.middleName)
    }

    class  ClientDeserializer: StdDeserializer<Client7>(Client7::class.java){
        override fun deserialize(p0: JsonParser?, p1: DeserializationContext?): Client7 {
            val node: JsonNode? = p0?.codec?.readTree(p0)
            val args = node?.get("client").toString().trim('"').split(" ")

            return Client7(args[1], args[0], args[2])
        }

    }

}
