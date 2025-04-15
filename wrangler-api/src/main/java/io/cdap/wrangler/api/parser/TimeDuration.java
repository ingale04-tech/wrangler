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
 * Represents a time duration value (e.g., "5ms", "2.1s") parsed into nanoseconds.
 */
public class TimeDuration implements Token {
    private final String value;
    private final long nanoseconds;

    public TimeDuration(String value) {
        this.value = value;
        this.nanoseconds = parseNanoseconds(value);
    }

    @Override
    public String value() {
        return value;
    }

    public long getNanoseconds() {
        return nanoseconds;
    }

    @Override
    public String toJson() {
        return String.format("{\"type\":\"TIME_DURATION\",\"value\":\"%s\",\"nanoseconds\":%d}", value, nanoseconds);
    }

    private long parseNanoseconds(String input) {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException("Time duration input cannot be null or empty");
        }
        String cleaned = input.trim().toLowerCase();
        double number;
        try {
            number = Double.parseDouble(cleaned.replaceAll("[^0-9.]", ""));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number format in time duration: " + input, e);
        }
        String unit = cleaned.replaceAll("[0-9.]", "");
        switch (unit) {
            case "ns":
                return (long) number;
            case "us":
                return (long) (number * 1_000);
            case "ms":
                return (long) (number * 1_000_000);
            case "s":
                return (long) (number * 1_000_000_000);
            case "m":
                return (long) (number * 60 * 1_000_000_000);
            case "h":
                return (long) (number * 3600 * 1_000_000_000);
            case "d":
                return (long) (number * 24 * 3600 * 1_000_000_000);
            default:
                throw new IllegalArgumentException("Unknown time unit: " + unit);
        }
    }
}