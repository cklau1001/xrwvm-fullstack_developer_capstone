package io.cklau1001.capstone.dto;

public record SentimentResponse(Double confidence, String sentiment) {

    @Override
    public String toString() {
        return "[SentimentResponse]: confidence=" + confidence + ", sentiment=" + sentiment;
    }

}
