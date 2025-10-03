package com.example.here4u.data.local

import com.example.here4u.data.local.entity.EmotionEntity
import com.example.here4u.ui.theme.C_PEACH
import com.example.here4u.ui.theme.C_TEAL
import com.example.here4u.ui.theme.C_SKY
import com.example.here4u.ui.theme.C_SLATE

object SeedEmotions {
    val list = listOf(
        // Group 1: Positive 😀 (C_PEACH)
        EmotionEntity(name = "Serenity", colorHex = C_PEACH, description = "Inner calm and peace"),
        EmotionEntity(name = "Joy", colorHex = C_PEACH, description = "Moderate happiness"),
        EmotionEntity(name = "Ecstasy", colorHex = C_PEACH, description = "Intense happiness"),
        EmotionEntity(name = "Acceptance", colorHex = C_PEACH, description = "Openness, tolerance"),
        EmotionEntity(name = "Trust", colorHex = C_PEACH, description = "Feeling of security"),
        EmotionEntity(name = "Admiration", colorHex = C_PEACH, description = "Positive recognition"),
        EmotionEntity(name = "Love", colorHex = C_PEACH, description = "Joy + Trust"),
        EmotionEntity(name = "Optimism", colorHex = C_PEACH, description = "Joy + Anticipation"),
        EmotionEntity(name = "Hope", colorHex = C_PEACH, description = "Trust + Anticipation"),
        EmotionEntity(name = "Pride", colorHex = C_PEACH, description = "Joy + Anger"),
        EmotionEntity(name = "Gratitude", colorHex = C_PEACH, description = "Joy + Surprise"),
        EmotionEntity(name = "Affection", colorHex = C_PEACH, description = "Warmth and care"),

        // Group 2: Negative Introspective 😢 (C_SLATE)
        EmotionEntity(name = "Pensiveness", colorHex = C_SLATE, description = "Mild sadness"),
        EmotionEntity(name = "Sadness", colorHex = C_SLATE, description = "Loss or discouragement"),
        EmotionEntity(name = "Grief", colorHex = C_SLATE, description = "Deep sadness"),
        EmotionEntity(name = "Apprehension", colorHex = C_SLATE, description = "Mild fear"),
        EmotionEntity(name = "Fear", colorHex = C_SLATE, description = "Perceived threat"),
        EmotionEntity(name = "Terror", colorHex = C_SLATE, description = "Extreme fear"),
        EmotionEntity(name = "Despair", colorHex = C_SLATE, description = "Sadness + Anticipation"),
        EmotionEntity(name = "Insecurity", colorHex = C_SLATE, description = "Sadness + Trust"),
        EmotionEntity(name = "Guilt", colorHex = C_SLATE, description = "Joy + Sadness"),
        EmotionEntity(name = "Submission", colorHex = C_SLATE, description = "Fear + Trust"),
        EmotionEntity(name = "Self-rejection", colorHex = C_SLATE, description = "Aversion toward self"),
        EmotionEntity(name = "Helplessness", colorHex = C_SLATE, description = "Sense of powerlessness"),

        // Group 3: Negative Reactive 😡 (C_TEAL)
        EmotionEntity(name = "Annoyance", colorHex = C_TEAL, description = "Mild anger"),
        EmotionEntity(name = "Anger", colorHex = C_TEAL, description = "Moderate rage"),
        EmotionEntity(name = "Rage", colorHex = C_TEAL, description = "Intense anger"),
        EmotionEntity(name = "Boredom", colorHex = C_TEAL, description = "Lack of interest"),
        EmotionEntity(name = "Disgust", colorHex = C_TEAL, description = "Moderate revulsion"),
        EmotionEntity(name = "Loathing", colorHex = C_TEAL, description = "Intense disgust"),
        EmotionEntity(name = "Contempt", colorHex = C_TEAL, description = "Anger + Disgust"),
        EmotionEntity(name = "Aggressiveness", colorHex = C_TEAL, description = "Anger + Anticipation"),
        EmotionEntity(name = "Envy", colorHex = C_TEAL, description = "Anger + Sadness"),
        EmotionEntity(name = "Remorse", colorHex = C_TEAL, description = "Disgust + Sadness"),
        EmotionEntity(name = "Cynicism", colorHex = C_TEAL, description = "Distrust with irony"),
        EmotionEntity(name = "Revenge", colorHex = C_TEAL, description = "Desire for retaliation"),

        // Group 4: Dynamic / Cognitive 😲 (C_SKY)
        EmotionEntity(name = "Distraction", colorHex = C_SKY, description = "Mild surprise"),
        EmotionEntity(name = "Surprise", colorHex = C_SKY, description = "Unexpected reaction"),
        EmotionEntity(name = "Amazement", colorHex = C_SKY, description = "Intense surprise"),
        EmotionEntity(name = "Interest", colorHex = C_SKY, description = "Moderate curiosity"),
        EmotionEntity(name = "Anticipation", colorHex = C_SKY, description = "Expectation of something"),
        EmotionEntity(name = "Vigilance", colorHex = C_SKY, description = "Extreme anticipation"),
        EmotionEntity(name = "Curiosity", colorHex = C_SKY, description = "Surprise + Anticipation"),
        EmotionEntity(name = "Anxiety", colorHex = C_SKY, description = "Fear + Anticipation"),
        EmotionEntity(name = "Startle", colorHex = C_SKY, description = "Fear + Surprise"),
        EmotionEntity(name = "Confusion", colorHex = C_SKY, description = "Surprise + Sadness"),
        EmotionEntity(name = "Fascination", colorHex = C_SKY, description = "Surprise + Joy"),
        EmotionEntity(name = "Expectation", colorHex = C_SKY, description = "Directed anticipation")
    )
}
