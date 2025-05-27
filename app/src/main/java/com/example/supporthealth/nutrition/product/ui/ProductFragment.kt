package com.example.supporthealth.nutrition.product.ui

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.supporthealth.R
import com.example.supporthealth.databinding.FragmentProductBinding
import com.example.supporthealth.nutrition.search.ui.SearchFragmentArgs
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProductFragment : Fragment() {

    companion object {
        fun newInstance() = ProductFragment()
    }

    private val viewModel: ProductViewModel by viewModel()

    private lateinit var binding: FragmentProductBinding

    private val args: ProductFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeProductValue().observe(viewLifecycleOwner) { product ->
            binding.calories.text = "${product.calories} ккал"
            binding.protein.text = "%.1f г".format(product.protein)
            binding.fat.text = "%.1f г".format(product.fat)
            binding.carbohydrates.text = "%.1f г".format(product.carbs)
            binding.caloriesValue.text = "${product.calories} ккал"
            binding.proteinValue.text = "%.1f г".format(product.protein)
            binding.fatValue.text = "%.1f г".format(product.fat)
            binding.carbohydratesValue.text = "%.1f г".format(product.carbs)
        }

        binding.gramsEditText.setText(args.grams.toString())

        viewModel.updateGrams(args.grams)

        binding.gramsEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val grams = s?.toString()?.toIntOrNull() ?: 0
                viewModel.updateGrams(grams)
                validateGrams()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })

        binding.title.text = viewModel.getName()

        binding.buttonAdd.setOnClickListener {
            findNavController().navigateUp()
            viewModel.observeProductValue().value?.let {
                viewModel.addProduct(
                    args.date, args.meal, binding.gramsEditText.text.toString().trim().toFloat()
                )
            }
        }

        binding.buttonBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun validateGrams() {
        val grams = binding.gramsEditText.text.toString().toIntOrNull() ?: 0
        binding.buttonAdd.isEnabled = grams > 0
        if (grams == 0) {
            binding.gramsEditText.error =
                getString(R.string.error_grams_zero)
        } else {
            binding.gramsEditText.error = null
        }
    }
}