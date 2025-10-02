package com.example.here4u.data.local

import com.example.here4u.data.local.entity.EmotionEntity
import com.example.here4u.ui.theme.C_PEACH
import com.example.here4u.ui.theme.C_TEAL
import com.example.here4u.ui.theme.C_SKY
import com.example.here4u.ui.theme.C_SLATE

object SeedEmotions {
    val list = listOf(
        // Group 1: Positive 😀 (C_PEACH)
        EmotionEntity(name = "Serenity", color = C_PEACH, description = "Inner calm and peace", score = 0.6f),
        EmotionEntity(name = "Joy", color = C_PEACH, description = "Moderate happiness", score = 0.8f),
        EmotionEntity(name = "Ecstasy", color = C_PEACH, description = "Intense happiness", score = 1.0f),
        EmotionEntity(name = "Acceptance", color = C_PEACH, description = "Openness, tolerance", score = 0.5f),
        EmotionEntity(name = "Trust", color = C_PEACH, description = "Feeling of security", score = 0.7f),
        EmotionEntity(name = "Admiration", color = C_PEACH, description = "Positive recognition", score = 0.6f),
        EmotionEntity(name = "Love", color = C_PEACH, description = "Joy + Trust", score = 0.9f),
        EmotionEntity(name = "Optimism", color = C_PEACH, description = "Joy + Anticipation", score = 0.8f),
        EmotionEntity(name = "Hope", color = C_PEACH, description = "Trust + Anticipation", score = 0.7f),
        EmotionEntity(name = "Pride", color = C_PEACH, description = "Joy + Anger", score = 0.4f),
        EmotionEntity(name = "Gratitude", color = C_PEACH, description = "Joy + Surprise", score = 0.9f),
        EmotionEntity(name = "Affection", color = C_PEACH, description = "Warmth and care", score = 0.8f),

        // Group 2: Negative Introspective 😢 (C_SLATE)
        EmotionEntity(name = "Pensiveness", color = C_SLATE, description = "Mild sadness", score = -0.3f),
        EmotionEntity(name = "Sadness", color = C_SLATE, description = "Loss or discouragement", score = -0.6f),
        EmotionEntity(name = "Grief", color = C_SLATE, description = "Deep sadness", score = -0.9f),
        EmotionEntity(name = "Apprehension", color = C_SLATE, description = "Mild fear", score = -0.4f),
        EmotionEntity(name = "Fear", color = C_SLATE, description = "Perceived threat", score = -0.7f),
        EmotionEntity(name = "Terror", color = C_SLATE, description = "Extreme fear", score = -1.0f),
        EmotionEntity(name = "Despair", color = C_SLATE, description = "Sadness + Anticipation", score = -0.8f),
        EmotionEntity(name = "Insecurity", color = C_SLATE, description = "Sadness + Trust", score = -0.5f),
        EmotionEntity(name = "Guilt", color = C_SLATE, description = "Joy + Sadness", score = -0.4f),
        EmotionEntity(name = "Submission", color = C_SLATE, description = "Fear + Trust", score = -0.3f),
        EmotionEntity(name = "Self-rejection", color = C_SLATE, description = "Aversion toward self", score = -0.7f),
        EmotionEntity(name = "Helplessness", color = C_SLATE, description = "Sense of powerlessness", score = -0.8f),

        // Group 3: Negative Reactive 😡 (C_TEAL)
        EmotionEntity(name = "Annoyance", color = C_TEAL, description = "Mild anger", score = -0.3f),
        EmotionEntity(name = "Anger", color = C_TEAL, description = "Moderate rage", score = -0.6f),
        EmotionEntity(name = "Rage", color = C_TEAL, description = "Intense anger", score = -0.9f),
        EmotionEntity(name = "Boredom", color = C_TEAL, description = "Lack of interest", score = -0.2f),
        EmotionEntity(name = "Disgust", color = C_TEAL, description = "Moderate revulsion", score = -0.5f),
        EmotionEntity(name = "Loathing", color = C_TEAL, description = "Intense disgust", score = -0.9f),
        EmotionEntity(name = "Contempt", color = C_TEAL, description = "Anger + Disgust", score = -0.6f),
        EmotionEntity(name = "Aggressiveness", color = C_TEAL, description = "Anger + Anticipation", score = -0.4f),
        EmotionEntity(name = "Envy", color = C_TEAL, description = "Anger + Sadness", score = -0.5f),
        EmotionEntity(name = "Remorse", color = C_TEAL, description = "Disgust + Sadness", score = -0.5f),
        EmotionEntity(name = "Cynicism", color = C_TEAL, description = "Distrust with irony", score = -0.4f),
        EmotionEntity(name = "Revenge", color = C_TEAL, description = "Desire for retaliation", score = -0.7f),

        // Group 4: Dynamic / Cognitive 😲 (C_SKY)
        EmotionEntity(name = "Distraction", color = C_SKY, description = "Mild surprise", score = 0.1f),
        EmotionEntity(name = "Surprise", color = C_SKY, description = "Unexpected reaction", score = 0.3f),
        EmotionEntity(name = "Amazement", color = C_SKY, description = "Intense surprise", score = 0.7f),
        EmotionEntity(name = "Interest", color = C_SKY, description = "Moderate curiosity", score = 0.4f),
        EmotionEntity(name = "Anticipation", color = C_SKY, description = "Expectation of something", score = 0.2f),
        EmotionEntity(name = "Vigilance", color = C_SKY, description = "Extreme anticipation", score = 0.5f),
        EmotionEntity(name = "Curiosity", color = C_SKY, description = "Surprise + Anticipation", score = 0.4f),
        EmotionEntity(name = "Anxiety", color = C_SKY, description = "Fear + Anticipation", score = -0.5f),
        EmotionEntity(name = "Startle", color = C_SKY, description = "Fear + Surprise", score = -0.2f),
        EmotionEntity(name = "Confusion", color = C_SKY, description = "Surprise + Sadness", score = -0.3f),
        EmotionEntity(name = "Fascination", color = C_SKY, description = "Surprise + Joy", score = 0.6f),
        EmotionEntity(name = "Expectation", color = C_SKY, description = "Directed anticipation", score = 0.3f)
    )
}
