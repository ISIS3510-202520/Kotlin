package com.example.here4u.data.local
import com.example.here4u.data.local.entity.EmotionEntity
import com.example.here4u.ui.theme.C_PEACH
import com.example.here4u.ui.theme.C_TEAL
import com.example.here4u.ui.theme.C_SKY
import com.example.here4u.ui.theme.C_SLATE


object SeedEmotions {
    val list = listOf(
        // Group 1: Positive 😀 (C_PEACH)
        EmotionEntity(name = "Serenity", color = C_PEACH, description = "Inner calm and peace"),
        EmotionEntity(name = "Joy", color = C_PEACH, description = "Moderate happiness"),
        EmotionEntity(name = "Ecstasy", color = C_PEACH, description = "Intense happiness"),
        EmotionEntity(name = "Acceptance", color = C_PEACH, description = "Openness, tolerance"),
        EmotionEntity(name = "Trust", color = C_PEACH, description = "Feeling of security"),
        EmotionEntity(name = "Admiration", color = C_PEACH, description = "Positive recognition"),
        EmotionEntity(name = "Love", color = C_PEACH, description = "Joy + Trust"),
        EmotionEntity(name = "Optimism", color = C_PEACH, description = "Joy + Anticipation"),
        EmotionEntity(name = "Hope", color = C_PEACH, description = "Trust + Anticipation"),
        EmotionEntity(name = "Pride", color = C_PEACH, description = "Joy + Anger"),
        EmotionEntity(name = "Gratitude", color = C_PEACH, description = "Joy + Surprise"),
        EmotionEntity(name = "Affection", color = C_PEACH, description = "Warmth and care"),

        // Group 2: Negative Introspective 😢 (C_SLATE)
        EmotionEntity(name = "Pensiveness", color = C_SLATE, description = "Mild sadness"),
        EmotionEntity(name = "Sadness", color = C_SLATE, description = "Loss or discouragement"),
        EmotionEntity(name = "Grief", color = C_SLATE, description = "Deep sadness"),
        EmotionEntity(name = "Apprehension", color = C_SLATE, description = "Mild fear"),
        EmotionEntity(name = "Fear", color = C_SLATE, description = "Perceived threat"),
        EmotionEntity(name = "Terror", color = C_SLATE, description = "Extreme fear"),
        EmotionEntity(name = "Despair", color = C_SLATE, description = "Sadness + Anticipation"),
        EmotionEntity(name = "Insecurity", color = C_SLATE, description = "Sadness + Trust"),
        EmotionEntity(name = "Guilt", color = C_SLATE, description = "Joy + Sadness"),
        EmotionEntity(name = "Submission", color = C_SLATE, description = "Fear + Trust"),
        EmotionEntity(name = "Self-rejection", color = C_SLATE, description = "Aversion toward self"),
        EmotionEntity(name = "Helplessness", color = C_SLATE, description = "Sense of powerlessness"),

        // Group 3: Negative Reactive 😡 (C_TEAL)
        EmotionEntity(name = "Annoyance", color = C_TEAL, description = "Mild anger"),
        EmotionEntity(name = "Anger", color = C_TEAL, description = "Moderate rage"),
        EmotionEntity(name = "Rage", color = C_TEAL, description = "Intense anger"),
        EmotionEntity(name = "Boredom", color = C_TEAL, description = "Lack of interest"),
        EmotionEntity(name = "Disgust", color = C_TEAL, description = "Moderate revulsion"),
        EmotionEntity(name = "Loathing", color = C_TEAL, description = "Intense disgust"),
        EmotionEntity(name = "Contempt", color = C_TEAL, description = "Anger + Disgust"),
        EmotionEntity(name = "Aggressiveness", color = C_TEAL, description = "Anger + Anticipation"),
        EmotionEntity(name = "Envy", color = C_TEAL, description = "Anger + Sadness"),
        EmotionEntity(name = "Remorse", color = C_TEAL, description = "Disgust + Sadness"),
        EmotionEntity(name = "Cynicism", color = C_TEAL, description = "Distrust with irony"),
        EmotionEntity(name = "Revenge", color = C_TEAL, description = "Desire for retaliation"),

        // Group 4: Dynamic / Cognitive 😲 (C_SKY)
        EmotionEntity(name = "Distraction", color = C_SKY, description = "Mild surprise"),
        EmotionEntity(name = "Surprise", color = C_SKY, description = "Unexpected reaction"),
        EmotionEntity(name = "Amazement", color = C_SKY, description = "Intense surprise"),
        EmotionEntity(name = "Interest", color = C_SKY, description = "Moderate curiosity"),
        EmotionEntity(name = "Anticipation", color = C_SKY, description = "Expectation of something"),
        EmotionEntity(name = "Vigilance", color = C_SKY, description = "Extreme anticipation"),
        EmotionEntity(name = "Curiosity", color = C_SKY, description = "Surprise + Anticipation"),
        EmotionEntity(name = "Anxiety", color = C_SKY, description = "Fear + Anticipation"),
        EmotionEntity(name = "Startle", color = C_SKY, description = "Fear + Surprise"),
        EmotionEntity(name = "Confusion", color = C_SKY, description = "Surprise + Sadness"),
        EmotionEntity(name = "Fascination", color = C_SKY, description = "Surprise + Joy"),
        EmotionEntity(name = "Expectation", color = C_SKY, description = "Directed anticipation")
    )
}