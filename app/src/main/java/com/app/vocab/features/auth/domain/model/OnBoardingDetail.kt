package com.app.vocab.features.auth.domain.model

import androidx.annotation.DrawableRes
import androidx.compose.ui.text.AnnotatedString

data class OnBoardingDetail(
    @DrawableRes val image: Int,
    val titleText: AnnotatedString,
    val detailText: String,
    val buttonText: String,
    val descriptionOfImage: String
)
