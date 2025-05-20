package com.example.supporthealth.nutrition.eating.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.supporthealth.databinding.FragmentEatingBinding
import com.example.supporthealth.main.domain.models.MealEntity
import com.example.supporthealth.main.domain.models.NutritionFull
import com.example.supporthealth.main.domain.models.ProductEntity
import com.example.supporthealth.nutrition.eating.domain.models.ProductWithGrams
import com.example.supporthealth.nutrition.main.domain.models.Meal
import com.example.supporthealth.nutrition.search.domain.models.Product
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class EatingFragment : Fragment() {

    companion object {
        fun newInstance() = EatingFragment()
    }

    private val args: EatingFragmentArgs by navArgs()

    private val viewModel: EatingViewModel by viewModel { parametersOf(args.mealId) }

    private val adapter = ProductAdapter(arrayListOf()) {
        viewModel.onProductClick(Product(
            productId = it.productId,
            name = it.name,
            calories = it.calories,
            protein = it.proteins,
            fat = it.fats,
            carbs = it.carbs
        ))
        openProductDetails(it)
    }

    private lateinit var binding: FragmentEatingBinding

    private var meal: MealEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEatingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerProducts.adapter = adapter

        viewModel.observeMealWithProducts().observe(viewLifecycleOwner) { mealWithProducts ->
            binding.title.text = mealWithProducts.meal.mealType.displayName
            meal = mealWithProducts.meal
            setMeal(meal!!)
        }

        viewModel.observeProductsWithGrams().observe(viewLifecycleOwner) { productsWithGrams ->
            adapter.updateProduct(productsWithGrams)
        }

        binding.buttonBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.buttonAdd.setOnClickListener {
            openSearch()
        }
    }

    private fun openSearch() {
        val action = EatingFragmentDirections
            .actionEatingFragmentToNavigationSearch(
                meal = meal!!.mealType,
                date = args.date
            )
        findNavController().navigate(action)
    }

    private fun openProductDetails(product: ProductWithGrams) {
        val action = EatingFragmentDirections
            .actionEatingFragmentToNavigationProduct(
                meal = meal!!.mealType,
                date = args.date,
                grams = product.grams,
                productId = product.productId
            )
        findNavController().navigate(action)
    }

    private fun setMeal(meal: MealEntity) {
        binding.statistic.apply {
            calories.text = "${meal.calories} ккал"
            remainingCalories.text =
                "${meal.recommendedCalories - meal.calories} ккал осталось"
            protein.text = "${meal.proteins.toInt()} г"
            remainingProtein.text =
                "${meal.recommendedProteins.toInt() - meal.proteins.toInt()} г осталось"
            fat.text = "${meal.fats.toInt()} г"
            remainingFat.text = "${meal.recommendedFats.toInt() - meal.fats.toInt()} г осталось"
            carbohydrates.text = "${meal.carbs.toInt()} г"
            remainingCarbohydrates.text = "${meal.recommendedCarbs.toInt() - meal.carbs.toInt()} г осталось"
        }
    }
}