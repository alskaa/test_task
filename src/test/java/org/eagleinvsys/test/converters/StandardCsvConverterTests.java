package org.eagleinvsys.test.converters;

import org.eagleinvsys.test.converters.impl.CsvConverter;
import org.eagleinvsys.test.converters.impl.StandardCsvConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StandardCsvConverterTests {

    private final StandardCsvConverter converter = new StandardCsvConverter(new CsvConverter());
    private OutputStream outputStream;

    @BeforeEach
    void setUp() {
        outputStream = new ByteArrayOutputStream();
    }

    @Test
    void shouldConvertCollection_ifHasTwoElements() {

        Map<String, String> firstElementMap = new TreeMap<>();
        firstElementMap.put("key_1", "val_1_1");
        firstElementMap.put("key_2", "val_1_2");
        firstElementMap.put("key_3", "val_1_3");
        Map<String, String> secondElementMap = new TreeMap<>();
        secondElementMap.put("key_1", "val_2_1");
        secondElementMap.put("key_2", "val_2_2");
        secondElementMap.put("key_3", "val_2_3");

        List<Map<String, String>> collectionToConvert = List.of(firstElementMap, secondElementMap);

        converter.convert(collectionToConvert, outputStream);
        assertEquals(
                "key_1,key_2,key_3\r\nval_1_1,val_1_2,val_1_3\r\nval_2_1,val_2_2,val_2_3\r\n",
                outputStream.toString()
        );
    }

    @Test
    void shouldConvertCollection_ifNull() {

        converter.convert(null, outputStream);
        assertEquals(
                "",
                outputStream.toString()
        );
    }

    @Test
    void shouldConvertCollection_ifEmpty() {

        converter.convert(Collections.emptyList(), outputStream);
        assertEquals(
                "",
                outputStream.toString()
        );
    }

    @Test
    void shouldConvertCollection_ifRecordWithNullValue() {

        Map<String, String> firstElementMap = new TreeMap<>();
        firstElementMap.put("key_1", "val_1_1");
        firstElementMap.put("key_2", null);
        firstElementMap.put("key_3", "val_1_3");

        List<Map<String, String>> collectionToConvert = List.of(firstElementMap);

        converter.convert(collectionToConvert, outputStream);
        assertEquals(
                "key_1,key_2,key_3\r\nval_1_1,,val_1_3\r\n",
                outputStream.toString()
        );
    }

    @Test
    void shouldConvertCollection_ifRecordWithEmptyStringValue() {

        Map<String, String> firstElementMap = new TreeMap<>();
        firstElementMap.put("key_1", "val_1_1");
        firstElementMap.put("key_2", "");
        firstElementMap.put("key_3", "val_1_3");

        List<Map<String, String>> collectionToConvert = List.of(firstElementMap);

        converter.convert(collectionToConvert, outputStream);
        assertEquals(
                "key_1,key_2,key_3\r\nval_1_1,,val_1_3\r\n",
                outputStream.toString()
        );
    }

    @Test
    void shouldConvertCollection_ifRecordWithSpecialCharactersValue() {

        Map<String, String> firstElementMap = new TreeMap<>();
        firstElementMap.put("key_1", "val_1_1");
        firstElementMap.put("key_2", "',\n;val_1_2\"");
        firstElementMap.put("key_3", "val_1_3");

        List<Map<String, String>> collectionToConvert = List.of(firstElementMap);

        converter.convert(collectionToConvert, outputStream);
        assertEquals(
                "key_1,key_2,key_3\r\nval_1_1,\"',\n;val_1_2\"\"\",val_1_3\r\n",
                outputStream.toString()
        );
    }

}