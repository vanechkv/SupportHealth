package com.example.supporthealth.chat.ui

import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.supporthealth.databinding.FragmentChatDialogListDialogBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

// TODO: Customize parameter argument names
const val ARG_ITEM_COUNT = "item_count"

/**
 *
 * A fragment that shows a list of items as a modal bottom sheet.
 *
 * You can show this modal bottom sheet from your activity like this:
 * <pre>
 *    ChatDialogFragment.newInstance(30).show(supportFragmentManager, "dialog")
 * </pre>
 */
class ChatDialogFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentChatDialogListDialogBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onStart() {
        super.onStart()
        val dialog = dialog as? BottomSheetDialog ?: return
        val bottomSheet =
            dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
                ?: return
        val behavior = BottomSheetBehavior.from(bottomSheet)

        bottomSheet.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        bottomSheet.requestLayout()

        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        behavior.skipCollapsed = true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentChatDialogListDialogBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.buttonBack.setOnClickListener {
            dismiss()
        }
    }

    companion object {

        // TODO: Customize parameters
        fun newInstance(): ChatDialogFragment =
            ChatDialogFragment().apply {
                arguments = Bundle().apply {
                }
            }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}