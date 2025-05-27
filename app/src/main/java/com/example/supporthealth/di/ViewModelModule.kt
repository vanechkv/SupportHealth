package com.example.supporthealth.di

import com.example.supporthealth.activity.main.ui.ActivityViewModel
import com.example.supporthealth.activity.statistic.ui.StatisticActivityViewModel
import com.example.supporthealth.chat.ui.ChatViewModel
import com.example.supporthealth.nutrition.eating.ui.EatingViewModel
import com.example.supporthealth.profile.details.ui.DetailsViewModel
import com.example.supporthealth.nutrition.main.ui.NutritionViewModel
import com.example.supporthealth.nutrition.product.ui.ProductViewModel
import com.example.supporthealth.nutrition.search.ui.SearchViewModel
import com.example.supporthealth.nutrition.statistic.ui.StatisticNutritionViewModel
import com.example.supporthealth.profile.main.ui.ProfileViewModel
import com.example.supporthealth.welcome.detailsOnbording.ui.DetailsOnBordingViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        NutritionViewModel(get(), get())
    }

    viewModel {
        DetailsViewModel(get(), get(), get())
    }

    viewModel {
        ProfileViewModel(get(), get(), get())
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
        ChatViewModel(get(), get())
    }

    viewModel { (mealId: Long) ->
        EatingViewModel(mealId, get(), get())
    }

    viewModel {
        ActivityViewModel(get())
    }

    viewModel {
        StatisticActivityViewModel(get())
    }

    viewModel {
        StatisticNutritionViewModel(get())
    }
}