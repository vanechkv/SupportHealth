package com.example.supporthealth.di

import com.example.supporthealth.chat.ui.ChatViewModel
import com.example.supporthealth.nutrition.eating.ui.EatingViewModel
import com.example.supporthealth.profile.details.ui.DetailsViewModel
import com.example.supporthealth.nutrition.main.ui.NutritionViewModel
import com.example.supporthealth.nutrition.product.ui.ProductViewModel
import com.example.supporthealth.nutrition.search.ui.SearchViewModel
import com.example.supporthealth.profile.main.ui.ProfileViewModel
import com.example.supporthealth.welcome.detailsOnbording.ui.DetailsOnBordingViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        NutritionViewModel(get())
    }

    viewModel {
        DetailsViewModel(get(), get())
    }

    viewModel {
        ProfileViewModel(get(), get())
    }

    viewModel {
        DetailsOnBordingViewModel(get(), get(), get())
    }

    viewModel {
        SearchViewModel(get())
    }

    viewModel {
        ProductViewModel(get(), get())
    }

    viewModel {
        ChatViewModel(get())
    }

    viewModel { (mealId: Long) ->
        EatingViewModel(mealId, get())
    }
}