package com.dp.lab04.note_producer.model;

public record Note(String noteName, int octave, long timestamp, int velocity) {
}
