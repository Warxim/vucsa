/*
 * Vulnerable Client-Server Application (VuCSA)
 *
 * Copyright (C) 2021 Michal Válka
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <https://www.gnu.org/licenses/>.
 */
package com.warxim.vucsa.common.message.xml;

import com.warxim.vucsa.common.message.Message;
import com.warxim.vucsa.common.message.SerializedMessage;
import com.warxim.vucsa.common.message.MessageDeserializer;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Deserializer for {@link StorageMessage}.
 */
public class StorageMessageDeserializer implements MessageDeserializer {
    @Override
    public Optional<Message> deserializeMessage(SerializedMessage serializedMessage) {
        var xml = new String(serializedMessage.getPayload(), 0, serializedMessage.getLength());
        var items = parseXmlStorage(xml);
        return Optional.of(StorageMessage.builder()
                .target(serializedMessage.getTarget())
                .items(items)
                .build());
    }

    /**
     * Parses XML to list of storage items.
     */
    private List<StorageItem> parseXmlStorage(String xml) {
        try {
            // Create XML parsers with insecure configuration
            var documentBuilderFactory = createDocumentBuilderFactory();
            var documentBuilder = documentBuilderFactory.newDocumentBuilder();
            var document = documentBuilder.parse(new InputSource(new StringReader(xml)));

            var storage = new LinkedList<StorageItem>();
            var list = document.getElementsByTagName("item");
            for (var i = 0; i < list.getLength(); ++i) {
                var node = list.item(i);
                var maybeItem = parseStorageItem(node);
                if (maybeItem.isPresent()) {
                    storage.add(maybeItem.get());
                }
            }
            return storage;
        } catch (ParserConfigurationException | SAXException | IOException e) {
           Logger.getGlobal().log(Level.SEVERE, "Could not parse XML!", e);
        }
        return Collections.emptyList();
    }

    /**
     * Parses XML node into storage item.
     */
    private Optional<StorageItem> parseStorageItem(Node node) {
        if (node.getNodeType() != Node.ELEMENT_NODE) {
            return Optional.empty();
        }
        var element = (Element) node;

        var itemKeyElement = element.getElementsByTagName("key");
        if (itemKeyElement.getLength() != 1) {
            return Optional.empty();
        }
        var itemKey = itemKeyElement.item(0).getTextContent();

        var itemValueElement = element.getElementsByTagName("value");
        if (itemKeyElement.getLength() != 1) {
            return Optional.empty();
        }
        var itemValue = itemValueElement.item(0).getTextContent();

        return Optional.of(StorageItem.builder()
                .key(itemKey)
                .value(itemValue)
                .build());
    }

    /**
     * Creates vulnerable document builder factory.
     * <p>Allows multiple vulnerabilities to be exploited!</p>
     */
    private DocumentBuilderFactory createDocumentBuilderFactory() throws ParserConfigurationException {
        var dbf = DocumentBuilderFactory.newInstance();
        // Extreme insecure feature configuration
        dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, false);
        dbf.setExpandEntityReferences(true);
        dbf.setFeature("http://apache.org/xml/features/disallow-doctype-decl",false);
        dbf.setFeature("http://xml.org/sax/features/external-general-entities",true);
        dbf.setFeature("http://xml.org/sax/features/external-parameter-entities",true);
        dbf.setNamespaceAware(true);
        dbf.setXIncludeAware(true);
        return dbf;
    }

}
