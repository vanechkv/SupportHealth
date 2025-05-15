package com.example.supporthealth.nutrition.search.ui

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity.INPUT_METHOD_SERVICE
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.supporthealth.R
import com.example.supporthealth.databinding.ErrorViewBinding
import com.example.supporthealth.databinding.FragmentNutritionBinding
import com.example.supporthealth.databinding.FragmentSearchBinding
import com.example.supporthealth.databinding.HistoryViewBinding
import com.example.supporthealth.main.domain.models.MealType
import com.example.supporthealth.nutrition.main.ui.NutritionFragment
import com.example.supporthealth.nutrition.main.ui.NutritionFragmentDirections
import com.example.supporthealth.nutrition.search.domain.models.Product
import com.example.supporthealth.nutrition.search.domain.models.ProductState
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    companion object {
        private const val SEARCH_TEXT = "SEARCH_TEXT"

        fun newInstance() = SearchFragment()
    }

    private lateinit var binding: FragmentSearchBinding

    private val viewModel: SearchViewModel by viewModel()

    private val adapter = ProductAdapter(arrayListOf()) {
        openProductDetails(it)
    }

    private val adapterHistory = ProductAdapter(arrayListOf()) {
        viewModel.onProductClick(it)
        openProductDetails(it)
    }

    private val args: SearchFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeState().observe(viewLifecycleOwner) { state ->
            render(state)
        }

        viewModel.observeHistory().observe(viewLifecycleOwner) { history ->
            adapterHistory.updateProduct(history)
        }

        val historyViewBinding =
            HistoryViewBinding.inflate(layoutInflater, binding.searchContainer, false)
        val historyView = historyViewBinding.root

        binding.buttonClear.setOnClickListener {
            binding.searchEditText.text = null

            val inputMethodManager =
                requireActivity().getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(binding.searchEditText.windowToken, 0)

            binding.searchEditText.clearFocus()

            binding.searchContainer.removeAllViews()
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.searchDebounce(s?.toString().orEmpty())
                binding.buttonClear.visibility = clearButtonVisibility(s)

                adapter.notifyDataSetChanged()

                val history = viewModel.observeHistory().value.orEmpty()

                if (binding.searchEditText.hasFocus() && s.isNullOrEmpty() && history.isNotEmpty()) {
                    adapterHistory.notifyDataSetChanged()
                    binding.searchContainer.addView(historyView)
                }else {
                    binding.searchContainer.removeAllViews()
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }

        }

        binding.searchEditText.addTextChangedListener(textWatcher)

        binding.searchRecycler.adapter = adapter

        historyViewBinding.historyRecycler.adapter = adapterHistory

        historyViewBinding.clear.setOnClickListener {
            viewModel.clearHistory()
            binding.searchContainer.removeAllViews()
        }

        binding.searchEditText.setOnFocusChangeListener { _, hasFocus ->
            val history = viewModel.observeHistory().value.orEmpty()
            if (hasFocus && binding.searchEditText.text.isNullOrEmpty() && history.isNotEmpty()) {
                adapterHistory.notifyDataSetChanged()
                binding.searchContainer.addView(historyView)
            } else {
                binding.searchContainer.removeAllViews()
            }
        }

        when (args.meal) {
            MealType.BREAKFAST -> binding.title.setText(R.string.breakfast)
            MealType.LUNCH -> binding.title.setText(R.string.lunch)
            MealType.AFTERNOON_TEA -> binding.title.setText(R.string.afternoon_tea)
            MealType.DINNER -> binding.title.setText(R.string.dinner)
        }
        binding.buttonBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.saveHistory()
    }


    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun showError(
        text: String,
        explanation: String?,
        iconError: Int,
        isButtonVisible: Boolean = false
    ) {
        if (text.isNotEmpty()) {

            binding.searchContainer.removeAllViews()

            val errorViewBinding =
                ErrorViewBinding.inflate(layoutInflater, binding.searchContainer, false)
            val errorView = errorViewBinding.root

            errorViewBinding.titleError.text = text

            if (!explanation.isNullOrEmpty()) {
                errorViewBinding.descriptionError.text = explanation
                errorViewBinding.descriptionError.visibility = View.VISIBLE
            } else {
                errorViewBinding.descriptionError.visibility = View.GONE
            }

            errorViewBinding.imgError.setImageResource(iconError)

            if (isButtonVisible) {
                errorViewBinding.errorButton.visibility = View.VISIBLE
                errorViewBinding.errorButton.setOnClickListener {
                    binding.searchContainer.removeAllViews()
                    viewModel.searchDebounce(binding.searchEditText.text.toString())
                }
            } else {
                errorViewBinding.errorButton.visibility = View.GONE
            }

            binding.searchContainer.addView(errorView)
        }
    }

    private fun render(state: ProductState) {
        binding.progressBar.visibility = View.GONE
        binding.searchRecycler.visibility = View.GONE
        binding.searchContainer.removeAllViews()

        when (state) {
            is ProductState.Loading -> {
                binding.progressBar.visibility = View.VISIBLE
            }

            is ProductState.Content -> {
                adapter.updateProduct(state.products)
                binding.searchRecycler.visibility = View.VISIBLE
            }

            is ProductState.Empty -> {
                showError(getString(state.message), null, R.drawable.ic_mood_bad, false)
            }

            is ProductState.Error -> {
                showError(
                    getString(state.errorMessage),
                    getString(R.string.internet_error_explanation),
                    R.drawable.ic_signal_wifi_off,
                    true
                )
            }

            is ProductState.SearchHistory -> {
                adapterHistory.updateProduct(state.history)
            }
        }
    }

    private fun openProductDetails(product: Product) {
        val action = SearchFragmentDirections
            .actionNavigationSearchToNavigationProduct(
                meal = args.meal,
                date = args.date,
                productId = product.productId
            )
        findNavController().navigate(action)
    }
}