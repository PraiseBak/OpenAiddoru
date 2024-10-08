package com.aiddoru.dev.Utility;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;

import java.io.IOException;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.math.BigInteger;

public class BigIntegerDeserializer extends JsonDeserializer<BigInteger> {
    @Override
    public BigInteger deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException, JsonProcessingException {
        return new BigInteger(jsonParser.getText());
    }
}
