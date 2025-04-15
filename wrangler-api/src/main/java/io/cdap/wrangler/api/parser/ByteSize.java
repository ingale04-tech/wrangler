/*
 * Copyright © 2017-2025 Cask Data, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package io.cdap.wrangler.api.parser;

import io.cdap.wrangler.api.Token;

/**
 * Represents a byte size value (e.g., "10KB", "1.5MB") parsed into bytes.
 */
public class ByteSize implements Token {
    private final String value;
    private final long bytes;

    public ByteSize(String value) {
        this.value = value;
        this.bytes = parseBytes(value);
    }

    @Override
    public String value() {
        return value;
    }

    public long getBytes() {
        return bytes;
    }

    @Override
    public String toJson() {
        return String.format("{\"type\":\"BYTE_SIZE\",\"value\":\"%s\",\"bytes\":%d}", value, bytes);
    }

    private long parseBytes(String input) {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException("Byte size input cannot be null or empty");
        }
        String cleaned = input.trim().toUpperCase();
        double number;
        try {
            number = Double.parseDouble(cleaned.replaceAll("[^0-9.]", ""));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number format in byte size: " + input, e);
        }
        String unit = cleaned.replaceAll("[0-9.]", "");
        switch (unit) {
            case "B":
                return (long) number;
            case "KB":
                return (long) (number * 1024);
            case "MB":
                return (long) (number * 1024 * 1024);
            case "GB":
                return (long) (number * 1024 * 1024 * 1024);
            case "TB":
                return (long) (number * 1024 * 1024 * 1024 * 1024);
            case "KIB":
                return (long) (number * 1000);
            case "MIB":
                return (long) (number * 1000 * 1000);
            case "GIB":
                return (long) (number * 1000 * 1000 * 1000);
            case "TIB":
                return (long) (number * 1000 * 1000 * 1000 * 1000);
            default:
                throw new IllegalArgumentException("Unknown byte unit: " + unit);
        }
    }
}