package com.example.supporthealth.nutrition.eating.ui

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.supporthealth.R
import com.example.supporthealth.databinding.FragmentEatingBinding
import com.example.supporthealth.main.domain.models.MealEntity
import com.example.supporthealth.main.domain.models.NutritionFull
import com.example.supporthealth.main.domain.models.ProductEntity
import com.example.supporthealth.nutrition.eating.domain.models.ProductWithGrams
import com.example.supporthealth.nutrition.main.domain.models.Meal
import com.example.supporthealth.nutrition.main.domain.models.NutrientStat
import com.example.supporthealth.nutrition.search.domain.models.Product
import com.google.android.material.color.MaterialColors
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class EatingFragment : Fragment() {

    companion object {
        fun newInstance() = EatingFragment()
    }

    private val args: EatingFragmentArgs by navArgs()

    private val viewModel: EatingViewModel by viewModel { parametersOf(args.mealId) }

    private val adapter = ProductAdapter(arrayListOf()) {
        viewModel.onProductClick(
            Product(
                productId = it.productId,
                name = it.name,
                calories = it.calories,
                protein = it.proteins,
                fat = it.fats,
                carbs = it.carbs
            )
        )
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

        val itemTouchHelper =
            ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.adapterPosition
                    val product = adapter.products[position]
                    viewModel.deleteProduct(product)
                }

                override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
                    return 0.35f
                }

                override fun onChildDraw(
                    c: Canvas, recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    dX: Float, dY: Float,
                    actionState: Int, isCurrentlyActive: Boolean
                ) {
                    val itemView = viewHolder.itemView
                    val context = itemView.context

                    if (dX < 0) {
                        val backgroundColor =
                            ContextCompat.getColor(context, R.color.iced_pink_rose)
                        val background = ColorDrawable(backgroundColor)
                        val left = itemView.right + dX.toInt()
                        val right = itemView.right
                        background.setBounds(left, itemView.top, right, itemView.bottom)
                        background.draw(c)

                        val density = context.resources.displayMetrics.density
                        val iconSize = (24 * density).toInt()
                        val iconMargin = (8 * density).toInt()
                        val textMargin = (8 * density).toInt()

                        val icon = ContextCompat.getDrawable(context, R.drawable.ic_delete_outline)
                        val text = context.getString(R.string.delete)

                        val paintText = Paint().apply {
                            textSize = 16 * density
                            typeface = Typeface.DEFAULT_BOLD
                        }
                        val textWidth = paintText.measureText(text).toInt()
                        val contentWidth =
                            iconSize + iconMargin + textMargin + textWidth + iconMargin

                        val visibleWidth = right - left
                        val revealProgress =
                            (visibleWidth / contentWidth.toFloat()).coerceIn(0f, 1f)

                        val contentLeft = right - visibleWidth + iconMargin
                        val iconLeft = contentLeft
                        val iconRight = iconLeft + iconSize
                        val iconTop = itemView.top + (itemView.height - iconSize) / 2
                        val iconBottom = iconTop + iconSize

                        val alpha = (revealProgress * 255).toInt()
                        icon?.alpha = alpha

                        icon?.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                        icon?.draw(c)

                        paintText.color = ContextCompat.getColor(context, R.color.red)
                        paintText.alpha = alpha
                        paintText.isAntiAlias = true

                        val textX = iconRight + textMargin.toFloat()
                        val textY = iconTop + iconSize / 2f + (paintText.textSize / 3)
                        c.drawText(text, textX, textY, paintText)
                    }

                    super.onChildDraw(
                        c,
                        recyclerView,
                        viewHolder,
                        dX,
                        dY,
                        actionState,
                        isCurrentlyActive
                    )
                }
            })
        itemTouchHelper.attachToRecyclerView(binding.recyclerProducts)
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
            val calDiff = meal.recommendedCalories - meal.calories
            when {
                calDiff > 0 -> {
                    remainingCalories.text = "$calDiff ккал осталось"
                    remainingCalories.setTextColor(
                        MaterialColors.getColor(requireContext(), com.google.android.material.R.attr.colorOnSecondaryFixed, Color.BLACK)
                    )
                }
                calDiff == 0 -> {
                    remainingCalories.text = "Норма достигнута"
                    remainingCalories.setTextColor(
                        ContextCompat.getColor(requireContext(), R.color.see_foam_greene)
                    )
                }
                else -> {
                    remainingCalories.text = "${-calDiff} ккал сверх"
                    remainingCalories.setTextColor(
                        ContextCompat.getColor(requireContext(), R.color.red)
                    )
                }
            }

            val protDiff = meal.recommendedProteins.toInt() - meal.proteins.toInt()
            when {
                protDiff > 0 -> {
                    remainingProtein.text = "$protDiff г осталось"
                    remainingProtein.setTextColor(
                        MaterialColors.getColor(requireContext(), com.google.android.material.R.attr.colorOnSecondaryFixed, Color.BLACK)
                    )
                }
                protDiff == 0 -> {
                    remainingProtein.text = "Норма достигнута"
                    remainingProtein.setTextColor(
                        ContextCompat.getColor(requireContext(), R.color.see_foam_greene)
                    )
                }
                else -> {
                    remainingProtein.text = "${-protDiff} г сверх"
                    remainingProtein.setTextColor(
                        ContextCompat.getColor(requireContext(), R.color.red)
                    )
                }
            }

            val fatDiff = meal.recommendedFats.toInt() - meal.fats.toInt()
            when {
                fatDiff > 0 -> {
                    remainingFat.text = "$fatDiff г осталось"
                    remainingFat.setTextColor(
                        MaterialColors.getColor(requireContext(), com.google.android.material.R.attr.colorOnSecondaryFixed, Color.BLACK)
                    )
                }
                fatDiff == 0 -> {
                    remainingFat.text = "Норма достигнута"
                    remainingFat.setTextColor(
                        ContextCompat.getColor(requireContext(), R.color.see_foam_greene)
                    )
                }
                else -> {
                    remainingFat.text = "${-fatDiff} г сверх"
                    remainingFat.setTextColor(
                        ContextCompat.getColor(requireContext(), R.color.red)
                    )
                }
            }

            val carbDiff = meal.recommendedCarbs.toInt() - meal.carbs.toInt()
            when {
                carbDiff > 0 -> {
                    remainingCarbohydrates.text = "$carbDiff г осталось"
                    remainingCarbohydrates.setTextColor(
                        MaterialColors.getColor(requireContext(), com.google.android.material.R.attr.colorOnSecondaryFixed, Color.BLACK)
                    )
                }
                carbDiff == 0 -> {
                    remainingCarbohydrates.text = "Норма достигнута"
                    remainingCarbohydrates.setTextColor(
                        ContextCompat.getColor(requireContext(), R.color.see_foam_greene)
                    )
                }
                else -> {
                    remainingCarbohydrates.text = "${-carbDiff} г сверх"
                    remainingCarbohydrates.setTextColor(
                        ContextCompat.getColor(requireContext(), R.color.red)
                    )
                }
            }

            protein.text = "${meal.proteins.toInt()} г"
            fat.text = "${meal.fats.toInt()} г"
            carbohydrates.text = "${meal.carbs.toInt()} г"

            staticDonut.updateNutrients(
                listOf(
                    NutrientStat(
                        meal.fats,
                        meal.recommendedFats
                    ),
                    NutrientStat(
                        meal.proteins,
                        meal.recommendedProteins
                    ),
                    NutrientStat(
                        meal.carbs,
                        meal.recommendedCarbs
                    )
                )
            )
            staticDonut.updateCalories(meal.calories.toFloat(), meal.recommendedCalories.toFloat())
        }
    }
}