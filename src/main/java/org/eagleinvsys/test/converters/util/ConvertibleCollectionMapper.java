package org.eagleinvsys.test.converters.util;

import org.eagleinvsys.test.converters.ConvertibleCollection;
import org.eagleinvsys.test.converters.ConvertibleMessage;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


public class ConvertibleCollectionMapper {

    private ConvertibleCollectionMapper() {
    }

    public static Iterable<Iterable<String>> recordsToIterable(ConvertibleCollection convertibleCollection) {
        // All records of the collection are considered to have the same headers, so if headers are empty,
        // records should be empty according to ConvertibleCollection interface contract
        if (convertibleCollection.getHeaders() == null || convertibleCollection.getHeaders().isEmpty()) {
            return Collections.emptyList();
        }

        return StreamSupport.stream(convertibleCollection.getRecords().spliterator(), false)
                .map(convertibleMessage -> convertibleMessageToIterable(convertibleMessage, convertibleCollection.getHeaders()))
                .collect(Collectors.toList());
    }

    private static Iterable<String> convertibleMessageToIterable(ConvertibleMessage convertibleMessage, Collection<String> headers) {
        // All records of the collection are considered to have the same headers, so if headers are empty,
        // records should be empty according to ConvertibleCollection interface contract
        if (headers == null || headers.isEmpty()) {
            return Collections.emptyList();
        }

        if (convertibleMessage == null) {
            return null;
        }

        return headers.stream()
                .map(convertibleMessage::getElement)
                .collect(Collectors.toList());
    }

    public static ConvertibleCollection listMapToConvertibleCollection(List<Map<String, String>> collectionToConvert) {

        if (collectionToConvert == null || collectionToConvert.isEmpty()) {
            return new ConvertibleCollection() {
                @Override
                public Collection<String> getHeaders() {
                    return Collections.emptyList();
                }

                @Override
                public Iterable<ConvertibleMessage> getRecords() {
                    return Collections.emptyList();
                }
            };
        }

        return new ConvertibleCollection() {
            @Override
            public Collection<String> getHeaders() {
                return collectionToConvert.get(0).keySet();
            }

            @Override
            public Iterable<ConvertibleMessage> getRecords() {
                return collectionToConvert.stream()
                        .filter(map -> map != null && !map.isEmpty())
                        .map(map -> (ConvertibleMessage) map::get)
                        .collect(Collectors.toList());
            }
        };
    }
}
