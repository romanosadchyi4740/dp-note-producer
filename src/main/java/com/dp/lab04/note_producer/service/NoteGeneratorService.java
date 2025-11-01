package com.dp.lab04.note_producer.service;

import com.dp.lab04.note_producer.model.Note;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@EnableScheduling
public class NoteGeneratorService {

    private final SimpMessagingTemplate messagingTemplate;
    private final Random random = new Random();

    private static final String[] NOTES = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};
    private static final int MIN_OCTAVE = 0;
    private static final int MAX_OCTAVE = 8;

    private int noteCount = 0;

    public NoteGeneratorService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Scheduled(fixedRate = 2000)
    public void generateNote() {
        String noteName = NOTES[random.nextInt(NOTES.length)];
        int octave = MIN_OCTAVE + random.nextInt(MAX_OCTAVE - MIN_OCTAVE + 1);
        int velocity = 30 + random.nextInt(98);

        Note note = new Note(noteName, octave, System.currentTimeMillis(), velocity);

        messagingTemplate.convertAndSend("/topic/notes", note);

        noteCount++;
        System.out.printf("[%d] Sent: %s%n", noteCount, note);
    }

    @Scheduled(fixedRate = 500)
    public void generateRapidNotes() {
        if (random.nextDouble() < 0.3) {
            String noteName = NOTES[random.nextInt(NOTES.length)];
            int octave = 4 + random.nextInt(3);
            int velocity = 60 + random.nextInt(68);

            Note note = new Note(noteName, octave, System.currentTimeMillis(), velocity);
            messagingTemplate.convertAndSend("/topic/notes", note);

            noteCount++;
            System.out.printf("[%d] Quick note: %s%n", noteCount, note);
        }
    }
}
