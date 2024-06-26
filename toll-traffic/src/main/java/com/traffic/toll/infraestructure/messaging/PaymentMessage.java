package com.traffic.toll.infraestructure.messaging;

import jakarta.json.JsonObject;
import jakarta.json.Json;
import jakarta.json.JsonReader;
import jakarta.json.JsonWriter;

import java.io.StringReader;
import java.io.StringWriter;

public record PaymentMessage (
        Double commonTariff,
        Double preferentialTariff,
        Long tagId
){
    public String toJson(){
        JsonObject json = Json.createObjectBuilder()
                .add("commonTariff", Double.toString(this.commonTariff))
                .add("preferentialTariff", Double.toString(this.preferentialTariff))
                .add("tagId", Long.toString(this.tagId))
                .build();

        StringWriter sw = new StringWriter();
        JsonWriter jsonWriter = Json.createWriter(sw);
        jsonWriter.write(json);
        jsonWriter.close();
        return sw.toString();
    }

    public static PaymentMessage readFromJson(String jsonString){
        JsonReader reader = Json.createReader(new StringReader(jsonString));
        JsonObject json = reader.readObject();

        return new PaymentMessage(
                Double.valueOf(json.getString("commonTariff")),
                Double.valueOf(json.getString("preferentialTariff")),
                Long.valueOf(json.getString("tagId"))
        );
    }
}
