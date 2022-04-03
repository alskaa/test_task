package org.eagleinvsys.test.converters;


import org.eagleinvsys.test.converters.impl.CsvConverter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

class CsvConverterTests {

    private final CsvConverter converter = new CsvConverter();
    private OutputStream outputStream;

    private final ConvertibleCollection convertibleCollectionMock = mock(ConvertibleCollection.class);
    private final ConvertibleMessage convertibleMessageFirstMock = mock(ConvertibleMessage.class);
    private final ConvertibleMessage convertibleMessageSecondMock = mock(ConvertibleMessage.class);

    @BeforeEach
    void setUp() {
        outputStream = new ByteArrayOutputStream();
    }

    @AfterEach
    void resetMocks() {
        reset(convertibleCollectionMock);
        reset(convertibleMessageFirstMock);
        reset(convertibleMessageSecondMock);
    }

    @Test
    void shouldConvertCollection_ifHasTwoElements() {

        when(convertibleCollectionMock.getHeaders()).thenReturn(List.of("key_1", "key_2", "key_3"));
        when(convertibleMessageFirstMock.getElement(anyString()))
                .thenReturn("val_1_1")
                .thenReturn("val_1_2")
                .thenReturn("val_1_3");
        when(convertibleMessageSecondMock.getElement(anyString()))
                .thenReturn("val_2_1")
                .thenReturn("val_2_2")
                .thenReturn("val_2_3");

        when(convertibleCollectionMock.getRecords()).thenReturn(List.of(convertibleMessageFirstMock, convertibleMessageSecondMock));

        converter.convert(convertibleCollectionMock, outputStream);
        assertEquals(
                "key_1,key_2,key_3\r\nval_1_1,val_1_2,val_1_3\r\nval_2_1,val_2_2,val_2_3\r\n",
                outputStream.toString()
        );
    }

    @Test
    void shouldConvertCollection_ifNull() {

        when(convertibleCollectionMock.getHeaders()).thenReturn(null);
        when(convertibleCollectionMock.getRecords()).thenReturn(null);

        converter.convert(convertibleCollectionMock, outputStream);
        assertEquals(
                "",
                outputStream.toString()
        );
    }

    @Test
    void shouldConvertCollection_ifEmpty() {

        when(convertibleCollectionMock.getHeaders()).thenReturn(Collections.emptyList());
        when(convertibleCollectionMock.getRecords()).thenReturn(Collections.emptyList());

        converter.convert(convertibleCollectionMock, outputStream);
        assertEquals(
                "",
                outputStream.toString()
        );
    }

    @Test
    void shouldConvertCollection_ifRecordWithNullValue() {

        when(convertibleCollectionMock.getHeaders()).thenReturn(List.of("key_1", "key_2", "key_3"));
        when(convertibleMessageFirstMock.getElement(anyString()))
                .thenReturn("val_1_1")
                .thenReturn(null)
                .thenReturn("val_1_3");
        when(convertibleCollectionMock.getRecords()).thenReturn(List.of(convertibleMessageFirstMock));

        converter.convert(convertibleCollectionMock, outputStream);
        assertEquals(
                "key_1,key_2,key_3\r\nval_1_1,,val_1_3\r\n",
                outputStream.toString()
        );
    }

    @Test
    void shouldConvertCollection_ifRecordWithEmptyStringValue() {

        when(convertibleCollectionMock.getHeaders()).thenReturn(List.of("key_1", "key_2", "key_3"));
        when(convertibleMessageFirstMock.getElement(anyString()))
                .thenReturn("val_1_1")
                .thenReturn("")
                .thenReturn("val_1_3");
        when(convertibleCollectionMock.getRecords()).thenReturn(List.of(convertibleMessageFirstMock));

        converter.convert(convertibleCollectionMock, outputStream);
        assertEquals(
                "key_1,key_2,key_3\r\nval_1_1,,val_1_3\r\n",
                outputStream.toString()
        );
    }

    @Test
    void shouldConvertCollection_ifRecordWithSpecialCharactersValue() {

        when(convertibleCollectionMock.getHeaders()).thenReturn(List.of("key_1", "key_2", "key_3"));
        when(convertibleMessageFirstMock.getElement(anyString()))
                .thenReturn("val_1_1")
                .thenReturn("',\n;val_1_2\"")
                .thenReturn("val_1_3");
        when(convertibleCollectionMock.getRecords()).thenReturn(List.of(convertibleMessageFirstMock));

        converter.convert(convertibleCollectionMock, outputStream);
        assertEquals(
                "key_1,key_2,key_3\r\nval_1_1,\"',\n;val_1_2\"\"\",val_1_3\r\n",
                outputStream.toString()
        );
    }
}